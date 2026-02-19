package com.google.mediapipe.examples.objectdetection.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.math.max

data class MotionDecision(
    val motionFrame: Boolean,
    val triggered: Boolean,
    val changedPixelRatio: Float,
    val consecutiveMotionFrames: Int,
    val maskBitmap: Bitmap?,   // downsampled ALPHA_8 mask (cleaned)
    val maskWidth: Int,
    val maskHeight: Int,
    val diffBitmap: Bitmap?    // downsampled ALPHA_8 diff visualization
)

/**
 * Drop-in MotionGate:
 * - More stringent default thresholds (tunable)
 * - Refined background update: updates primarily on unchanged pixels (less “self-canceling”)
 * - Border-safe majority filter
 * - Optional global mean-diff suppression for exposure changes
 *
 * Also includes DROP-IN display helpers at bottom:
 *  - alpha8MaskToArgbOverlay()
 *  - alpha8ToGrayArgb()
 *  - transformForPreview()
 *  - renderOverlayOnPreview()
 */
class MotionGate(
    // ----- TUNING (more stringent defaults) -----
    private val downsampleStep: Int = 6,        // 4..8
    private val alpha: Float = 0.04f,           // 0.02..0.08
    private val diffThreshold: Int = 36,        // 25..55
    private val ratioThreshold: Float = 0.04f,  // 0.02..0.10
    private val minConsecutive: Int = 3,
    private val cooldownMs: Long = 4_000L,
    private val globalChangeIgnoreRatio: Float = 0.50f, // 0.45..0.70
    private val warmupFrames: Int = 15,
    // Exposure-shift suppression: mean absolute diff across grid
    private val meanDiffIgnore: Float = 10f     // 8..15; set 0f to disable
) {
    private var bg: FloatArray? = null
    private var bgW = 0
    private var bgH = 0

    private var consecutive = 0
    private var lastTriggerMs = 0L
    private var frameCount = 0

    private fun readLuma(buffer: ByteBuffer, pos: Int, pixelStride: Int): Int {
        return if (pixelStride == 1) {
            buffer.get(pos).toInt() and 0xFF
        } else {
            // RGBA_8888: R,G,B,A
            val r = buffer.get(pos).toInt() and 0xFF
            val g = buffer.get(pos + 1).toInt() and 0xFF
            val b = buffer.get(pos + 2).toInt() and 0xFF
            ((0.299f * r) + (0.587f * g) + (0.114f * b)).toInt().coerceIn(0, 255)
        }
    }

    fun update(image: ImageProxy, nowMs: Long = System.currentTimeMillis()): MotionDecision {
        val yPlane = image.planes[0]
        val buffer = yPlane.buffer
        val rowStride = yPlane.rowStride
        val pixelStride = yPlane.pixelStride

        // Absolute gets rely on consistent buffer; rewind for safety.
        buffer.rewind()

        val width = image.width
        val height = image.height

        val step = max(1, downsampleStep)
        val dsW = (width + step - 1) / step
        val dsH = (height + step - 1) / step
        val total = dsW * dsH

        if (total <= 0) {
            consecutive = 0
            return MotionDecision(false, false, 0f, consecutive, null, 0, 0, null)
        }

        // (Re)initialize background model
        if (bg == null || bgW != dsW || bgH != dsH) {
            bgW = dsW
            bgH = dsH
            bg = FloatArray(total)

            var idx = 0
            for (y in 0 until height step step) {
                val rowBase = y * rowStride
                for (x in 0 until width step step) {
                    val pos = rowBase + x * pixelStride
                    bg!![idx++] = readLuma(buffer, pos, pixelStride).toFloat()
                }
            }

            consecutive = 0
            frameCount = 1

            val emptyMask = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            val emptyDiff = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            return MotionDecision(false, false, 0f, consecutive, emptyMask, dsW, dsH, emptyDiff)
        }

        val rawMask = ByteArray(total)
        val diffVis = ByteArray(total)

        var rawChanged = 0
        var sumDiff = 0
        var idx = 0

        // Make diff pop for humans (4..10)
        val diffGain = 6

        for (y in 0 until height step step) {
            val rowBase = y * rowStride
            for (x in 0 until width step step) {
                val pos = rowBase + x * pixelStride
                val cur = readLuma(buffer, pos, pixelStride)
                val prev = bg!![idx]

                val d = abs(cur - prev).toInt()
                sumDiff += d

                val dv = (d * diffGain).coerceAtMost(255)
                diffVis[idx] = dv.toByte()

                val isChanged = d >= diffThreshold
                if (isChanged) {
                    rawChanged++
                    rawMask[idx] = 0xFF.toByte()

                    // Refined: do NOT update background on changed pixels (or creep minimally)
                    bg!![idx] = prev + (alpha * 0.01f) * (cur - prev)
                } else {
                    rawMask[idx] = 0x00
                    bg!![idx] = prev + alpha * (cur - prev)
                }

                idx++
            }
        }

        frameCount++

        val rawRatio = rawChanged.toFloat() / total.toFloat()
        val meanDiff = sumDiff.toFloat() / total.toFloat()

        // Suppress global exposure changes (pixel-ratio heuristic)
        if (rawRatio >= globalChangeIgnoreRatio || (meanDiffIgnore > 0f && meanDiff >= meanDiffIgnore)) {
            consecutive = 0

            val maskBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            ByteBuffer.wrap(rawMask).also { it.rewind(); maskBmp.copyPixelsFromBuffer(it) }

            val diffBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            ByteBuffer.wrap(diffVis).also { it.rewind(); diffBmp.copyPixelsFromBuffer(it) }

            return MotionDecision(false, false, rawRatio, consecutive, maskBmp, dsW, dsH, diffBmp)
        }

        // --- CLEANUP: border-safe majority filter (3x3) ---
        // Start with rawMask so borders are preserved (no “black frame” artifacts).
        val cleaned = rawMask.clone()

        fun isOn(x: Int, y: Int): Boolean {
            if (x !in 0 until dsW || y !in 0 until dsH) return false
            return rawMask[y * dsW + x].toInt() != 0
        }

        for (yy in 1 until dsH - 1) {
            for (xx in 1 until dsW - 1) {
                var on = 0
                for (dy in -1..1) for (dx in -1..1) if (isOn(xx + dx, yy + dy)) on++
                cleaned[yy * dsW + xx] = if (on >= 5) 0xFF.toByte() else 0x00
            }
        }
        // --- END CLEANUP ---

        var cleanedChanged = 0
        for (i in 0 until total) if (cleaned[i].toInt() != 0) cleanedChanged++
        val ratio = cleanedChanged.toFloat() / total.toFloat()

        val maskBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
        ByteBuffer.wrap(cleaned).also { it.rewind(); maskBmp.copyPixelsFromBuffer(it) }

        val diffBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
        ByteBuffer.wrap(diffVis).also { it.rewind(); diffBmp.copyPixelsFromBuffer(it) }

        val motionFrame = ratio >= ratioThreshold
        consecutive = if (motionFrame) (consecutive + 1) else 0

        val warmedUp = frameCount > warmupFrames
        val canTrigger = (nowMs - lastTriggerMs) >= cooldownMs
        val triggered = warmedUp && canTrigger && consecutive >= minConsecutive

        if (triggered) lastTriggerMs = nowMs

        return MotionDecision(
            motionFrame = motionFrame,
            triggered = triggered,
            changedPixelRatio = ratio,
            consecutiveMotionFrames = consecutive,
            maskBitmap = maskBmp,
            maskWidth = dsW,
            maskHeight = dsH,
            diffBitmap = diffBmp
        )
    }
}

/* ======================================================================
   DROP-IN DISPLAY HELPERS (fix “displayed weirdly” on phone)
   - ALPHA_8 is not ideal to show directly. Convert to ARGB for overlay.
   - Apply rotationDegrees and mirroring to match CameraX preview.
   ====================================================================== */

/** Mask (ALPHA_8) -> white overlay (ARGB_8888) with transparent background. */
fun alpha8MaskToArgbOverlay(alpha8: Bitmap, onAlpha: Int = 160): Bitmap {
    val w = alpha8.width
    val h = alpha8.height
    val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    val buf = ByteBuffer.allocate(w * h)
    alpha8.copyPixelsToBuffer(buf)
    buf.rewind()

    val pixels = IntArray(w * h)
    for (i in 0 until w * h) {
        val v = buf.get().toInt() and 0xFF
        val a = if (v > 0) onAlpha else 0
        pixels[i] = (a shl 24) or (255 shl 16) or (255 shl 8) or 255
    }
    out.setPixels(pixels, 0, w, 0, 0, w, h)
    return out
}

/** Diff (ALPHA_8) -> grayscale ARGB_8888 for debugging. */
fun alpha8ToGrayArgb(alpha8: Bitmap, alpha: Int = 255): Bitmap {
    val w = alpha8.width
    val h = alpha8.height
    val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    val buf = ByteBuffer.allocate(w * h)
    alpha8.copyPixelsToBuffer(buf)
    buf.rewind()

    val pixels = IntArray(w * h)
    for (i in 0 until w * h) {
        val v = buf.get().toInt() and 0xFF
        pixels[i] = (alpha shl 24) or (v shl 16) or (v shl 8) or v
    }
    out.setPixels(pixels, 0, w, 0, 0, w, h)
    return out
}

/**
 * Rotate + mirror to match preview, then scale to preview size.
 *
 * rotationDegrees: image.imageInfo.rotationDegrees
 * mirrorX: true for front camera (if your preview is mirrored)
 */
fun transformForPreview(
    src: Bitmap,
    previewW: Int,
    previewH: Int,
    rotationDegrees: Int,
    mirrorX: Boolean,
    fitXY: Boolean = true
): Bitmap {
    val m = Matrix()

    // rotate around center
    m.postRotate(rotationDegrees.toFloat(), src.width / 2f, src.height / 2f)

    // mirror around center if needed
    if (mirrorX) {
        m.postScale(-1f, 1f, src.width / 2f, src.height / 2f)
    }

    val transformed = Bitmap.createBitmap(src, 0, 0, src.width, src.height, m, true)

    return if (fitXY) {
        // Debug-friendly: exact fit to preview.
        Bitmap.createScaledBitmap(transformed, previewW, previewH, false)
    } else {
        // Aspect-preserving: center-fit inside preview.
        val scale = minOf(previewW.toFloat() / transformed.width, previewH.toFloat() / transformed.height)
        val outW = (transformed.width * scale).toInt().coerceAtLeast(1)
        val outH = (transformed.height * scale).toInt().coerceAtLeast(1)
        Bitmap.createScaledBitmap(transformed, outW, outH, false)
    }
}

/**
 * If you have a preview-sized bitmap (e.g., what you're drawing to), render overlay on top.
 */
fun renderOverlayOnPreview(previewBitmap: Bitmap, overlayBitmap: Bitmap): Bitmap {
    val out = previewBitmap.copy(Bitmap.Config.ARGB_8888, true)
    val c = Canvas(out)
    val p = Paint(Paint.ANTI_ALIAS_FLAG)
    c.drawBitmap(overlayBitmap, 0f, 0f, p)
    return out
}

/* ======================================================================
   EXAMPLE USAGE (drop-in pattern)
   ====================================================================== */
/*
val decision = motionGate.update(imageProxy)
val rot = imageProxy.imageInfo.rotationDegrees
val mirrorX = (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) // adjust to your app

val previewW = previewView.width
val previewH = previewView.height

val maskOverlay = decision.maskBitmap?.let { alpha8MaskToArgbOverlay(it, onAlpha = 160) }?.let {
    transformForPreview(it, previewW, previewH, rot, mirrorX, fitXY = true)
}

val diffGray = decision.diffBitmap?.let { alpha8ToGrayArgb(it) }?.let {
    transformForPreview(it, previewW, previewH, rot, mirrorX, fitXY = true)
}

// Then display via ImageView or overlay canvas:
// imageViewMask.setImageBitmap(maskOverlay)
// imageViewDiff.setImageBitmap(diffGray)
*/
