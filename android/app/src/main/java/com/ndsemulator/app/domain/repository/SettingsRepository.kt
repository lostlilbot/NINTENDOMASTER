package com.ndsemulator.app.domain.repository

import com.ndsemulator.app.domain.model.EmulatorSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for emulator settings.
 */
interface SettingsRepository {
    
    /**
     * Get current emulator settings as a flow.
     */
    fun getSettings(): Flow<EmulatorSettings>
    
    /**
     * Update emulator settings.
     */
    suspend fun updateSettings(settings: EmulatorSettings)
    
    /**
     * Reset settings to default values.
     */
    suspend fun resetToDefaults()
    
    /**
     * Get a specific setting value.
     */
    suspend fun <T> getSetting(key: String, default: T): T
    
    /**
     * Set a specific setting value.
     */
    suspend fun <T> setSetting(key: String, value: T)
}
