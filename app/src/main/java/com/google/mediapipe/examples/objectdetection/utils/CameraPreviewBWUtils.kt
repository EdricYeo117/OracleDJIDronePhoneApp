package com.google.mediapipe.examples.objectdetection.fragments

import android.graphics.Bitmap
import android.graphics.Color
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import kotlin.math.max

object CameraPreviewBWUtils {

    /** Fast: RGBA_8888 ImageProxy -> ARGB_8888 bitmap (full resolution). */
    @JvmStatic
    fun rgbaProxyToBitmap(image: ImageProxy): Bitmap {
        val width = image.width
        val height = image.height
        val plane = image.planes[0]
        val buffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride // should be 4

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        buffer.rewind()

        val expectedStride = width * pixelStride
        if (rowStride == expectedStride) {
            bitmap.copyPixelsFromBuffer(buffer)
            return bitmap
        }

        val rowBuffer = ByteArray(rowStride)
        val compact = java.nio.ByteBuffer.allocateDirect(width * height * pixelStride)
        for (row in 0 until height) {
            buffer.get(rowBuffer, 0, rowStride)
            compact.put(rowBuffer, 0, expectedStride)
        }
        compact.rewind()
        bitmap.copyPixelsFromBuffer(compact)
        return bitmap
    }

    /** Convert ARGB bitmap to grayscale (opaque). */
    @JvmStatic
    fun toGrayscale(src: Bitmap): Bitmap {
        val w = src.width
        val h = src.height
        val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        val pixels = IntArray(w * h)
        src.getPixels(pixels, 0, w, 0, 0, w, h)

        for (i in pixels.indices) {
            val c = pixels[i]
            val r = (c shr 16) and 0xFF
            val g = (c shr 8) and 0xFF
            val b = c and 0xFF
            val y = (0.299f * r + 0.587f * g + 0.114f * b).toInt().coerceIn(0, 255)
            pixels[i] = (0xFF shl 24) or (y shl 16) or (y shl 8) or y
        }

        out.setPixels(pixels, 0, w, 0, 0, w, h)
        return out
    }

    /** Nearest-neighbor downscale for inset (fast). */
    @JvmStatic
    fun downscale(src: Bitmap, outW: Int, outH: Int): Bitmap {
        val w = max(1, outW)
        val h = max(1, outH)
        return Bitmap.createScaledBitmap(src, w, h, false)
    }
}
