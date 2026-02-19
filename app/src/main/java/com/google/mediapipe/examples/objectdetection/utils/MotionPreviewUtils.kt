package com.google.mediapipe.examples.objectdetection.fragments

import android.graphics.Bitmap
import java.nio.ByteBuffer

object MotionPreviewUtils {

    /** ALPHA_8 mask -> fully opaque BLACK/WHITE ARGB_8888 */
    @JvmStatic
    fun alpha8MaskToOpaqueBW(maskAlpha8: Bitmap): Bitmap {
        val w = maskAlpha8.width
        val h = maskAlpha8.height

        val buf = ByteBuffer.allocate(w * h)
        maskAlpha8.copyPixelsToBuffer(buf)
        buf.rewind()

        val pixels = IntArray(w * h)
        for (i in 0 until w * h) {
            val v = buf.get().toInt() and 0xFF
            pixels[i] = if (v > 0) 0xFFFFFFFF.toInt() else 0xFF000000.toInt()
        }
        return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888)
    }

    /** ALPHA_8 diff -> fully opaque BLACK/WHITE ARGB_8888 (thresholded) */
    @JvmStatic
    fun alpha8DiffToOpaqueBW(diffAlpha8: Bitmap, threshold: Int = 40): Bitmap {
        val w = diffAlpha8.width
        val h = diffAlpha8.height

        val buf = ByteBuffer.allocate(w * h)
        diffAlpha8.copyPixelsToBuffer(buf)
        buf.rewind()

        val pixels = IntArray(w * h)
        for (i in 0 until w * h) {
            val v = buf.get().toInt() and 0xFF
            pixels[i] = if (v >= threshold) 0xFFFFFFFF.toInt() else 0xFF000000.toInt()
        }
        return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888)
    }
}
