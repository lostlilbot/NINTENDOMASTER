package com.ndsemulator.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ndsemulator.app.domain.model.CpuMode
import com.ndsemulator.app.domain.model.EmulatorSettings
import com.ndsemulator.app.domain.model.FilterMode
import com.ndsemulator.app.domain.model.GraphicsRenderer
import com.ndsemulator.app.domain.model.ScaleMode
import com.ndsemulator.app.domain.model.ScreenLayout
import com.ndsemulator.app.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "emulator_settings")

/**
 * Implementation of SettingsRepository using DataStore.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    
    private object Keys {
        // Graphics
        val RENDERER = stringPreferencesKey("renderer")
        val SCREEN_LAYOUT = stringPreferencesKey("screen_layout")
        val SCREEN_ROTATION = intPreferencesKey("screen_rotation")
        val SCALE_MODE = stringPreferencesKey("scale_mode")
        val FILTER_MODE = stringPreferencesKey("filter_mode")
        val SHOW_FPS = booleanPreferencesKey("show_fps")
        val FRAMESKIP = intPreferencesKey("frameskip")
        
        // Audio
        val AUDIO_ENABLED = booleanPreferencesKey("audio_enabled")
        val AUDIO_VOLUME = floatPreferencesKey("audio_volume")
        val MICROPHONE_ENABLED = booleanPreferencesKey("microphone_enabled")
        
        // Controls
        val TOUCH_CONTROLS_ENABLED = booleanPreferencesKey("touch_controls_enabled")
        val SHOW_TOUCH_BUTTONS = booleanPreferencesKey("show_touch_buttons")
        val BUTTON_SIZE = floatPreferencesKey("button_size")
        val BUTTON_OPACITY = floatPreferencesKey("button_opacity")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val GYRO_ENABLED = booleanPreferencesKey("gyro_enabled")
        val DPAD_SENSITIVITY = floatPreferencesKey("dpad_sensitivity")
        
        // Emulation
        val DSI_MODE = booleanPreferencesKey("dsi_mode")
        val WIFI_SIMULATION = booleanPreferencesKey("wifi_simulation")
        val AUTO_SAVE = booleanPreferencesKey("auto_save")
        val CPU_MODE = stringPreferencesKey("cpu_mode")
        
        // General
        val ROM_DIRECTORY = stringPreferencesKey("rom_directory")
        val SHOW_COVER_ART = booleanPreferencesKey("show_cover_art")
        val USE_BIOS = booleanPreferencesKey("use_bios")
    }
    
    override fun getSettings(): Flow<EmulatorSettings> {
        return context.dataStore.data.map { preferences ->
            EmulatorSettings(
                renderer = preferences[Keys.RENDERER]?.let { GraphicsRenderer.valueOf(it) } 
                    ?: GraphicsRenderer.OPENGL,
                screenLayout = preferences[Keys.SCREEN_LAYOUT]?.let { ScreenLayout.valueOf(it) } 
                    ?: ScreenLayout.HORIZONTAL,
                screenRotation = preferences[Keys.SCREEN_ROTATION] ?: 0,
                scaleMode = preferences[Keys.SCALE_MODE]?.let { ScaleMode.valueOf(it) } 
                    ?: ScaleMode.FIT,
                filterMode = preferences[Keys.FILTER_MODE]?.let { FilterMode.valueOf(it) } 
                    ?: FilterMode.NONE,
                showFps = preferences[Keys.SHOW_FPS] ?: false,
                frameskip = preferences[Keys.FRAMESKIP] ?: 0,
                audioEnabled = preferences[Keys.AUDIO_ENABLED] ?: true,
                audioVolume = preferences[Keys.AUDIO_VOLUME] ?: 1.0f,
                microphoneEnabled = preferences[Keys.MICROPHONE_ENABLED] ?: true,
                touchControlsEnabled = preferences[Keys.TOUCH_CONTROLS_ENABLED] ?: true,
                showTouchButtons = preferences[Keys.SHOW_TOUCH_BUTTONS] ?: true,
                buttonSize = preferences[Keys.BUTTON_SIZE] ?: 1.0f,
                buttonOpacity = preferences[Keys.BUTTON_OPACITY] ?: 0.7f,
                hapticFeedback = preferences[Keys.HAPTIC_FEEDBACK] ?: true,
                gyroEnabled = preferences[Keys.GYRO_ENABLED] ?: false,
                dpadSensitivity = preferences[Keys.DPAD_SENSITIVITY] ?: 0.5f,
                dsiMode = preferences[Keys.DSI_MODE] ?: false,
                wifiSimulation = preferences[Keys.WIFI_SIMULATION] ?: true,
                autoSave = preferences[Keys.AUTO_SAVE] ?: true,
                cpuMode = preferences[Keys.CPU_MODE]?.let { CpuMode.valueOf(it) } 
                    ?: CpuMode.JIT,
                romDirectory = preferences[Keys.ROM_DIRECTORY] ?: "",
                showCoverArt = preferences[Keys.SHOW_COVER_ART] ?: true,
                useBios = preferences[Keys.USE_BIOS] ?: false
            )
        }
    }
    
    override suspend fun updateSettings(settings: EmulatorSettings) {
        context.dataStore.edit { preferences ->
            preferences[Keys.RENDERER] = settings.renderer.name
            preferences[Keys.SCREEN_LAYOUT] = settings.screenLayout.name
            preferences[Keys.SCREEN_ROTATION] = settings.screenRotation
            preferences[Keys.SCALE_MODE] = settings.scaleMode.name
            preferences[Keys.FILTER_MODE] = settings.filterMode.name
            preferences[Keys.SHOW_FPS] = settings.showFps
            preferences[Keys.FRAMESKIP] = settings.frameskip
            preferences[Keys.AUDIO_ENABLED] = settings.audioEnabled
            preferences[Keys.AUDIO_VOLUME] = settings.audioVolume
            preferences[Keys.MICROPHONE_ENABLED] = settings.microphoneEnabled
            preferences[Keys.TOUCH_CONTROLS_ENABLED] = settings.touchControlsEnabled
            preferences[Keys.SHOW_TOUCH_BUTTONS] = settings.showTouchButtons
            preferences[Keys.BUTTON_SIZE] = settings.buttonSize
            preferences[Keys.BUTTON_OPACITY] = settings.buttonOpacity
            preferences[Keys.HAPTIC_FEEDBACK] = settings.hapticFeedback
            preferences[Keys.GYRO_ENABLED] = settings.gyroEnabled
            preferences[Keys.DPAD_SENSITIVITY] = settings.dpadSensitivity
            preferences[Keys.DSI_MODE] = settings.dsiMode
            preferences[Keys.WIFI_SIMULATION] = settings.wifiSimulation
            preferences[Keys.AUTO_SAVE] = settings.autoSave
            preferences[Keys.CPU_MODE] = settings.cpuMode.name
            preferences[Keys.ROM_DIRECTORY] = settings.romDirectory
            preferences[Keys.SHOW_COVER_ART] = settings.showCoverArt
            preferences[Keys.USE_BIOS] = settings.useBios
        }
    }
    
    override suspend fun resetToDefaults() {
        context.dataStore.edit { it.clear() }
    }
    
    override suspend fun <T> getSetting(key: String, default: T): T {
        return when (default) {
            is Boolean -> context.dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey(key)] as? T ?: default
            }.let { flow ->
                var result = default
                flow.collect { result = it }
                result
            }
            is Int -> context.dataStore.data.map { preferences ->
                preferences[intPreferencesKey(key)] as? T ?: default
            }.let { flow ->
                var result = default
                flow.collect { result = it }
                result
            }
            is Float -> context.dataStore.data.map { preferences ->
                preferences[floatPreferencesKey(key)] as? T ?: default
            }.let { flow ->
                var result = default
                flow.collect { result = it }
                result
            }
            is String -> context.dataStore.data.map { preferences ->
                preferences[stringPreferencesKey(key)] as? T ?: default
            }.let { flow ->
                var result = default
                flow.collect { result = it }
                result
            }
            else -> default
        }
    }
    
    override suspend fun <T> setSetting(key: String, value: T) {
        context.dataStore.edit { preferences ->
            when (value) {
                is Boolean -> preferences[booleanPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is Float -> preferences[floatPreferencesKey(key)] = value
                is String -> preferences[stringPreferencesKey(key)] = value
            }
        }
    }
}
