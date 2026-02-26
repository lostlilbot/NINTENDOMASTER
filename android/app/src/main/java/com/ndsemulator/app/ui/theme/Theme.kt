package com.ndsemulator.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Nintendo DS-inspired colors
val NDSBlue = Color(0xFF1E3A8A)
val NDSDarkBlue = Color(0xFF1E3A5F)
val NDSLightBlue = Color(0xFF3B82F6)
val NDSAccent = Color(0xFFEF4444)
val NDSPurple = Color(0xFF7C3AED)
val NDSGreen = Color(0xFF22C55E)
val NDSYellow = Color(0xFFEAB308)

private val DarkColorScheme = darkColorScheme(
    primary = NDSLightBlue,
    onPrimary = Color.White,
    primaryContainer = NDSBlue,
    onPrimaryContainer = Color.White,
    secondary = NDSPurple,
    onSecondary = Color.White,
    tertiary = NDSGreen,
    onTertiary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    error = NDSAccent,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = NDSBlue,
    onPrimary = Color.White,
    primaryContainer = NDSLightBlue,
    onPrimaryContainer = Color.White,
    secondary = NDSPurple,
    onSecondary = Color.White,
    tertiary = NDSGreen,
    onTertiary = Color.White,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = NDSAccent,
    onError = Color.White
)

@Composable
fun NDSEmulatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
