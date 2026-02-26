package com.ndsemulator.app.ui.screens.library

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndsemulator.app.domain.model.Game
import com.ndsemulator.app.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for the Library screen.
 */
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val games: StateFlow<List<Game>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                gameRepository.getAllGames()
            } else {
                gameRepository.searchGames(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun refreshLibrary() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val defaultDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                gameRepository.scanDirectory(defaultDir.absolutePath)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun scanForGames() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Scan common ROM directories
                val directories = listOf(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    File(Environment.getExternalStorageDirectory(), "ROMs/NDS")
                )
                
                directories.forEach { dir ->
                    if (dir.exists() && dir.isDirectory) {
                        gameRepository.scanDirectory(dir.absolutePath)
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteGame(game: Game) {
        viewModelScope.launch {
            gameRepository.deleteGame(game)
        }
    }
    
    fun toggleFavorite(game: Game) {
        viewModelScope.launch {
            gameRepository.updateGame(game.copy(isFavorite = !game.isFavorite))
        }
    }
}
