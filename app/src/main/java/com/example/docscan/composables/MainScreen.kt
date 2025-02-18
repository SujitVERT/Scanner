package com.example.docscan.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.docscan.viewModel.CameraViewModel
import org.opencv.core.Point

@Composable
fun MainScreen(viewModel: CameraViewModel) {
    val edges by viewModel.edges.collectAsState()
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
                BoundingBoxOverlay(edges)
            } else {
                CapturedImageScreen(viewModel) {
                    viewModel.resetCapture()
                    showCamera = false
                }
            }
        }
    }
}


@Composable
fun BoundingBoxOverlay(edges: List<Point>?) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        edges?.let { points ->
            if (points.size == 4) {
                for (i in 0..3) {
                    drawLine(
                        color = Color.Green,
                        start = Offset(points[i].x.toFloat(), points[i].y.toFloat()),
                        end = Offset(points[(i + 1) % 4].x.toFloat(), points[(i + 1) % 4].y.toFloat()),
                        strokeWidth = 3f
                    )
                }
            }
        }
    }
}

