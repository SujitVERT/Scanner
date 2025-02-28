package com.example.docscan.detection

import android.graphics.Bitmap
import com.example.docscan.toMat
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javax.inject.Inject
import javax.inject.Singleton
import org.opencv.android.Utils
import kotlin.math.max

@Singleton
class ImageProcessor @Inject constructor() {

    fun correctPerspective(image: Bitmap, corners: List<Point>): Bitmap {
        val sortedCorners = sortCorners(corners)

        val widthA = euclideanDistance(sortedCorners[0], sortedCorners[1])
        val widthB = euclideanDistance(sortedCorners[2], sortedCorners[3])
        val maxWidth = max(widthA, widthB).toInt()

        val heightA = euclideanDistance(sortedCorners[0], sortedCorners[3])
        val heightB = euclideanDistance(sortedCorners[1], sortedCorners[2])
        val maxHeight = max(heightA, heightB).toInt()

        val inputPoints = MatOfPoint2f(
            sortedCorners[0], sortedCorners[1],
            sortedCorners[2], sortedCorners[3]
        )

        val outputPoints = MatOfPoint2f(
            Point(0.0, 0.0),
            Point(maxWidth.toDouble(), 0.0),
            Point(maxWidth.toDouble(), maxHeight.toDouble()),
            Point(0.0, maxHeight.toDouble())
        )

        val perspectiveTransform = Imgproc.getPerspectiveTransform(inputPoints, outputPoints)
        val srcMat = image.toMat()
        val correctedMat = Mat()

        Imgproc.warpPerspective(srcMat, correctedMat, perspectiveTransform, Size(maxWidth.toDouble(), maxHeight.toDouble()))

        val resultBitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(correctedMat, resultBitmap)

        return resultBitmap
    }

    private fun sortCorners(corners: List<Point>): List<Point> {
        val sorted = corners.sortedBy { it.y }
        val top = sorted.take(2).sortedBy { it.x }
        val bottom = sorted.takeLast(2).sortedBy { it.x }

        return listOf(top[0], top[1], bottom[1], bottom[0])
    }

    private fun euclideanDistance(p1: Point, p2: Point): Double {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2.0) + Math.pow(p1.y - p2.y, 2.0))
    }
}