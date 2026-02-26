package com.ndsemulator.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ndsemulator.app.data.local.entity.SaveStateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for save state operations.
 */
@Dao
interface SaveStateDao {
    
    @Query("SELECT * FROM save_states WHERE gameId = :gameId ORDER BY slot ASC")
    fun getSaveStatesForGame(gameId: Long): Flow<List<SaveStateEntity>>
    
    @Query("SELECT * FROM save_states WHERE gameId = :gameId AND slot = :slot")
    suspend fun getSaveState(gameId: Long, slot: Int): SaveStateEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaveState(saveState: SaveStateEntity): Long
    
    @Delete
    suspend fun deleteSaveState(saveState: SaveStateEntity)
    
    @Query("DELETE FROM save_states WHERE gameId = :gameId AND slot = :slot")
    suspend fun deleteSaveStateBySlot(gameId: Long, slot: Int)
    
    @Query("DELETE FROM save_states WHERE gameId = :gameId")
    suspend fun deleteAllSaveStatesForGame(gameId: Long)
}
