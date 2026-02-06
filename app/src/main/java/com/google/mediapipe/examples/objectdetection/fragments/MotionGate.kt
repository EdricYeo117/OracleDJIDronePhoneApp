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
        val pixelStride = yPlane.pixelStride // for Y plane usually 1

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
            // Return an empty mask for the first frame (optional)
            val emptyMask = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            return MotionDecision(false, false, 0f, consecutive, emptyMask, dsW, dsH)
        }

        val mask = ByteArray(total)
        var changed = 0
        var idx = 0

        for (y in 0 until height step step) {
            val rowBase = y * rowStride
            for (x in 0 until width step step) {
                val pos = rowBase + x * pixelStride
                val cur = buffer.get(pos).toInt() and 0xFF

                val prev = bg!![idx]
                val updated = prev + alpha * (cur - prev)
                bg!![idx] = updated

                val d = abs(cur - updated).toInt()
                val isChanged = d >= diffThreshold

                if (isChanged) {
                    changed++
                    mask[idx] = 0xFF.toByte()
                } else {
                    mask[idx] = 0x00
                }

                idx++
            }
        }

        frameCount++

        val ratio = changed.toFloat() / total.toFloat()

// --- INSERT CLEANUP HERE ---
        val cleaned = ByteArray(total)
        for (yy in 1 until dsH - 1) {
            for (xx in 1 until dsW - 1) {
                var on = 0
                for (dy in -1..1) for (dx in -1..1) {
                    if (mask[(yy + dy) * dsW + (xx + dx)].toInt() != 0) on++
                }
                cleaned[yy * dsW + xx] = if (on >= 5) 0xFF.toByte() else 0x00
            }
        }
// --- END CLEANUP ---

// Build mask bitmap (downsampled) from CLEANED mask
        val maskBmp = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
        maskBmp.copyPixelsFromBuffer(ByteBuffer.wrap(cleaned))

        // Suppress global exposure changes
        if (ratio >= globalChangeIgnoreRatio) {
            consecutive = 0
            return MotionDecision(
                motionFrame = false,
                triggered = false,
                changedPixelRatio = ratio,
                consecutiveMotionFrames = consecutive,
                maskBitmap = maskBmp,
                maskWidth = dsW,
                maskHeight = dsH
            )
        }

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
