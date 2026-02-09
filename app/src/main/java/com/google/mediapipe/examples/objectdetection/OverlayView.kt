/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mediapipe.examples.objectdetection

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.max
import kotlin.math.min

/**
 * A custom view that renders:
 * 1. Bounding boxes for detected objects.
 * 2. Classification labels and confidence scores.
 * 3. Pose landmarks (skeletons) if enabled.
 * 4. Motion masks (for debugging background subtraction).
 *
 * It handles coordinate mapping from the image space (normalized or pixel) to the view space.
 */
class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // Object detection
    private var results: ObjectDetectorResult? = null
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    private var bounds = Rect()

    // Pose landmarker (NEW)
    private var poseResults: PoseLandmarkerResult? = null
    private var posePointPaint = Paint()
    private var poseLinePaint = Paint()

    // Shared geometry
    private var scaleFactor: Float = 1f
    private var outputWidth = 0
    private var outputHeight = 0
    private var outputRotate = 0
    private var offsetX: Float = 0f
    private var offsetY: Float = 0f
    private var runningMode: RunningMode = RunningMode.IMAGE

    // Background subtraction / status (your existing additions)
    private var motionMask: Bitmap? = null
    private var motionActive: Boolean = false
    private var personActive: Boolean = false
    private val maskPaint = Paint().apply { alpha = 60 } // semi-transparent
    private var poseEnabled: Boolean = true
    private val transformMatrix = Matrix()
    private val tmpPts = FloatArray(2)

    // Pose geometry (separate from detection)
    private var poseOutputWidth = 0
    private var poseOutputHeight = 0
    private var poseOutputRotate = 0
    private val poseTransformMatrix = Matrix()
    private val poseTmpPts = FloatArray(2)


    init {
        initPaints()
    }

    // -----------------------------
    // Public API (Object Detection)
    // -----------------------------
    fun clear() {
        results = null
        poseResults = null
        motionMask = null
        motionActive = false
        personActive = false

        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        posePointPaint.reset()
        poseLinePaint.reset()

        invalidate()
        initPaints()
    }

    fun setRunningMode(runningMode: RunningMode) {
        this.runningMode = runningMode
    }

    /**
     * Updates the object detection results to be drawn.
     * Triggers an invalidate() to redraw the view.
     *
     * @param detectionResults The raw results from MediaPipe ObjectDetector.
     * @param outputHeight The height of the input image.
     * @param outputWidth The width of the input image.
     * @param imageRotation The rotation applied to the input image.
     */
    fun setResults(
        detectionResults: ObjectDetectorResult,
        outputHeight: Int,
        outputWidth: Int,
        imageRotation: Int
    ) {
        results = detectionResults
        this.outputWidth = outputWidth
        this.outputHeight = outputHeight
        this.outputRotate = imageRotation

        recalcScaleFactor()
        updateTransformMatrix()
        invalidate()
    }

    fun setPoseEnabled(enabled: Boolean) {
        poseEnabled = enabled
        if (!enabled) poseResults = null
        invalidate()
    }

    // -----------------------------
    // Public API (Pose Landmarker)
    // -----------------------------
    /**
     * Call this from your analyzer when you have a PoseLandmarkerResult
     * (or set to null to hide pose).
     *
     * IMPORTANT: pass the same output dims + rotation you pass into setResults()
     * so both overlays align to the same preview transform.
     */
    fun setPoseResults(
        poseLandmarkerResults: PoseLandmarkerResult?,
        outputHeight: Int,
        outputWidth: Int,
        imageRotation: Int
    ) {
        poseResults = poseLandmarkerResults

        if (outputWidth > 0 && outputHeight > 0) {
            poseOutputWidth = outputWidth
            poseOutputHeight = outputHeight
            poseOutputRotate = imageRotation
            updatePoseTransformMatrix()
        }

        invalidate()
    }

    private fun updatePoseTransformMatrix() {
        poseTransformMatrix.reset()
        poseTransformMatrix.postTranslate(-poseOutputWidth / 2f, -poseOutputHeight / 2f)
        poseTransformMatrix.postRotate(poseOutputRotate.toFloat())

        if (poseOutputRotate == 90 || poseOutputRotate == 270) {
            poseTransformMatrix.postTranslate(poseOutputHeight / 2f, poseOutputWidth / 2f)
        } else {
            poseTransformMatrix.postTranslate(poseOutputWidth / 2f, poseOutputHeight / 2f)
        }
    }

    // -----------------------------
    // Motion mask/status (existing)
    // -----------------------------
    fun setMotionMask(mask: Bitmap?, motionActive: Boolean) {
        this.motionMask = mask
        this.motionActive = motionActive
        invalidate()
    }

    fun setPersonActive(active: Boolean) {
        this.personActive = active
        invalidate()
    }

    // -----------------------------
    // Drawing
    // -----------------------------
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        // 1) Draw motion mask (stretched to overlay size)
        motionMask?.let { mask ->
            val dst = Rect(0, 0, width, height)
            canvas.drawBitmap(mask, null, dst, maskPaint)
        }

        // 2) Draw activation text (top-left)
//        val status = when {
//            personActive -> "PERSON"
//            motionActive -> "MOTION"
//            else -> ""
//        }

//            canvas.drawText("ACTIVE: $status", 20f, 60f, textPaint)
//        }

        // 3) Draw object boxes + labels
        drawObjectDetections(canvas)

        // 4) Draw pose skeleton
        drawPose(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalcScaleFactor()
        updateTransformMatrix()
        updatePoseTransformMatrix()
    }

    private fun drawObjectDetections(canvas: Canvas) {
        val detResult = results ?: return
        val detections = detResult.detections()
        if (detections.isEmpty()) return

        detections.forEach { det ->
            val categories = det.categories()
            if (categories.isNullOrEmpty()) return@forEach

            val best = categories.maxByOrNull { it.score() } ?: return@forEach
            if (!best.categoryName().equals("person", ignoreCase = true)) return@forEach  // ✅ ONLY PERSON

            val mappedRect = mapRectToView(det.boundingBox())
            canvas.drawRect(mappedRect, boxPaint)

            val drawableText = "person " + String.format("%.2f", best.score())
            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)

            val textWidth = bounds.width()
            val textHeight = bounds.height()

            canvas.drawRect(
                mappedRect.left,
                mappedRect.top,
                mappedRect.left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                mappedRect.top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )

            canvas.drawText(
                drawableText,
                mappedRect.left,
                mappedRect.top + textHeight,
                textPaint
            )
        }
    }

    private fun drawPose(canvas: Canvas) {
        if (!poseEnabled) return

        val pose = poseResults ?: return
        val people = pose.landmarks()
        if (people.isEmpty()) return

        val visThresh = 0.5f

        for (personLandmarks in people) {
            // Draw connections
            for (pair in POSE_CONNECTIONS) {
                val sIdx = pair[0]
                val eIdx = pair[1]
                if (sIdx >= personLandmarks.size || eIdx >= personLandmarks.size) continue

                val s = personLandmarks[sIdx]
                val e = personLandmarks[eIdx]

                // Some builds return Optional<Float>, keep this safe
                val sVis = s.visibility().orElse(1f)
                val sPre = s.presence().orElse(1f)
                val eVis = e.visibility().orElse(1f)
                val ePre = e.presence().orElse(1f)

                val sOk = sVis >= visThresh && sPre >= visThresh
                val eOk = eVis >= visThresh && ePre >= visThresh
                if (!sOk || !eOk) continue

                val p1 = mapPosePointToView(s.x() * poseOutputWidth, s.y() * poseOutputHeight)
                val p2 = mapPosePointToView(e.x() * poseOutputWidth, e.y() * poseOutputHeight)
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, poseLinePaint)
            }

            // Draw points
            for (lm in personLandmarks) {
                val v = lm.visibility().orElse(1f)
                val p = lm.presence().orElse(1f)
                if (v < visThresh || p < visThresh) continue

                val pt = mapPosePointToView(lm.x() * poseOutputWidth, lm.y() * poseOutputHeight)
                canvas.drawPoint(pt.x, pt.y, posePointPaint)
            }
        }
    }

    // -----------------------------
    // Geometry helpers
    // -----------------------------
    private fun recalcScaleFactor() {
        if (outputWidth <= 0 || outputHeight <= 0 || width <= 0 || height <= 0) return

        val rotated = when (outputRotate) {
            0, 180 -> Pair(outputWidth, outputHeight)
            90, 270 -> Pair(outputHeight, outputWidth)
            else -> Pair(outputWidth, outputHeight)
        }

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE, RunningMode.VIDEO -> min(
                width * 1f / rotated.first,
                height * 1f / rotated.second
            )
            RunningMode.LIVE_STREAM -> max(
                width * 1f / rotated.first,
                height * 1f / rotated.second
            )
        }

        // Center the scaled image inside the view (this is the missing piece)
        val scaledW = rotated.first * scaleFactor
        val scaledH = rotated.second * scaleFactor
        offsetX = (width - scaledW) / 2f
        offsetY = (height - scaledH) / 2f

        updateTransformMatrix()
    }

    /**
     * Maps an un-rotated rect in output-image coordinates into view coordinates,
     * applying rotation and then scaleFactor (same logic you already used).
     */
    private fun mapRectToView(src: RectF): RectF {
        val boxRect = RectF(src)

        val matrix = Matrix()
        matrix.postTranslate(-outputWidth / 2f, -outputHeight / 2f)
        matrix.postRotate(outputRotate.toFloat())

        if (outputRotate == 90 || outputRotate == 270) {
            matrix.postTranslate(outputHeight / 2f, outputWidth / 2f)
        } else {
            matrix.postTranslate(outputWidth / 2f, outputHeight / 2f)
        }

        matrix.mapRect(boxRect)

        // Scale to view
        // Scale to view + center offset
        boxRect.left = boxRect.left * scaleFactor + offsetX
        boxRect.top = boxRect.top * scaleFactor + offsetY
        boxRect.right = boxRect.right * scaleFactor + offsetX
        boxRect.bottom = boxRect.bottom * scaleFactor + offsetY
        return boxRect
    }

    private fun updateTransformMatrix() {
        transformMatrix.reset()
        transformMatrix.postTranslate(-outputWidth / 2f, -outputHeight / 2f)
        transformMatrix.postRotate(outputRotate.toFloat())
        if (outputRotate == 90 || outputRotate == 270) {
            transformMatrix.postTranslate(outputHeight / 2f, outputWidth / 2f)
        } else {
            transformMatrix.postTranslate(outputWidth / 2f, outputHeight / 2f)
        }
    }

    private fun mapPosePointToView(x: Float, y: Float): PointF {
        poseTmpPts[0] = x
        poseTmpPts[1] = y
        poseTransformMatrix.mapPoints(poseTmpPts)
        return PointF(poseTmpPts[0] * scaleFactor + offsetX, poseTmpPts[1] * scaleFactor + offsetY)
    }

    /**
     * Maps a point in output-image coordinates into view coordinates,
     * applying the same rotation transform and scaleFactor.
     */
    private fun mapPointToView(x: Float, y: Float): PointF {
        tmpPts[0] = x
        tmpPts[1] = y
        transformMatrix.mapPoints(tmpPts)
        return PointF(tmpPts[0] * scaleFactor + offsetX, tmpPts[1] * scaleFactor + offsetY)
    }

    // -----------------------------
    // Paint init
    // -----------------------------
    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        // Object detection box paint
        boxPaint.color = ContextCompat.getColor(context, R.color.mp_primary)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE

        // Pose paints (NEW)
        poseLinePaint.color = ContextCompat.getColor(context, R.color.mp_primary)
        poseLinePaint.strokeWidth = POSE_STROKE_WIDTH
        poseLinePaint.style = Paint.Style.STROKE

        posePointPaint.color = Color.YELLOW
        posePointPaint.strokeWidth = POSE_STROKE_WIDTH
        posePointPaint.style = Paint.Style.FILL
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
        private const val POSE_STROKE_WIDTH = 12F

        // Mediapipe Pose connections (landmark index pairs)
        private val POSE_CONNECTIONS: Array<IntArray> = arrayOf(
            intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(2, 3), intArrayOf(3, 7),
            intArrayOf(0, 4), intArrayOf(4, 5), intArrayOf(5, 6), intArrayOf(6, 8),
            intArrayOf(9, 10),

            intArrayOf(11, 12),
            intArrayOf(11, 13), intArrayOf(13, 15),
            intArrayOf(15, 17), intArrayOf(15, 19), intArrayOf(15, 21),
            intArrayOf(17, 19),

            intArrayOf(12, 14), intArrayOf(14, 16),
            intArrayOf(16, 18), intArrayOf(16, 20), intArrayOf(16, 22),
            intArrayOf(18, 20),

            intArrayOf(11, 23), intArrayOf(12, 24),
            intArrayOf(23, 24),

            intArrayOf(23, 25), intArrayOf(24, 26),
            intArrayOf(25, 27), intArrayOf(26, 28),
            intArrayOf(27, 29), intArrayOf(28, 30),
            intArrayOf(29, 31), intArrayOf(30, 32),
            intArrayOf(27, 31), intArrayOf(28, 32)
        )
    }
}
