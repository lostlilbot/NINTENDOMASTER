package com.ndsemulator.app.di

import android.content.Context
import androidx.room.Room
import com.ndsemulator.app.data.local.NDSDatabase
import com.ndsemulator.app.data.local.dao.CheatDao
import com.ndsemulator.app.data.local.dao.GameDao
import com.ndsemulator.app.data.local.dao.SaveStateDao
import com.ndsemulator.app.data.repository.GameRepositoryImpl
import com.ndsemulator.app.data.repository.SettingsRepositoryImpl
import com.ndsemulator.app.domain.repository.GameRepository
import com.ndsemulator.app.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NDSDatabase {
        return Room.databaseBuilder(
            context,
            NDSDatabase::class.java,
            NDSDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideGameDao(database: NDSDatabase): GameDao {
        return database.gameDao()
    }
    
    @Provides
    @Singleton
    fun provideSaveStateDao(database: NDSDatabase): SaveStateDao {
        return database.saveStateDao()
    }
    
    @Provides
    @Singleton
    fun provideCheatDao(database: NDSDatabase): CheatDao {
        return database.cheatDao()
    }
}

/**
 * Hilt module for binding repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository
    
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
