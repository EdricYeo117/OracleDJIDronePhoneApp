package com.google.mediapipe.examples.poselandmarker

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy

object ImageProxyUtils {
    fun toBitmap(imageProxy: ImageProxy, mirror: Boolean): Bitmap {
        // NOTE: Your project already uses OUTPUT_IMAGE_FORMAT_RGBA_8888 in analyzers,
        // so planes[0] contains RGBA bytes.
        val plane = imageProxy.planes[0]
        val buffer = plane.buffer
        buffer.rewind()

        val width = imageProxy.width
        val height = imageProxy.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)

        val rotated = rotate(bitmap, imageProxy.imageInfo.rotationDegrees.toFloat(), mirror)
        if (rotated != bitmap) bitmap.recycle()
        return rotated
    }

    private fun rotate(src: Bitmap, degrees: Float, mirror: Boolean): Bitmap {
        val m = Matrix()
        if (mirror) m.postScale(-1f, 1f, src.width / 2f, src.height / 2f)
        m.postRotate(degrees)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, m, true)
    }
}
