package com.ndsemulator.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndsemulator.app.domain.model.CpuMode
import com.ndsemulator.app.domain.model.EmulatorSettings
import com.ndsemulator.app.domain.model.FilterMode
import com.ndsemulator.app.domain.model.GraphicsRenderer
import com.ndsemulator.app.domain.model.ScreenLayout
import com.ndsemulator.app.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    val settings: StateFlow<EmulatorSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmulatorSettings()
        )
    
    private fun updateSettings(update: (EmulatorSettings) -> EmulatorSettings) {
        viewModelScope.launch {
            val current = settings.value
            settingsRepository.updateSettings(update(current))
        }
    }
    
    fun updateRenderer(value: String) {
        updateSettings { it.copy(renderer = GraphicsRenderer.valueOf(value)) }
    }
    
    fun updateScreenLayout(value: String) {
        updateSettings { it.copy(screenLayout = ScreenLayout.valueOf(value)) }
    }
    
    fun updateFilterMode(value: String) {
        updateSettings { it.copy(filterMode = FilterMode.valueOf(value)) }
    }
    
    fun updateFrameskip(value: Int) {
        updateSettings { it.copy(frameskip = value) }
    }
    
    fun updateShowFps(value: Boolean) {
        updateSettings { it.copy(showFps = value) }
    }
    
    fun updateAudioEnabled(value: Boolean) {
        updateSettings { it.copy(audioEnabled = value) }
    }
    
    fun updateAudioVolume(value: Float) {
        updateSettings { it.copy(audioVolume = value) }
    }
    
    fun updateMicrophoneEnabled(value: Boolean) {
        updateSettings { it.copy(microphoneEnabled = value) }
    }
    
    fun updateTouchControlsEnabled(value: Boolean) {
        updateSettings { it.copy(touchControlsEnabled = value) }
    }
    
    fun updateShowTouchButtons(value: Boolean) {
        updateSettings { it.copy(showTouchButtons = value) }
    }
    
    fun updateButtonSize(value: Float) {
        updateSettings { it.copy(buttonSize = value) }
    }
    
    fun updateButtonOpacity(value: Float) {
        updateSettings { it.copy(buttonOpacity = value) }
    }
    
    fun updateHapticFeedback(value: Boolean) {
        updateSettings { it.copy(hapticFeedback = value) }
    }
    
    fun updateGyroEnabled(value: Boolean) {
        updateSettings { it.copy(gyroEnabled = value) }
    }
    
    fun updateDsiMode(value: Boolean) {
        updateSettings { it.copy(dsiMode = value) }
    }
    
    fun updateWifiSimulation(value: Boolean) {
        updateSettings { it.copy(wifiSimulation = value) }
    }
    
    fun updateAutoSave(value: Boolean) {
        updateSettings { it.copy(autoSave = value) }
    }
    
    fun updateCpuMode(value: String) {
        updateSettings { it.copy(cpuMode = CpuMode.valueOf(value)) }
    }
    
    fun updateShowCoverArt(value: Boolean) {
        updateSettings { it.copy(showCoverArt = value) }
    }
    
    fun updateUseBios(value: Boolean) {
        updateSettings { it.copy(useBios = value) }
    }
    
    fun resetToDefaults() {
        viewModelScope.launch {
            settingsRepository.resetToDefaults()
        }
    }
}
