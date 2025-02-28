package com.example.docscan.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.docscan.viewModel.CameraViewModel
import org.opencv.core.Point

@Composable
fun MainScreen(viewModel: CameraViewModel) {
    val capturedImage by viewModel.capturedImage.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()

    var showCamera by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!showCamera) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tap to Scan Your ID")
                Button(onClick = { showCamera = true }) {
                    Text("Open Camera")
                }
            }
        } else {
            if (capturedImage == null || isProcessing) {
                CameraPreviewScreen(viewModel)
            } else {
                CapturedImageScreen(viewModel) {
                    viewModel.resetCapture()
                    showCamera = false
                }
            }
        }
    }
}
