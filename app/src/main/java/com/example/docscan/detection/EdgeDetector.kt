package com.example.docscan.detection

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EdgeDetector @Inject constructor() {

    fun detectEdges(image: Bitmap): List<Point>? {
        val mat = Mat()
        Utils.bitmapToMat(image, mat)

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)

        Imgproc.adaptiveThreshold(
            mat, mat, 255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2.0
        )

        Imgproc.GaussianBlur(mat, mat, Size(5.0, 5.0), 0.0)

        Imgproc.Canny(mat, mat, 50.0, 150.0)

        val contours: List<MatOfPoint> = ArrayList()
        val hierarchy = Mat()
        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

        var biggestContour: MatOfPoint? = null
        var maxArea = 0.0

        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > 5000) {
                val approx = MatOfPoint2f()
                val contour2f = MatOfPoint2f(*contour.toArray())

                Imgproc.approxPolyDP(contour2f, approx, 0.02 * Imgproc.arcLength(contour2f, true), true)

                if (approx.total() == 4L && area > maxArea) {
                    biggestContour = MatOfPoint(*approx.toArray())
                    maxArea = area
                }
            }
        }

        return biggestContour?.toList()
    }
}