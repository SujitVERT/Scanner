package com.example.docscan.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import com.example.docscan.viewModel.CameraViewModel

@Composable
fun CapturedImageScreen(viewModel: CameraViewModel, onRetake: () -> Unit) {
    val capturedImage by viewModel.capturedImage.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        capturedImage?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Captured ID Card")
        }

        Button(
            onClick = onRetake,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Text("Retake")
        }
    }
}
