package com.ndsemulator.app.domain.repository

import com.ndsemulator.app.domain.model.Cheat
import com.ndsemulator.app.domain.model.Game
import com.ndsemulator.app.domain.model.GameSave
import com.ndsemulator.app.domain.model.SaveState
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for game-related operations.
 */
interface GameRepository {
    
    /**
     * Get all games in the library.
     */
    fun getAllGames(): Flow<List<Game>>
    
    /**
     * Search games by name.
     */
    fun searchGames(query: String): Flow<List<Game>>
    
    /**
     * Get a specific game by ID.
     */
    suspend fun getGameById(id: Long): Game?
    
    /**
     * Add a new game to the library.
     */
    suspend fun addGame(game: Game): Long
    
    /**
     * Update an existing game.
     */
    suspend fun updateGame(game: Game)
    
    /**
     * Delete a game from the library.
     */
    suspend fun deleteGame(game: Game)
    
    /**
     * Scan a directory for ROM files and add them to the library.
     */
    suspend fun scanDirectory(directoryPath: String): List<Game>
    
    /**
     * Import a ROM file to the library.
     */
    suspend fun importRom(filePath: String): Game?
    
    /**
     * Get game save data.
     */
    suspend fun getGameSave(gameId: Long, slot: Int): GameSave?
    
    /**
     * Save game data.
     */
    suspend fun saveGameData(gameId: Long, data: ByteArray)
    
    /**
     * Get all save states for a game.
     */
    fun getSaveStates(gameId: Long): Flow<List<SaveState>>
    
    /**
     * Save a save state.
     */
    suspend fun saveState(saveState: SaveState)
    
    /**
     * Load a save state.
     */
    suspend fun loadSaveState(gameId: Long, slot: Int): SaveState?
    
    /**
     * Delete a save state.
     */
    suspend fun deleteSaveState(saveState: SaveState)
    
    /**
     * Get cheats for a game.
     */
    fun getCheats(gameId: Long): Flow<List<Cheat>>
    
    /**
     * Add a cheat.
     */
    suspend fun addCheat(cheat: Cheat): Long
    
    /**
     * Update a cheat.
     */
    suspend fun updateCheat(cheat: Cheat)
    
    /**
     * Delete a cheat.
     */
    suspend fun deleteCheat(cheat: Cheat)
    
    /**
     * Toggle cheat enabled state.
     */
    suspend fun toggleCheat(cheatId: Long, enabled: Boolean)
}
