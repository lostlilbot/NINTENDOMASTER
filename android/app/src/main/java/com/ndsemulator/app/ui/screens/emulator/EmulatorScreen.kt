package com.ndsemulator.app.ui.screens.emulator

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ndsemulator.app.ui.components.TouchControls
import kotlin.math.roundToInt

/**
 * Emulator screen for playing games.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulatorScreen(
    gameId: Long,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: EmulatorViewModel = hiltViewModel()
) {
    val game by viewModel.game.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val isFastForward by viewModel.isFastForward.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val screenLayout by viewModel.screenLayout.collectAsState()
    
    var screenWidth by remember { mutableStateOf(0f) }
    var screenHeight by remember { mutableStateOf(0f) }
    
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    DisposableEffect(Unit) {
        viewModel.loadGame(gameId)
        onDispose {
            viewModel.pauseEmulation()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(game?.title ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopEmulation()
                        onBackClick()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFastForward() }) {
                        Icon(
                            Icons.Default.FastForward,
                            contentDescription = "Fast Forward",
                            tint = if (isFastForward) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { viewModel.showSaveStateDialog.value = true }) {
                        Icon(Icons.Default.Save, contentDescription = "Save State")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ) {
            // Game screen area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                viewModel.handleTouchScreenTap(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            }
                        )
                    }
            ) {
                // Emulator screen placeholder - would be replaced with actual GL surface
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isRunning) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tap play to start",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            IconButton(
                                onClick = { viewModel.startEmulation() },
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }
                }
                
                // Control overlay
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isPaused) viewModel.resumeEmulation() 
                            else viewModel.pauseEmulation()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "Resume" else "Pause",
                            tint = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    IconButton(
                        onClick = { viewModel.stopEmulation() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = Color.White
                        )
                    }
                }
            }
            
            // Touch controls area
            if (settings.touchControlsEnabled && settings.showTouchButtons && isRunning) {
                TouchControls(
                    onDpadUp = { viewModel.handleInput("UP", true) },
                    onDpadDown = { viewModel.handleInput("DOWN", true) },
                    onDpadLeft = { viewModel.handleInput("LEFT", true) },
                    onDpadRight = { viewModel.handleInput("RIGHT", true) },
                    onDpadUpRelease = { viewModel.handleInput("UP", false) },
                    onDpadDownRelease = { viewModel.handleInput("DOWN", false) },
                    onDpadLeftRelease = { viewModel.handleInput("LEFT", false) },
                    onDpadRightRelease = { viewModel.handleInput("RIGHT", false) },
                    onButtonA = { viewModel.handleInput("A", true) },
                    onButtonB = { viewModel.handleInput("B", true) },
                    onButtonARelease = { viewModel.handleInput("A", false) },
                    onButtonBRelease = { viewModel.handleInput("B", false) },
                    onButtonStart = { viewModel.handleInput("START", true) },
                    onButtonSelect = { viewModel.handleInput("SELECT", true) },
                    onButtonStartRelease = { viewModel.handleInput("START", false) },
                    onButtonSelectRelease = { viewModel.handleInput("SELECT", false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}
