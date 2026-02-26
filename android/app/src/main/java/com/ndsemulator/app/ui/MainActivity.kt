package com.ndsemulator.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ndsemulator.app.ui.navigation.NDSNavigation
import com.ndsemulator.app.ui.theme.NDSEmulatorTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for the NDS Emulator app.
 * Uses Jetpack Compose for UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            NDSEmulatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NDSNavigation()
                }
            }
        }
    }
}
