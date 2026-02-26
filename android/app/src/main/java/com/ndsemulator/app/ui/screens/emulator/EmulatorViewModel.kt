package com.ndsemulator.app.ui.screens.emulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndsemulator.app.domain.model.EmulatorSettings
import com.ndsemulator.app.domain.model.Game
import com.ndsemulator.app.domain.model.ScreenLayout
import com.ndsemulator.app.domain.repository.GameRepository
import com.ndsemulator.app.domain.repository.SettingsRepository
import com.ndsemulator.app.emulator.EmulatorCore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Emulator screen.
 */
@HiltViewModel
class EmulatorViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
    private val emulatorCore: EmulatorCore
) : ViewModel() {
    
    private val _game = MutableStateFlow<Game?>(null)
    val game: StateFlow<Game?> = _game.asStateFlow()
    
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    
    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()
    
    private val _isFastForward = MutableStateFlow(false)
    val isFastForward: StateFlow<Boolean> = _isFastForward.asStateFlow()
    
    val settings: StateFlow<EmulatorSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmulatorSettings()
        )
    
    val screenLayout: StateFlow<ScreenLayout> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmulatorSettings()
        ).let { flow ->
            MutableStateFlow(ScreenLayout.HORIZONTAL).also { layoutFlow ->
                viewModelScope.launch {
                    settingsRepository.getSettings().collect {
                        layoutFlow.value = it.screenLayout
                    }
                }
            }
        }
    
    val showSaveStateDialog = MutableStateFlow(false)
    
    fun loadGame(gameId: Long) {
        viewModelScope.launch {
            _game.value = gameRepository.getGameById(gameId)
        }
    }
    
    fun startEmulation() {
        val currentGame = _game.value ?: return
        
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                emulatorCore.configure(settings)
                emulatorCore.loadRom(currentGame.filePath)
                emulatorCore.start()
                _isRunning.value = true
                _isPaused.value = false
                return@collect
            }
        }
    }
    
    fun pauseEmulation() {
        emulatorCore.pause()
        _isPaused.value = true
    }
    
    fun resumeEmulation() {
        emulatorCore.resume()
        _isPaused.value = false
    }
    
    fun stopEmulation() {
        emulatorCore.stop()
        _isRunning.value = false
        _isPaused.value = false
    }
    
    fun toggleFastForward() {
        _isFastForward.value = !_isFastForward.value
        emulatorCore.setFastForward(_isFastForward.value)
    }
    
    fun handleInput(button: String, pressed: Boolean) {
        emulatorCore.setButtonState(button, pressed)
    }
    
    fun handleTouchScreenTap(x: Float, y: Float) {
        emulatorCore.setTouchPosition(x, y)
    }
    
    fun saveState(slot: Int) {
        val currentGame = _game.value ?: return
        viewModelScope.launch {
            val stateData = emulatorCore.getSaveState()
            if (stateData != null) {
                gameRepository.saveState(
                    com.ndsemulator.app.domain.model.SaveState(
                        gameId = currentGame.id,
                        slot = slot,
                        data = stateData
                    )
                )
            }
        }
    }
    
    fun loadState(slot: Int) {
        val currentGame = _game.value ?: return
        viewModelScope.launch {
            val saveState = gameRepository.loadSaveState(currentGame.id, slot)
            saveState?.let {
                emulatorCore.loadSaveState(it.data)
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        emulatorCore.stop()
    }
}
