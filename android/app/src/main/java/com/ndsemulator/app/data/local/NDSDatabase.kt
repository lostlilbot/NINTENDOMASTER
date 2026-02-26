package com.ndsemulator.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ndsemulator.app.data.local.dao.CheatDao
import com.ndsemulator.app.data.local.dao.GameDao
import com.ndsemulator.app.data.local.dao.SaveStateDao
import com.ndsemulator.app.data.local.entity.CheatEntity
import com.ndsemulator.app.data.local.entity.GameEntity
import com.ndsemulator.app.data.local.entity.SaveStateEntity

/**
 * Room database for the NDS Emulator app.
 */
@Database(
    entities = [
        GameEntity::class,
        SaveStateEntity::class,
        CheatEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NDSDatabase : RoomDatabase() {
    
    abstract fun gameDao(): GameDao
    abstract fun saveStateDao(): SaveStateDao
    abstract fun cheatDao(): CheatDao
    
    companion object {
        const val DATABASE_NAME = "nds_emulator.db"
    }
}
