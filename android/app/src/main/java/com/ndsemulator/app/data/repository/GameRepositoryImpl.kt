package com.ndsemulator.app.data.repository

import android.content.Context
import com.ndsemulator.app.data.local.dao.CheatDao
import com.ndsemulator.app.data.local.dao.GameDao
import com.ndsemulator.app.data.local.dao.SaveStateDao
import com.ndsemulator.app.data.local.entity.CheatEntity
import com.ndsemulator.app.data.local.entity.GameEntity
import com.ndsemulator.app.data.local.entity.SaveStateEntity
import com.ndsemulator.app.domain.model.Cheat
import com.ndsemulator.app.domain.model.Game
import com.ndsemulator.app.domain.model.GameFileType
import com.ndsemulator.app.domain.model.GameMetadata
import com.ndsemulator.app.domain.model.GameSave
import com.ndsemulator.app.domain.model.SaveState
import com.ndsemulator.app.domain.repository.GameRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GameRepository using Room database.
 */
@Singleton
class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
    private val saveStateDao: SaveStateDao,
    private val cheatDao: CheatDao,
    @ApplicationContext private val context: Context
) : GameRepository {
    
    override fun getAllGames(): Flow<List<Game>> {
        return gameDao.getAllGames().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun searchGames(query: String): Flow<List<Game>> {
        return gameDao.searchGames(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getGameById(id: Long): Game? {
        return gameDao.getGameById(id)?.toDomain()
    }
    
    override suspend fun addGame(game: Game): Long {
        return gameDao.insertGame(GameEntity.fromDomain(game))
    }
    
    override suspend fun updateGame(game: Game) {
        gameDao.updateGame(GameEntity.fromDomain(game))
    }
    
    override suspend fun deleteGame(game: Game) {
        // Delete the ROM file
        withContext(Dispatchers.IO) {
            val file = File(game.filePath)
            if (file.exists()) {
                file.delete()
            }
        }
        gameDao.deleteGame(GameEntity.fromDomain(game))
    }
    
    override suspend fun scanDirectory(directoryPath: String): List<Game> = withContext(Dispatchers.IO) {
        val directory = File(directoryPath)
        val games = mutableListOf<Game>()
        
        if (!directory.exists() || !directory.isDirectory) {
            return@withContext games
        }
        
        val supportedExtensions = listOf("nds", "zip", "7z")
        
        directory.listFiles()?.forEach { file ->
            if (file.isFile) {
                val extension = file.extension.lowercase()
                if (extension in supportedExtensions) {
                    val game = createGameFromFile(file)
                    if (game != null) {
                        // Check if game already exists
                        val existingGame = gameDao.getGameByPath(file.absolutePath)
                        if (existingGame == null) {
                            val id = gameDao.insertGame(GameEntity.fromDomain(game))
                            games.add(game.copy(id = id))
                        }
                    }
                }
            }
        }
        
        games
    }
    
    override suspend fun importRom(filePath: String): Game? = withContext(Dispatchers.IO) {
        val file = File(filePath)
        if (!file.exists()) return@withContext null
        
        createGameFromFile(file)
    }
    
    private fun createGameFromFile(file: File): Game? {
        val fileName = file.name
        val extension = file.extension.lowercase()
        
        val fileType = when (extension) {
            "nds" -> GameFileType.NDS
            "zip" -> GameFileType.ZIP
            "7z" -> GameFileType.SEVEN_ZIP
            else -> return null
        }
        
        val title = fileName
            .substringBeforeLast(".")
            .replace("_", " ")
            .replace("-", " ")
        
        return Game(
            title = title,
            filePath = file.absolutePath,
            fileName = fileName,
            fileSize = file.length(),
            fileType = fileType,
            dateAdded = System.currentTimeMillis()
        )
    }
    
    override suspend fun getGameSave(gameId: Long, slot: Int): GameSave? {
        // In a real implementation, this would read from the save file
        return null
    }
    
    override suspend fun saveGameData(gameId: Long, data: ByteArray) {
        // In a real implementation, this would save to a file
    }
    
    override fun getSaveStates(gameId: Long): Flow<List<SaveState>> {
        return saveStateDao.getSaveStatesForGame(gameId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun saveState(saveState: SaveState) {
        saveStateDao.insertSaveState(SaveStateEntity.fromDomain(saveState))
    }
    
    override suspend fun loadSaveState(gameId: Long, slot: Int): SaveState? {
        return saveStateDao.getSaveState(gameId, slot)?.toDomain()
    }
    
    override suspend fun deleteSaveState(saveState: SaveState) {
        saveStateDao.deleteSaveState(SaveStateEntity.fromDomain(saveState))
    }
    
    override fun getCheats(gameId: Long): Flow<List<Cheat>> {
        return cheatDao.getCheatsForGame(gameId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun addCheat(cheat: Cheat): Long {
        return cheatDao.insertCheat(CheatEntity.fromDomain(cheat))
    }
    
    override suspend fun updateCheat(cheat: Cheat) {
        cheatDao.updateCheat(CheatEntity.fromDomain(cheat))
    }
    
    override suspend fun deleteCheat(cheat: Cheat) {
        cheatDao.deleteCheat(CheatEntity.fromDomain(cheat))
    }
    
    override suspend fun toggleCheat(cheatId: Long, enabled: Boolean) {
        cheatDao.setCheatEnabled(cheatId, enabled)
    }
}
