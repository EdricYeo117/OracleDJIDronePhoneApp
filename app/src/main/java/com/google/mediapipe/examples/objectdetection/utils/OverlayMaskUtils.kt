package com.google.mediapipe.examples.objectdetection.fragments

import android.graphics.Bitmap

object OverlayMaskUtils {

    /** Paints changed pixels as white on top of a grayscale ARGB bitmap. */
    @JvmStatic
    fun paintMaskWhiteOnGray(gray: Bitmap, maskA8: Bitmap): Bitmap {
        val w = gray.width
        val h = gray.height
        val out = gray.copy(Bitmap.Config.ARGB_8888, true)

        val g = IntArray(w * h)
        out.getPixels(g, 0, w, 0, 0, w, h)

        // Read mask ALPHA_8
        val mask = ByteArray(w * h)
        val buf = java.nio.ByteBuffer.allocate(w * h)
        maskA8.copyPixelsToBuffer(buf)
        buf.rewind()
        buf.get(mask)

        for (i in g.indices) {
            if ((mask[i].toInt() and 0xFF) > 0) {
                g[i] = 0xFFFFFFFF.toInt()
            }
        }

        out.setPixels(g, 0, w, 0, 0, w, h)
        return out
    }
}
