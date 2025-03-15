package com.nshm.attendancesystem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CameraScreen(
    attendanceViewModel: AttendanceViewModel,
    navController: NavController
) {
    val message by attendanceViewModel::messageScan
    val color by attendanceViewModel::color
    val nameState by attendanceViewModel.name.collectAsState()
    var isScanning by remember { mutableStateOf(true) }
    val col = MaterialTheme.colorScheme.primary

    if (nameState.isNotEmpty()) {
        isScanning = false
        navController.navigate("${NavigationDestination.Authorized.name}/$nameState/$message/$color")
        attendanceViewModel.resetName()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CameraPreview(
            attendanceViewModel = attendanceViewModel,
            onScanComplete = {
                isScanning = false
            }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val rectWidth = 280.dp.toPx()
            val squareSide = 280.dp.toPx()
            val left = (size.width - rectWidth) / 2
            val top = (size.height - squareSide) / 2

            drawRect(
                color = Color.Black.copy(alpha = 0.6f), size = size
            )

            drawRect(
                color = Color.Transparent,
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(rectWidth, squareSide),
                blendMode = BlendMode.Clear
            )

            val cornerSize = 30.dp.toPx()
            val strokeWidth = 5.dp.toPx()

            drawLine(
                color = col,
                start = Offset(left, top),
                end = Offset(left + cornerSize, top),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = col,
                start = Offset(left, top),
                end = Offset(left, top + cornerSize),
                strokeWidth = strokeWidth
            )

            drawLine(
                color = col,
                start = Offset(left + rectWidth - cornerSize, top),
                end = Offset(left + rectWidth, top),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = col,
                start = Offset(left + rectWidth, top),
                end = Offset(left + rectWidth, top + cornerSize),
                strokeWidth = strokeWidth
            )

            drawLine(
                color = col,
                start = Offset(left, top + squareSide),
                end = Offset(left + cornerSize, top + squareSide),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = col,
                start = Offset(left, top + squareSide - cornerSize),
                end = Offset(left, top + squareSide),
                strokeWidth = strokeWidth
            )

            this.drawLine(
                color = col,
                start = Offset(left + rectWidth - cornerSize, top + squareSide),
                end = Offset(left + rectWidth, top + squareSide),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = col,
                start = Offset(left + rectWidth, top + squareSide - cornerSize),
                end = Offset(left + rectWidth, top + squareSide),
                strokeWidth = strokeWidth
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 36.dp)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Position the QR to Scan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        AnimatedVisibility(
            visible = !isScanning, enter = fadeIn(), exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Processing...", style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}