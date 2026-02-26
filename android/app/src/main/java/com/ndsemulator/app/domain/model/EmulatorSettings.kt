package com.ndsemulator.app.domain.model

/**
 * Emulator settings configuration.
 */
data class EmulatorSettings(
    // Graphics settings
    val renderer: GraphicsRenderer = GraphicsRenderer.OPENGL,
    val screenLayout: ScreenLayout = ScreenLayout.HORIZONTAL,
    val screenRotation: Int = 0,
    val scaleMode: ScaleMode = ScaleMode.FIT,
    val filterMode: FilterMode = FilterMode.NONE,
    val showFps: Boolean = false,
    val frameskip: Int = 0,
    
    // Audio settings
    val audioEnabled: Boolean = true,
    val audioVolume: Float = 1.0f,
    val microphoneEnabled: Boolean = true,
    
    // Control settings
    val touchControlsEnabled: Boolean = true,
    val showTouchButtons: Boolean = true,
    val buttonSize: Float = 1.0f,
    val buttonOpacity: Float = 0.7f,
    val hapticFeedback: Boolean = true,
    val gyroEnabled: Boolean = false,
    val dpadSensitivity: Float = 0.5f,
    
    // Emulation settings
    val dsiMode: Boolean = false,
    val wifiSimulation: Boolean = true,
    val autoSave: Boolean = true,
    val cpuMode: CpuMode = CpuMode.JIT,
    
    // General settings
    val romDirectory: String = "",
    val showCoverArt: Boolean = true,
    val useBios: Boolean = false
)

/**
 * Graphics rendering mode.
 */
enum class GraphicsRenderer {
    SOFTWARE,
    OPENGL,
    HARDWARE
}

/**
 * Screen layout modes.
 */
enum class ScreenLayout {
    VERTICAL,
    HORIZONTAL,
    HYBRID,
    TOP_ONLY,
    BOTTOM_ONLY
}

/**
 * Scale modes for the screen.
 */
enum class ScaleMode {
    FIT,
    STRETCH,
    ORIGINAL,
    ASPECT_RATIO
}

/**
 * Filter modes for graphics.
 */
enum class FilterMode {
    NONE,
    SMOOTH,
    PIXELATED
}

/**
 * CPU emulation modes.
 */
enum class CpuMode {
    JIT,          // Just-in-time compilation (fast)
    INTERPRETER   // Interpreter (accurate but slow)
}
