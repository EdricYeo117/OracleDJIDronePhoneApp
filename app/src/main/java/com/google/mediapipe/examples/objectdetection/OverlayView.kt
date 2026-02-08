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
    private var runningMode: RunningMode = RunningMode.IMAGE

    // Background subtraction / status (your existing additions)
    private var motionMask: Bitmap? = null
    private var motionActive: Boolean = false
    private var personActive: Boolean = false
    private val maskPaint = Paint().apply { alpha = 120 } // semi-transparent

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
        this.outputWidth = outputWidth
        this.outputHeight = outputHeight
        this.outputRotate = imageRotation

        recalcScaleFactor()
        invalidate()
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
        val status = when {
            personActive -> "PERSON"
            motionActive -> "MOTION"
            else -> ""
        }
        if (status.isNotEmpty()) {
            canvas.drawText("ACTIVE: $status", 20f, 60f, textPaint)
        }

        // 3) Draw pose skeleton (NEW) — behind boxes or above? (currently behind boxes)
        drawPose(canvas)

        // 4) Draw object boxes + labels
        drawObjectDetections(canvas)
    }

    private fun drawObjectDetections(canvas: Canvas) {
        val detResult = results ?: return
        val detections = detResult.detections()
        if (detections.isEmpty()) return

        detections.forEachIndexed { index, det ->
            val mappedRect = mapRectToView(det.boundingBox())
            canvas.drawRect(mappedRect, boxPaint)

            val categories = det.categories()
            if (categories.isNullOrEmpty()) return@forEachIndexed

            val category = categories[0]
            val drawableText = category.categoryName() + " " + String.format("%.2f", category.score())

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
        val pose = poseResults ?: return
        val people = pose.landmarks()
        if (people.isEmpty()) return

        // Each "person" is a list of landmarks
        for (personLandmarks in people) {
            // Draw points
            for (lm in personLandmarks) {
                val p = mapPointToView(lm.x() * outputWidth, lm.y() * outputHeight)
                canvas.drawPoint(p.x, p.y, posePointPaint)
            }

            // Draw connections
            PoseLandmarker.POSE_LANDMARKS.forEach { connection ->
                if (connection == null) return@forEach
                val sIdx = connection.start()
                val eIdx = connection.end()
                if (sIdx >= personLandmarks.size || eIdx >= personLandmarks.size) return@forEach

                val s = personLandmarks[sIdx]
                val e = personLandmarks[eIdx]

                val p1 = mapPointToView(s.x() * outputWidth, s.y() * outputHeight)
                val p2 = mapPointToView(e.x() * outputWidth, e.y() * outputHeight)

                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, poseLinePaint)
            }
        }
    }

    // -----------------------------
    // Geometry helpers
    // -----------------------------
    private fun recalcScaleFactor() {
        if (outputWidth <= 0 || outputHeight <= 0 || width <= 0 || height <= 0) return

        val rotatedWidthHeight = when (outputRotate) {
            0, 180 -> Pair(outputWidth, outputHeight)
            90, 270 -> Pair(outputHeight, outputWidth)
            else -> Pair(outputWidth, outputHeight)
        }

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> min(
                width * 1f / rotatedWidthHeight.first,
                height * 1f / rotatedWidthHeight.second
            )

            RunningMode.LIVE_STREAM -> max(
                width * 1f / rotatedWidthHeight.first,
                height * 1f / rotatedWidthHeight.second
            )
        }
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
        boxRect.left *= scaleFactor
        boxRect.top *= scaleFactor
        boxRect.right *= scaleFactor
        boxRect.bottom *= scaleFactor

        return boxRect
    }

    /**
     * Maps a point in output-image coordinates into view coordinates,
     * applying the same rotation transform and scaleFactor.
     */
    private fun mapPointToView(x: Float, y: Float): PointF {
        val pts = floatArrayOf(x, y)

        val matrix = Matrix()
        matrix.postTranslate(-outputWidth / 2f, -outputHeight / 2f)
        matrix.postRotate(outputRotate.toFloat())

        if (outputRotate == 90 || outputRotate == 270) {
            matrix.postTranslate(outputHeight / 2f, outputWidth / 2f)
        } else {
            matrix.postTranslate(outputWidth / 2f, outputHeight / 2f)
        }

        matrix.mapPoints(pts)

        // Scale to view
        pts[0] *= scaleFactor
        pts[1] *= scaleFactor

        return PointF(pts[0], pts[1])
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
    }
}
