package com.example.docscan.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.docscan.viewModel.CameraViewModel

@Composable
fun CameraScreen(
    viewModel: CameraViewModel
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "KYC Process"
            )

            Spacer(modifier = Modifier.height(100.dp))

            Box(
                modifier = Modifier.size(500.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
            ) {
                CameraPreviewScreen(
                    viewModel
                )
            }
        }
    }
}

@Preview
@Composable
fun CameraScreenPreview() {
//    CameraScreen()
}