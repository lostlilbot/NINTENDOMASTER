package com.ndsemulator.app.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Settings screen for configuring emulator options.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Graphics Settings
            SettingsSection(title = "Graphics") {
                SettingsSelector(
                    title = "Renderer",
                    value = settings.renderer.name,
                    options = listOf("OPENGL", "SOFTWARE", "HARDWARE"),
                    onValueChange = { viewModel.updateRenderer(it) }
                )
                
                SettingsSelector(
                    title = "Screen Layout",
                    value = settings.screenLayout.name,
                    options = listOf("HORIZONTAL", "VERTICAL", "HYBRID", "TOP_ONLY", "BOTTOM_ONLY"),
                    onValueChange = { viewModel.updateScreenLayout(it) }
                )
                
                SettingsSelector(
                    title = "Filter",
                    value = settings.filterMode.name,
                    options = listOf("NONE", "SMOOTH", "PIXELATED"),
                    onValueChange = { viewModel.updateFilterMode(it) }
                )
                
                var frameskip by remember { mutableIntStateOf(settings.frameskip) }
                SettingsSlider(
                    title = "Frameskip",
                    value = frameskip.toFloat(),
                    valueRange = 0f..6f,
                    steps = 5,
                    onValueChange = { 
                        frameskip = it.toInt()
                        viewModel.updateFrameskip(frameskip)
                    }
                )
                
                SettingsSwitch(
                    title = "Show FPS",
                    checked = settings.showFps,
                    onCheckedChange = { viewModel.updateShowFps(it) }
                )
            }
            
            // Audio Settings
            SettingsSection(title = "Audio") {
                SettingsSwitch(
                    title = "Enable Audio",
                    checked = settings.audioEnabled,
                    onCheckedChange = { viewModel.updateAudioEnabled(it) }
                )
                
                var volume by remember { mutableFloatStateOf(settings.audioVolume) }
                SettingsSlider(
                    title = "Volume",
                    value = volume,
                    valueRange = 0f..1f,
                    onValueChange = { 
                        volume = it
                        viewModel.updateAudioVolume(volume)
                    }
                )
                
                SettingsSwitch(
                    title = "Microphone",
                    checked = settings.microphoneEnabled,
                    onCheckedChange = { viewModel.updateMicrophoneEnabled(it) }
                )
            }
            
            // Control Settings
            SettingsSection(title = "Controls") {
                SettingsSwitch(
                    title = "Touch Controls",
                    checked = settings.touchControlsEnabled,
                    onCheckedChange = { viewModel.updateTouchControlsEnabled(it) }
                )
                
                SettingsSwitch(
                    title = "Show Touch Buttons",
                    checked = settings.showTouchButtons,
                    onCheckedChange = { viewModel.updateShowTouchButtons(it) }
                )
                
                var buttonSize by remember { mutableFloatStateOf(settings.buttonSize) }
                SettingsSlider(
                    title = "Button Size",
                    value = buttonSize,
                    valueRange = 0.5f..2f,
                    onValueChange = { 
                        buttonSize = it
                        viewModel.updateButtonSize(buttonSize)
                    }
                )
                
                var buttonOpacity by remember { mutableFloatStateOf(settings.buttonOpacity) }
                SettingsSlider(
                    title = "Button Opacity",
                    value = buttonOpacity,
                    valueRange = 0.1f..1f,
                    onValueChange = { 
                        buttonOpacity = it
                        viewModel.updateButtonOpacity(buttonOpacity)
                    }
                )
                
                SettingsSwitch(
                    title = "Haptic Feedback",
                    checked = settings.hapticFeedback,
                    onCheckedChange = { viewModel.updateHapticFeedback(it) }
                )
                
                SettingsSwitch(
                    title = "Gyroscope",
                    checked = settings.gyroEnabled,
                    onCheckedChange = { viewModel.updateGyroEnabled(it) }
                )
            }
            
            // Emulation Settings
            SettingsSection(title = "Emulation") {
                SettingsSwitch(
                    title = "DSi Mode",
                    checked = settings.dsiMode,
                    onCheckedChange = { viewModel.updateDsiMode(it) }
                )
                
                SettingsSwitch(
                    title = "WiFi Simulation",
                    checked = settings.wifiSimulation,
                    onCheckedChange = { viewModel.updateWifiSimulation(it) }
                )
                
                SettingsSwitch(
                    title = "Auto Save",
                    checked = settings.autoSave,
                    onCheckedChange = { viewModel.updateAutoSave(it) }
                )
                
                SettingsSelector(
                    title = "CPU Mode",
                    value = settings.cpuMode.name,
                    options = listOf("JIT", "INTERPRETER"),
                    onValueChange = { viewModel.updateCpuMode(it) }
                )
            }
            
            // General Settings
            SettingsSection(title = "General") {
                SettingsSwitch(
                    title = "Show Cover Art",
                    checked = settings.showCoverArt,
                    onCheckedChange = { viewModel.updateShowCoverArt(it) }
                )
                
                SettingsSwitch(
                    title = "Use BIOS",
                    checked = settings.useBios,
                    onCheckedChange = { viewModel.updateUseBios(it) }
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsSlider(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Column {
        Text(text = title)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps
        )
    }
}

@Composable
private fun SettingsSelector(
    title: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { androidx.compose.runtime.mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Text(text = value, color = MaterialTheme.colorScheme.primary)
    }
}
