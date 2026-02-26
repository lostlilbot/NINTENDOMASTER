package com.ndsemulator.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ndsemulator.app.data.local.entity.CheatEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for cheat operations.
 */
@Dao
interface CheatDao {
    
    @Query("SELECT * FROM cheats WHERE gameId = :gameId ORDER BY name ASC")
    fun getCheatsForGame(gameId: Long): Flow<List<CheatEntity>>
    
    @Query("SELECT * FROM cheats WHERE id = :id")
    suspend fun getCheatById(id: Long): CheatEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheat(cheat: CheatEntity): Long
    
    @Update
    suspend fun updateCheat(cheat: CheatEntity)
    
    @Delete
    suspend fun deleteCheat(cheat: CheatEntity)
    
    @Query("DELETE FROM cheats WHERE id = :id")
    suspend fun deleteCheatById(id: Long)
    
    @Query("DELETE FROM cheats WHERE gameId = :gameId")
    suspend fun deleteAllCheatsForGame(gameId: Long)
    
    @Query("UPDATE cheats SET enabled = :enabled WHERE id = :id")
    suspend fun setCheatEnabled(id: Long, enabled: Boolean)
}
