import androidx.camera.core.ImageProxy
import kotlin.math.abs
import kotlin.math.max

data class MotionDecision(
    val motionFrame: Boolean,
    val triggered: Boolean,
    val changedPixelRatio: Float,
    val consecutiveMotionFrames: Int
)

class MotionGate(
    private val downsampleStep: Int = 4,        // 2..8 typical. 4 is a good start.
    private val alpha: Float = 0.05f,           // EMA update rate (0.02..0.10)
    private val diffThreshold: Int = 18,        // pixel diff threshold (10..30)
    private val ratioThreshold: Float = 0.02f,  // % pixels changed (0.01..0.06)
    private val minConsecutive: Int = 3,        // motion frames required
    private val cooldownMs: Long = 10_000L,     // minimum time between triggers
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
        val pixelStride = yPlane.pixelStride // usually 1 for Y plane

        val width = image.width
        val height = image.height

        val step = max(1, downsampleStep)
        val dsW = width / step
        val dsH = height / step
        val total = dsW * dsH

        if (total <= 0) {
            consecutive = 0
            return MotionDecision(false, false, 0f, consecutive)
        }

        // (Re)initialize background if needed
        if (bg == null || bgW != dsW || bgH != dsH) {
            bgW = dsW
            bgH = dsH
            bg = FloatArray(total)
            // initialize bg from current frame
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
            return MotionDecision(false, false, 0f, consecutive)
        }

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
                if (d >= diffThreshold) changed++
                idx++
            }
        }

        frameCount++

        val ratio = changed.toFloat() / total.toFloat()

        // Suppress “whole frame changed” (often auto-exposure / lighting shift)
        if (ratio >= globalChangeIgnoreRatio) {
            consecutive = 0
            // Optionally: you can re-seed bg faster here if needed.
            return MotionDecision(motionFrame = false, triggered = false, changedPixelRatio = ratio, consecutiveMotionFrames = consecutive)
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
            consecutiveMotionFrames = consecutive
        )
    }
}
