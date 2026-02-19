package com.google.mediapipe.examples.objectdetection.fragments

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

data class MotionDecisionV2(
    val motionFrame: Boolean,
    val triggered: Boolean,
    val changedPixelRatio: Float,
    val consecutiveMotionFrames: Int,
    val maskBitmap: Bitmap?,        // ALPHA_8 (downsampled)
    val previewBitmap: Bitmap?,     // ARGB_8888 (downsampled BW)
    val maskWidth: Int,
    val maskHeight: Int
)

class MotionGateV2(
    private val downsampleStep: Int = 6,          // 4..10 typical
    private val alphaMean: Float = 0.06f,         // bg mean update rate
    private val alphaVar: Float = 0.06f,          // bg variance update rate
    private val baseThreshold: Float = 12f,       // minimum threshold in luma
    private val kSigma: Float = 2.5f,             // adaptive = base + k*sigma
    private val ratioThreshold: Float = 0.015f,   // % foreground pixels to call "motion"
    private val minConsecutive: Int = 2,
    private val cooldownMs: Long = 2000L,
    private val warmupFrames: Int = 24,
    private val globalChangeIgnoreRatio: Float = 0.65f, // ignore huge exposure shift
    private val openIters: Int = 1,               // morphology open iterations
    private val closeIters: Int = 1,              // morphology close iterations
    private val minBlobArea: Int = 25             // remove tiny components in ds grid
) {
    private var mean: FloatArray? = null
    private var varr: FloatArray? = null
    private var dsW = 0
    private var dsH = 0

    private var consecutive = 0
    private var lastTriggerMs = 0L
    private var frameCount = 0

    // clamp variance to avoid division weirdness
    private val varMin = 9f     // sigma >= 3
    private val varMax = 2500f  // sigma <= 50

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

    fun update(image: ImageProxy, nowMs: Long = System.currentTimeMillis()): MotionDecisionV2 {
        val plane = image.planes[0]
        val buffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride
        buffer.rewind()

        val width = image.width
        val height = image.height

        val step = max(1, downsampleStep)
        val w = (width + step - 1) / step
        val h = (height + step - 1) / step
        val total = w * h
        if (total <= 0) {
            consecutive = 0
            return MotionDecisionV2(false, false, 0f, consecutive, null, null, 0, 0)
        }

        // init model
        if (mean == null || varr == null || w != dsW || h != dsH) {
            dsW = w
            dsH = h
            mean = FloatArray(total)
            varr = FloatArray(total)

            var idx = 0
            for (yy in 0 until height step step) {
                val rowBase = yy * rowStride
                for (xx in 0 until width step step) {
                    val pos = rowBase + xx * pixelStride
                    val cur = readLuma(buffer, pos, pixelStride).toFloat()
                    mean!![idx] = cur
                    varr!![idx] = 400f // initial variance (sigma ~20)
                    idx++
                }
            }

            frameCount = 1
            consecutive = 0

            val emptyMask = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ALPHA_8)
            val emptyPreview = Bitmap.createBitmap(dsW, dsH, Bitmap.Config.ARGB_8888)
            return MotionDecisionV2(false, false, 0f, consecutive, emptyMask, emptyPreview, dsW, dsH)
        }

        val raw = ByteArray(total)      // 0/255 mask
        var rawChanged = 0

        // foreground extraction
        var idx = 0
        for (yy in 0 until height step step) {
            val rowBase = yy * rowStride
            for (xx in 0 until width step step) {
                val pos = rowBase + xx * pixelStride
                val cur = readLuma(buffer, pos, pixelStride).toFloat()

                val m = mean!![idx]
                val v = varr!![idx]
                val sigma = sqrt(v)

                val thr = baseThreshold + kSigma * sigma
                val d = abs(cur - m)

                val isFg = d > thr
                if (isFg) {
                    raw[idx] = 0xFF.toByte()
                    rawChanged++
                    // update bg slowly on FG (so it doesn't absorb moving objects too fast)
                    val dm = cur - m
                    mean!![idx] = m + (alphaMean * 0.05f) * dm
                    val dv = (dm * dm) - v
                    varr!![idx] = (v + (alphaVar * 0.05f) * dv).coerceIn(varMin, varMax)
                } else {
                    raw[idx] = 0x00
                    // normal bg update
                    val dm = cur - m
                    mean!![idx] = m + alphaMean * dm
                    val dv = (dm * dm) - v
                    varr!![idx] = (v + alphaVar * dv).coerceIn(varMin, varMax)
                }

                idx++
            }
        }

        frameCount++

        val rawRatio = rawChanged.toFloat() / total.toFloat()

        // reject global changes (exposure shift)
        if (rawRatio >= globalChangeIgnoreRatio) {
            consecutive = 0
            val maskBmp = alpha8FromMask(raw, dsW, dsH)
            val prevBmp = bwPreviewFromMask(raw, dsW, dsH) // still show something for debugging
            return MotionDecisionV2(false, false, rawRatio, consecutive, maskBmp, prevBmp, dsW, dsH)
        }

        // morphology: open then close
        var work = raw
        repeat(openIters) {
            work = dilate(erode(work, dsW, dsH), dsW, dsH)
        }
        repeat(closeIters) {
            work = erode(dilate(work, dsW, dsH), dsW, dsH)
        }

        // remove small blobs
        if (minBlobArea > 1) {
            work = removeSmallComponents(work, dsW, dsH, minBlobArea)
        }

        // ratio from cleaned mask
        var cleanedChanged = 0
        for (i in 0 until total) if ((work[i].toInt() and 0xFF) != 0) cleanedChanged++
        val ratio = cleanedChanged.toFloat() / total.toFloat()

        val motionFrame = ratio >= ratioThreshold
        consecutive = if (motionFrame) consecutive + 1 else 0

        val warmedUp = frameCount > warmupFrames
        val canTrigger = (nowMs - lastTriggerMs) >= cooldownMs
        val triggered = warmedUp && canTrigger && consecutive >= minConsecutive
        if (triggered) lastTriggerMs = nowMs

        val maskBmp = alpha8FromMask(work, dsW, dsH)
        val prevBmp = bwPreviewFromMask(work, dsW, dsH)

        return MotionDecisionV2(
            motionFrame = motionFrame,
            triggered = triggered,
            changedPixelRatio = ratio,
            consecutiveMotionFrames = consecutive,
            maskBitmap = maskBmp,
            previewBitmap = prevBmp,
            maskWidth = dsW,
            maskHeight = dsH
        )
    }

    // --- helpers ---

    private fun alpha8FromMask(mask: ByteArray, w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)
        bmp.copyPixelsFromBuffer(ByteBuffer.wrap(mask))
        return bmp
    }

    /** OPAQUE BW preview like your example: white FG on black BG. */
    private fun bwPreviewFromMask(mask: ByteArray, w: Int, h: Int): Bitmap {
        val pixels = IntArray(w * h)
        for (i in pixels.indices) {
            val on = (mask[i].toInt() and 0xFF) != 0
            pixels[i] = if (on) 0xFFFFFFFF.toInt() else 0xFF000000.toInt()
        }
        return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888)
    }

    private fun erode(src: ByteArray, w: Int, h: Int): ByteArray {
        val out = ByteArray(w * h)
        for (y in 1 until h - 1) {
            val row = y * w
            for (x in 1 until w - 1) {
                val i = row + x
                var allOn = true
                for (dy in -1..1) {
                    val rr = (y + dy) * w
                    for (dx in -1..1) {
                        if ((src[rr + (x + dx)].toInt() and 0xFF) == 0) {
                            allOn = false
                            break
                        }
                    }
                    if (!allOn) break
                }
                out[i] = if (allOn) 0xFF.toByte() else 0x00
            }
        }
        return out
    }

    private fun dilate(src: ByteArray, w: Int, h: Int): ByteArray {
        val out = ByteArray(w * h)
        for (y in 1 until h - 1) {
            val row = y * w
            for (x in 1 until w - 1) {
                val i = row + x
                var anyOn = false
                for (dy in -1..1) {
                    val rr = (y + dy) * w
                    for (dx in -1..1) {
                        if ((src[rr + (x + dx)].toInt() and 0xFF) != 0) {
                            anyOn = true
                            break
                        }
                    }
                    if (anyOn) break
                }
                out[i] = if (anyOn) 0xFF.toByte() else 0x00
            }
        }
        return out
    }

    private fun removeSmallComponents(src: ByteArray, w: Int, h: Int, minArea: Int): ByteArray {
        val out = src.copyOf()
        val visited = BooleanArray(w * h)
        val q = IntArray(w * h)

        fun idx(x: Int, y: Int) = y * w + x

        for (y in 0 until h) {
            for (x in 0 until w) {
                val start = idx(x, y)
                if (visited[start]) continue
                if ((out[start].toInt() and 0xFF) == 0) {
                    visited[start] = true
                    continue
                }

                // BFS
                var head = 0
                var tail = 0
                q[tail++] = start
                visited[start] = true

                var area = 0
                while (head < tail) {
                    val p = q[head++]
                    area++

                    val px = p % w
                    val py = p / w

                    for (dy in -1..1) for (dx in -1..1) {
                        if (dx == 0 && dy == 0) continue
                        val nx = px + dx
                        val ny = py + dy
                        if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue
                        val ni = idx(nx, ny)
                        if (visited[ni]) continue
                        visited[ni] = true
                        if ((out[ni].toInt() and 0xFF) != 0) q[tail++] = ni
                    }
                }

                if (area < minArea) {
                    // clear this component
                    for (i in 0 until tail) {
                        val p = q[i]
                        out[p] = 0x00
                    }
                }
            }
        }
        return out
    }
}
