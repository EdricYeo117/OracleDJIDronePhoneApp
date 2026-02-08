package com.google.mediapipe.examples.objectdetection.fragments

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.math.max

data class MotionDecision(
    val motionFrame: Boolean,
    val triggered: Boolean,
    val changedPixelRatio: Float,
    val consecutiveMotionFrames: Int,
    val maskBitmap: Bitmap?,   // downsampled ALPHA_8 mask
    val maskWidth: Int,
    val maskHeight: Int
)

/**
 * Implements a lightweight background subtraction algorithm to detect motion.
 *
 * Algorithm:
 * 1. **Downsampling**: Reduces processing load by working on a smaller grid.
 * 2. **EMA Background**: Maintains an Exponential Moving Average of the background.
 * 3. **Diffing**: Compares current frame to background.
 * 4. **Global Change Rejection**: Ignores frames where too many pixels change at once (lighting shifts).
 * 5. **Majority Filter**: Cleans up noise using a 3x3 neighborhood filter.
 *
 * @param downsampleStep Step size for downsampling (e.g., 6 means utilize every 6th pixel).
 * @param alpha The learning rate for the background model (0.0 - 1.0).
 * @param diffThreshold Pixel intensity difference to consider a pixel "changed".
 * @param ratioThreshold Fraction of pixels that must change to trigger "motion".
 * @param minConsecutive Number of consecutive motion frames required to trigger.
 * @param cooldownMs Minimum time between triggers.
 * @param globalChangeIgnoreRatio If changed pixels > this ratio, ignore the frame (global lighting change).
 * @param warmupFrames Number of initial frames to build the background model.
 */
class MotionGate(
    private val downsampleStep: Int = 6,        // 2..8 typical
    private val alpha: Float = 0.08f,           // EMA update rate (0.02..0.10)
    private val diffThreshold: Int = 28,        // luma diff threshold (10..30)
    private val ratioThreshold: Float = 0.03f,  // % pixels changed (0.01..0.06)
    private val minConsecutive: Int = 3,        // motion frames required
    private val cooldownMs: Long = 10_000L,     // min time between triggers
    private val globalChangeIgnoreRatio: Float = 0.60f, // ignore exposure shifts
    private val warmupFrames: Int = 15          // build background before triggering
) {
    private var bg: FloatArray? = null
    private var bgW = 0
    private var bgH = 0

    private var consecutive = 0
    private var lastTriggerMs = 0L
    private var frameCount = 0

    fun update(image: ImageProxy, nowMs: Long = System.currentTimeMillis()): MotionDecision {
        val yPlane = image.planes[0]
        val buffer = yPlane.buffer
        val rowStride = yPlane.rowStride
        val pixelStride = yPlane.pixelStride // Y plane usually 1

        // Safety: ensure absolute get(pos) reads a consistent backing buffer
        buffer.rewind()

        val width = image.width
        val height = image.height

        val step = max(1, downsampleStep)
        val dsW = (width + step - 1) / step
        val dsH = (height + step - 1) / step
        val total = dsW * dsH

        if (total <= 0) {
            consecutive = 0
            return MotionDecision(false, false, 0f, consecutive, null, 0, 0)
        }

        // (Re)initialize background model if needed
        if (bg == null || bgW != dsW || bgH != dsH) {
            bgW = dsW
            bgH = dsH
            bg = FloatArray(total)

            var idx = 0
            for (y in 0 until height step step) {
                val rowBase = y * rowStride
                for (x in 0 until width step step) {
                    val pos = rowBase + x * pixelStride
                    val yVal = buffer.get(pos).toInt() and 0xFF
                    bg!![idx++] = yVal.toFloat()
                }
            }

            consecutive = 0
            frameCount = 1
            val emptyMask = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            return MotionDecision(false, false, 0f, consecutive, emptyMask, dsW, dsH)
        }

        val rawMask = ByteArray(total)
        var rawChanged = 0
        var idx = 0

        // IMPORTANT FIX:
        // 1) compute diff against PREVIOUS bg value
        // 2) update bg only when pixel is NOT changed (prevents "foreground bleeding" into background)
        for (y in 0 until height step step) {
            val rowBase = y * rowStride
            for (x in 0 until width step step) {
                val pos = rowBase + x * pixelStride
                val cur = buffer.get(pos).toInt() and 0xFF

                val prev = bg!![idx]
                val d = abs(cur - prev).toInt()
                val isChanged = d >= diffThreshold

                if (isChanged) {
                    rawChanged++
                    rawMask[idx] = 0xFF.toByte()
                    // Optionally: do NOT update bg here
                } else {
                    rawMask[idx] = 0x00
                    // EMA update only on stable/background pixels
                    bg!![idx] = prev + alpha * (cur - prev)
                }

                idx++
            }
        }

        frameCount++

        val rawRatio = rawChanged.toFloat() / total.toFloat()

        // Suppress global exposure changes (use RAW ratio, before cleanup)
        if (rawRatio >= globalChangeIgnoreRatio) {
            consecutive = 0
            val maskBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            maskBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rawMask))
            return MotionDecision(false, false, rawRatio, consecutive, maskBmp, dsW, dsH)
        }

        // --- CLEANUP (majority filter) ---
        // Note: this affects how it LOOKS; it shouldn't affect the "global change ignore" check above.
        val cleaned = ByteArray(total)
        for (yy in 1 until dsH - 1) {
            val row = yy * dsW
            for (xx in 1 until dsW - 1) {
                var on = 0
                val base = row + xx
                // 3x3 neighborhood count
                for (dy in -1..1) {
                    val nRow = (yy + dy) * dsW
                    for (dx in -1..1) {
                        if (rawMask[nRow + (xx + dx)].toInt() != 0) on++
                    }
                }
                cleaned[base] = if (on >= 5) 0xFF.toByte() else 0x00
            }
        }
        // --- END CLEANUP ---

        // (Optional but recommended) compute ratio from CLEANED mask for more stable decisions
        var cleanedChanged = 0
        for (i in 0 until total) if (cleaned[i].toInt() != 0) cleanedChanged++
        val ratio = cleanedChanged.toFloat() / total.toFloat()

        // Build mask bitmap (downsampled)
        val maskBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
        maskBmp.copyPixelsFromBuffer(ByteBuffer.wrap(cleaned))

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
            maskHeight = dsH
        )
    }
}
