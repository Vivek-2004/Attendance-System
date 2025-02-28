package com.nshm.attendancesystem

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    attendanceViewModel: AttendanceViewModel = viewModel(),
    navController: NavController
) {
    val message by attendanceViewModel::messageScan
    val color by attendanceViewModel::color
    val nameState by attendanceViewModel.name.collectAsState()
    var showAuthorizedScreen by remember { mutableStateOf(false) }
    var name = nameState
    var isScanning by remember { mutableStateOf(true) }
    val col = MaterialTheme.colorScheme.primary

    LaunchedEffect(name) {
        if (name.isNotEmpty()) {
            showAuthorizedScreen = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (showAuthorizedScreen) {
            AuthorizedScreen(
                name = name,
                message = message,
                color = color,
                navController = navController
            )
            name = ""
        } else {
            CameraPreview(
                onScanComplete = {
                    isScanning = false
                }
            )

            // QR scanning overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val rectWidth = 280.dp.toPx()
                val squareSide = 280.dp.toPx()
                val left = (size.width - rectWidth) / 2
                val top = (size.height - squareSide) / 2

                drawRect(
                    color = Color.Black.copy(alpha = 0.6f),
                    size = size
                )

                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(rectWidth, squareSide),
                    blendMode = BlendMode.Clear
                )

                val cornerSize = 30.dp.toPx()
                val strokeWidth = 5.dp.toPx()


                // Top-left corner
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

                // Top-right corner
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

                // Bottom-left corner
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

                // Bottom-right corner
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

            // Instructions card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                        "Position the QR code inside the frame",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "The scan will happen automatically",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Loading indicator when processing scan
            AnimatedVisibility(
                visible = !isScanning,
                enter = fadeIn(),
                exit = fadeOut()
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
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                "Processing...",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    attendanceViewModel: AttendanceViewModel = viewModel(),
    onScanComplete: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scanner = BarcodeScanning.getClient()
    val executor = remember { Executors.newSingleThreadExecutor() }
    var scannedText by remember { mutableStateOf("") }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        LaunchedEffect(scannedText) {
            if (scannedText.isNotEmpty()) {
                onScanComplete()
                attendanceViewModel.fetchScan(scannedText)
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                val previewView = PreviewView(it)
                val preview = Preview.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_16_9) // Adjust the aspect ratio
                    .build()

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(executor) { imageProxy ->
                                processImageProxy(scanner, imageProxy) { barcode ->
                                    scannedText = barcode
                                }
                            }
                        }
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                        preview.surfaceProvider = previewView.surfaceProvider
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(it))
                previewView
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                            .padding(8.dp),
                        painter = painterResource(R.drawable.filter),
                        contentDescription = "Permission Required",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Camera permission is required",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Please grant camera permission to use the scanner",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
        Toast.makeText(
            context,
            "Camera permission is required to use this feature.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    scanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onBarcodeDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_TEXT -> onBarcodeDetected(barcode.displayValue ?: "No Text")
                        Barcode.TYPE_URL -> onBarcodeDetected(barcode.url?.url ?: "No URL")
                        Barcode.TYPE_CONTACT_INFO -> onBarcodeDetected(
                            barcode.contactInfo?.title ?: "No Contact Info"
                        )

                        else -> onBarcodeDetected("Unknown Type")
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}