package com.ndsemulator.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ndsemulator.app.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for game operations.
 */
@Dao
interface GameDao {
    
    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<GameEntity>>
    
    @Query("SELECT * FROM games WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun searchGames(query: String): Flow<List<GameEntity>>
    
    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: Long): GameEntity?
    
    @Query("SELECT * FROM games WHERE filePath = :filePath")
    suspend fun getGameByPath(filePath: String): GameEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long
    
    @Update
    suspend fun updateGame(game: GameEntity)
    
    @Delete
    suspend fun deleteGame(game: GameEntity)
    
    @Query("UPDATE games SET lastPlayed = :timestamp WHERE id = :gameId")
    suspend fun updateLastPlayed(gameId: Long, timestamp: Long)
    
    @Query("UPDATE games SET playTime = playTime + :additionalTime WHERE id = :gameId")
    suspend fun updatePlayTime(gameId: Long, additionalTime: Long)
    
    @Query("UPDATE games SET isFavorite = :isFavorite WHERE id = :gameId")
    suspend fun updateFavorite(gameId: Long, isFavorite: Boolean)
    
    @Query("SELECT * FROM games WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteGames(): Flow<List<GameEntity>>
    
    @Query("SELECT * FROM games ORDER BY lastPlayed DESC LIMIT :limit")
    fun getRecentGames(limit: Int): Flow<List<GameEntity>>
}
