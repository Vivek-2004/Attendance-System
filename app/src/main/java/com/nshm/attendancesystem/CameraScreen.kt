package com.nshm.attendancesystem

import android.util.Size
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CameraScreen(attendanceViewModel: AttendanceViewModel = viewModel()) {

    val message by attendanceViewModel::messageScan
    val name by attendanceViewModel::name
    val clgId by attendanceViewModel::clgId

    Box(modifier = Modifier.fillMaxSize()) {

        CameraPreview()

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Name : $name",
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "College Id : $clgId",
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Status : $message",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Black.copy(alpha = 0.6f),
                    size = size
                )

                val rectWidth = 320.dp.toPx()
                val rectHeight = 150.dp.toPx()
                val left = (size.width - rectWidth) / 2
                val top = (size.height - rectHeight) / 2

                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight),
                    blendMode = BlendMode.Clear
                )

                val cornerSize = 30.dp.toPx()
                val strokeWidth = 5.dp.toPx()

                drawLine(
                    color = Color.Red,
                    start = Offset(left, top),
                    end = Offset(left + cornerSize, top),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = Color.Red,
                    start = Offset(left, top),
                    end = Offset(left, top + cornerSize),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = Color.Yellow,
                    start = Offset(left + rectWidth - cornerSize, top),
                    end = Offset(left + rectWidth, top),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = Color.Yellow,
                    start = Offset(left + rectWidth, top),
                    end = Offset(left + rectWidth, top + cornerSize),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = Color.Blue,
                    start = Offset(left, top + rectHeight),
                    end = Offset(left + cornerSize, top + rectHeight),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = Color.Blue,
                    start = Offset(left, top + rectHeight - cornerSize),
                    end = Offset(left, top + rectHeight),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = Color.Green,
                    start = Offset(left + rectWidth - cornerSize, top + rectHeight),
                    end = Offset(left + rectWidth, top + rectHeight),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(left + rectWidth, top + rectHeight - cornerSize),
                    end = Offset(left + rectWidth, top + rectHeight),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}

@Composable
fun CameraPreview(attendanceViewModel: AttendanceViewModel = viewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scanner = BarcodeScanning.getClient()
    val executor = remember { Executors.newSingleThreadExecutor() }
    var scannedText by remember { mutableStateOf("Scan") }
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
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
        if (scannedText.matches(Regex("\\d+"))) {
            if (scannedText.length == 11) {
                Toast.makeText(context, scannedText, Toast.LENGTH_SHORT).show()
                attendanceViewModel.fetchScan(scannedText)
            } else {
                Toast.makeText(context, "Invalid ID", Toast.LENGTH_SHORT).show()
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder()
                    .setTargetResolution(Size(1280, 720))
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
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))
                previewView
            }
        )
    } else {
        Toast.makeText(context, "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show()
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
                        Barcode.TYPE_CONTACT_INFO -> onBarcodeDetected(barcode.contactInfo?.title ?: "No Contact Info")
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