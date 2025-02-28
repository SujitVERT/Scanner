package com.example.docscan.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docscan.detection.EdgeDetector
import com.example.docscan.detection.ImageProcessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.opencv.core.Point
import javax.inject.Inject

//@HiltViewModel
//class CameraViewModel @Inject constructor(
//    private val edgeDetector: EdgeDetector,
//    private val imageProcessor: ImageProcessor
//) : ViewModel() {
//
//    private val _edges = MutableStateFlow<List<Point>?>(null)
//    val edges: StateFlow<List<Point>?> = _edges.asStateFlow()
//
//    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
//    val capturedImage: StateFlow<Bitmap?> = _capturedImage.asStateFlow()
//
//    private val _isProcessing = MutableStateFlow(false)
//    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()
//
//    private var stableDetectionCount = 0
//    private val detectionThreshold = 1
//    private var isCapturing = false
//
//    fun processFrame(imageProxy: ImageProxy) {
//        if (isCapturing || _isProcessing.value) {
//            imageProxy.close()
//            return
//        }
//
//        val bitmap = imageProxy.toBitmap()
//        val detectedEdges = edgeDetector.detectEdges(bitmap)
//
//        if (detectedEdges != null && detectedEdges.size == 4) {
//            _edges.value = detectedEdges
//            stableDetectionCount++
//
//            if (stableDetectionCount >= detectionThreshold) {
//                isCapturing = true
//                _isProcessing.value = true // ✅ Prevent UI flickering by keeping camera active
//                autoCaptureImage(bitmap, detectedEdges)
//            }
//        } else {
//            stableDetectionCount = 0
//        }
//
//        imageProxy.close()
//    }
//
//    private fun autoCaptureImage(image: Bitmap, edges: List<Point>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            delay(300) // ✅ Add slight delay to smooth transition
//            val processedImage = imageProcessor.correctPerspective(image, edges)
//            _capturedImage.value = processedImage
//            _isProcessing.value = false
//            Log.d("CameraViewModel", "Image Auto-Captured!")
//        }
//    }
//
//    fun resetCapture() {
//        _capturedImage.value = null
//        stableDetectionCount = 0
//        isCapturing = false
//    }
//}

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val edgeDetector: EdgeDetector,
    private val imageProcessor: ImageProcessor
) : ViewModel() {

    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage: StateFlow<Bitmap?> = _capturedImage.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private var stableDetectionCount = 0
    private val detectionThreshold = 3
    private var isCapturing = false

    fun processFrame(imageProxy: ImageProxy) {
        if (isCapturing || _isProcessing.value) {
            imageProxy.close()
            return
        }

        val bitmap = imageProxy.toBitmap()
        val detectedEdges = edgeDetector.detectEdges(bitmap)

        if (detectedEdges != null) {
            stableDetectionCount++
            Log.d("Detection", "Stable Count: $stableDetectionCount")

            if (stableDetectionCount >= detectionThreshold) {
                Log.d("AutoCapture", "Capturing Image")
                isCapturing = true
                _isProcessing.value = true
                autoCaptureImage(bitmap, detectedEdges)
            }
        } else {
            stableDetectionCount = 0
        }

        imageProxy.close()
    }

    private fun autoCaptureImage(image: Bitmap, edges: List<Point>) {
        viewModelScope.launch(Dispatchers.IO) {
            val processedImage = imageProcessor.correctPerspective(image, edges)
            _capturedImage.value = processedImage
            _isProcessing.value = false
            Log.d("CameraViewModel", "✅ Image Auto-Captured & Cropped Successfully!")
        }
    }

    fun resetCapture() {
        _capturedImage.value = null
        stableDetectionCount = 0
        isCapturing = false
    }
}
