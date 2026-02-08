package com.google.mediapipe.examples.objectdetection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.renderscript.Type
import androidx.camera.core.ImageProxy

/**
 * Helper class to convert ImageProxy YUV_420_888 buffers to RGBA Bitmaps
 * using RenderScript for hardware acceleration.
 */
class YuvToRgbConverter(context: Context) {
    private val rs: RenderScript = RenderScript.create(context)
    private val script: ScriptIntrinsicYuvToRGB =
        ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))

    private var yuvBuffer: ByteArray? = null
    private var inAlloc: Allocation? = null
    private var outAlloc: Allocation? = null

    fun yuvToRgb(image: ImageProxy, output: Bitmap) {
        require(image.format == ImageFormat.YUV_420_888) {
            "Expected YUV_420_888 but got ${image.format}"
        }

        val nv21 = yuvBuffer ?: ByteArray(nv21Size(image)).also { yuvBuffer = it }
        imageToNv21(image, nv21)

        val inType = Type.Builder(rs, Element.U8(rs)).setX(nv21.size)
        if (inAlloc == null || inAlloc!!.type.x != nv21.size) {
            inAlloc = Allocation.createTyped(rs, inType.create(), Allocation.USAGE_SCRIPT)
        }

        val outType = Type.Builder(rs, Element.RGBA_8888(rs))
            .setX(output.width).setY(output.height)
        if (outAlloc == null || outAlloc!!.type.x != output.width || outAlloc!!.type.y != output.height) {
            outAlloc = Allocation.createTyped(rs, outType.create(), Allocation.USAGE_SCRIPT)
        }

        inAlloc!!.copyFrom(nv21)
        script.setInput(inAlloc)
        script.forEach(outAlloc)
        outAlloc!!.copyTo(output)
    }

    private fun nv21Size(image: ImageProxy): Int = image.width * image.height * 3 / 2

    private fun imageToNv21(image: ImageProxy, out: ByteArray) {
        val width = image.width
        val height = image.height

        val yPlane = image.planes[0]
        val uPlane = image.planes[1]
        val vPlane = image.planes[2]

        val yBuf = yPlane.buffer
        val uBuf = uPlane.buffer
        val vBuf = vPlane.buffer

        val yRowStride = yPlane.rowStride
        val yPixelStride = yPlane.pixelStride

        val uRowStride = uPlane.rowStride
        val uPixelStride = uPlane.pixelStride

        // Copy Y
        var outIndex = 0
        for (row in 0 until height) {
            val rowStart = row * yRowStride
            for (col in 0 until width) {
                out[outIndex++] = yBuf.get(rowStart + col * yPixelStride)
            }
        }

        // Copy interleaved VU for NV21
        val chromaHeight = height / 2
        val chromaWidth = width / 2

        for (row in 0 until chromaHeight) {
            val rowStart = row * uRowStride
            for (col in 0 until chromaWidth) {
                val uvIndex = rowStart + col * uPixelStride
                out[outIndex++] = vBuf.get(uvIndex) // V
                out[outIndex++] = uBuf.get(uvIndex) // U
            }
        }
    }
}
