package com.ndsemulator.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp

/**
 * Touch controls overlay for the emulator.
 */
@Composable
fun TouchControls(
    onDpadUp: () -> Unit,
    onDpadDown: () -> Unit,
    onDpadLeft: () -> Unit,
    onDpadRight: () -> Unit,
    onDpadUpRelease: () -> Unit,
    onDpadDownRelease: () -> Unit,
    onDpadLeftRelease: () -> Unit,
    onDpadRightRelease: () -> Unit,
    onButtonA: () -> Unit,
    onButtonB: () -> Unit,
    onButtonARelease: () -> Unit,
    onButtonBRelease: () -> Unit,
    onButtonStart: () -> Unit,
    onButtonSelect: () -> Unit,
    onButtonStartRelease: () -> Unit,
    onButtonSelectRelease: () -> Unit,
    modifier: Modifier = Modifier,
    opacity: Float = 0.7f
) {
    Row(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // D-Pad on the left
        DPad(
            onUp = onDpadUp,
            onDown = onDpadDown,
            onLeft = onDpadLeft,
            onRight = onDpadRight,
            onUpRelease = onDpadUpRelease,
            onDownRelease = onDpadDownRelease,
            onLeftRelease = onDpadLeftRelease,
            onRightRelease = onDpadRightRelease,
            modifier = Modifier.alpha(opacity)
        )
        
        // Action buttons on the right
        ActionButtons(
            onA = onButtonA,
            onB = onButtonB,
            onARelease = onButtonARelease,
            onBRelease = onButtonBRelease,
            onStart = onButtonStart,
            onSelect = onButtonSelect,
            onStartRelease = onButtonStartRelease,
            onSelectRelease = onButtonSelectRelease,
            modifier = Modifier.alpha(opacity)
        )
    }
}

@Composable
private fun DPad(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onUpRelease: () -> Unit,
    onDownRelease: () -> Unit,
    onLeftRelease: () -> Unit,
    onRightRelease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonSize = 48.dp
    
    Box(
        modifier = modifier.size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        // Up button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(buttonSize)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onUp()
                            tryAwaitRelease()
                            onUpRelease()
                        }
                    )
                }
        )
        
        // Down button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(buttonSize)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onDown()
                            tryAwaitRelease()
                            onDownRelease()
                        }
                    )
                }
        )
        
        // Left button
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(buttonSize)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onLeft()
                            tryAwaitRelease()
                            onLeftRelease()
                        }
                    )
                }
        )
        
        // Right button
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(buttonSize)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onRight()
                            tryAwaitRelease()
                            onRightRelease()
                        }
                    )
                }
        )
        
        // Center (D-pad base)
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.DarkGray.copy(alpha = 0.5f), CircleShape)
        )
    }
}

@Composable
private fun ActionButtons(
    onA: () -> Unit,
    onB: () -> Unit,
    onARelease: () -> Unit,
    onBRelease: () -> Unit,
    onStart: () -> Unit,
    onSelect: () -> Unit,
    onStartRelease: () -> Unit,
    onSelectRelease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // A and B buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // B button
            ButtonCircle(
                label = "B",
                onPress = onB,
                onRelease = onBRelease,
                color = Color(0xFFE53935)
            )
            
            // A button
            ButtonCircle(
                label = "A",
                onPress = onA,
                onRelease = onARelease,
                color = Color(0xFF43A047)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Start and Select buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Select button
            ButtonOval(
                label = "SELECT",
                onPress = onSelect,
                onRelease = onSelectRelease
            )
            
            // Start button
            ButtonOval(
                label = "START",
                onPress = onStart,
                onRelease = onStartRelease
            )
        }
    }
}

@Composable
private fun ButtonCircle(
    label: String,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    color: Color,
    size: Int = 56
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(color)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress()
                        tryAwaitRelease()
                        onRelease()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ButtonOval(
    label: String,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    width: Int = 60,
    height: Int = 30
) {
    Box(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress()
                        tryAwaitRelease()
                        onRelease()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
