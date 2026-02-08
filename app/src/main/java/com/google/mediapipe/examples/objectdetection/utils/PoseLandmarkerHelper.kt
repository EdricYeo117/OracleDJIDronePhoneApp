package com.google.mediapipe.examples.objectdetection.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import java.util.concurrent.atomic.AtomicBoolean
import java.nio.ByteBuffer
import com.google.mediapipe.tasks.core.Delegate

class PoseLandmarkerHelper(
    private val context: Context,
    private val runningMode: RunningMode,
    private val modelAssetPath: String? = null,
    var minPoseDetectionConfidence: Float = DEFAULT_POSE_DETECTION_CONFIDENCE,
    var minPoseTrackingConfidence: Float = DEFAULT_POSE_TRACKING_CONFIDENCE,
    var minPosePresenceConfidence: Float = DEFAULT_POSE_PRESENCE_CONFIDENCE,
    var currentDelegate: Int = DELEGATE_CPU,
    var currentModel: Int = MODEL_POSE_LANDMARKER_FULL,
    private val poseLandmarkerHelperListener: LandmarkerListener? = null
) {

    /**
     * Convenience constructor that matches YOUR CameraFragment usage:
     * PoseLandmarkerHelper(requireContext(), modelAssetPath = "pose_landmarker_lite.task")
     *
     * For your pose-verification pipeline (timestamped frames), VIDEO mode is correct.
     */
    constructor(
        context: Context,
        modelAssetPath: String
    ) : this(
        context = context,
        runningMode = RunningMode.VIDEO,
        modelAssetPath = modelAssetPath
    )

    interface LandmarkerListener {
        fun onError(error: String, errorCode: Int)
        fun onResults(resultBundle: ResultBundle)
    }

    data class ResultBundle(
        val results: List<PoseLandmarkerResult>,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int
    )

    private var poseLandmarker: PoseLandmarker? = null
    private val closed = AtomicBoolean(false)

    fun isClose(): Boolean = closed.get()

    fun setupPoseLandmarker() {
        closed.set(false)

        val baseOptionsBuilder = BaseOptions.builder()

        // Delegate selection (CPU/GPU)
        when (currentDelegate) {
            DELEGATE_GPU -> baseOptionsBuilder.setDelegate(Delegate.GPU)
            else -> baseOptionsBuilder.setDelegate(Delegate.CPU)
        }

        // Model selection:
        // 1) explicit asset path (your ensurePoseHelper provides this)
        // 2) otherwise choose based on currentModel
        val modelAsset = modelAssetPath ?: when (currentModel) {
            MODEL_POSE_LANDMARKER_LITE -> "pose_landmarker_lite.task"
            MODEL_POSE_LANDMARKER_FULL -> "pose_landmarker_full.task"
            MODEL_POSE_LANDMARKER_HEAVY -> "pose_landmarker_heavy.task"
            else -> "pose_landmarker_full.task"
        }

        baseOptionsBuilder.setModelAssetPath(modelAsset)

        val optionsBuilder =
            PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptionsBuilder.build())
                .setMinPoseDetectionConfidence(minPoseDetectionConfidence)
                .setMinPosePresenceConfidence(minPosePresenceConfidence)
                .setMinTrackingConfidence(minPoseTrackingConfidence)
                .setRunningMode(runningMode)

        // Only LIVE_STREAM uses callbacks.
        if (runningMode == RunningMode.LIVE_STREAM) {
            optionsBuilder
                .setResultListener { result, input ->
                    poseLandmarkerHelperListener?.onResults(
                        ResultBundle(
                            results = listOf(result),
                            inferenceTime = 0L,
                            inputImageHeight = input.height,
                            inputImageWidth = input.width
                        )
                    )
                }
                .setErrorListener { e ->
                    poseLandmarkerHelperListener?.onError(
                        e.message ?: "Unknown error",
                        if (currentDelegate == DELEGATE_GPU) GPU_ERROR else OTHER_ERROR
                    )
                }
        }

        poseLandmarker = PoseLandmarker.createFromOptions(context, optionsBuilder.build())
    }

    fun clearPoseLandmarker() {
        closed.set(true)
        poseLandmarker?.close()
        poseLandmarker = null
    }

    // Alias so your CameraFragment's poseHelper?.close() compiles
    fun close() = clearPoseLandmarker()

    /**
     * Your CameraFragment calls:
     *   val poseResult = poseHelper?.detectVideo(mpImage, ts)
     *
     * This is VIDEO mode inference. Must call detectForVideo().
     */
    fun detectVideo(mpImage: MPImage, timestampMs: Long): PoseLandmarkerResult? {
        return try {
            ensureInit(requiredMode = RunningMode.VIDEO)
            poseLandmarker!!.detectForVideo(mpImage, timestampMs)
        } catch (e: Exception) {
            poseLandmarkerHelperListener?.onError(
                e.message ?: "detectVideo failed",
                if (currentDelegate == DELEGATE_GPU) GPU_ERROR else OTHER_ERROR
            )
            null
        }
    }

    fun detectImage(bitmap: Bitmap): ResultBundle? {
        return try {
            ensureInit(requiredMode = RunningMode.IMAGE)
            val mpImage: MPImage = BitmapImageBuilder(bitmap).build()
            val start = SystemClock.uptimeMillis()
            val result = poseLandmarker!!.detect(mpImage)
            val end = SystemClock.uptimeMillis()
            ResultBundle(
                results = listOf(result),
                inferenceTime = (end - start),
                inputImageHeight = bitmap.height,
                inputImageWidth = bitmap.width
            )
        } catch (e: Exception) {
            poseLandmarkerHelperListener?.onError(
                e.message ?: "detectImage failed",
                if (currentDelegate == DELEGATE_GPU) GPU_ERROR else OTHER_ERROR
            )
            null
        }
    }

    fun detectVideoFile(uri: Uri, intervalMs: Long): ResultBundle? {
        poseLandmarkerHelperListener?.onError(
            "detectVideoFile not implemented. (Not needed for your current pose-verification path.)",
            OTHER_ERROR
        )
        return null
    }

    fun detectLiveStream(imageProxy: ImageProxy, isFrontCamera: Boolean) {
        try {
            ensureInit(requiredMode = RunningMode.LIVE_STREAM)
            val bitmap = rgba8888ImageProxyToBitmap(imageProxy)
            val mpImage: MPImage = BitmapImageBuilder(bitmap).build()
            poseLandmarker!!.detectAsync(mpImage, SystemClock.uptimeMillis())
        } catch (e: Exception) {
            poseLandmarkerHelperListener?.onError(
                e.message ?: "detectLiveStream failed",
                if (currentDelegate == DELEGATE_GPU) GPU_ERROR else OTHER_ERROR
            )
        } finally {
            imageProxy.close()
        }
    }

    private fun ensureInit(requiredMode: RunningMode) {
        if (runningMode != requiredMode) {
            // Fail fast with a clear error instead of silent misbehavior.
            throw IllegalStateException("PoseLandmarkerHelper runningMode=$runningMode but requiredMode=$requiredMode")
        }
        if (poseLandmarker == null || closed.get()) setupPoseLandmarker()
    }

    /**
     * Converts RGBA_8888 ImageProxy to Bitmap.
     * IMPORTANT: CameraX analyzer must be configured with:
     *   .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
     */
    private fun rgba8888ImageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val width = imageProxy.width
        val height = imageProxy.height

        val plane = imageProxy.planes[0]
        val buffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride // should be 4 for RGBA

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        buffer.rewind()

        val expectedStride = width * pixelStride
        if (rowStride == expectedStride) {
            bitmap.copyPixelsFromBuffer(buffer)
            return bitmap
        }

        val rowBuffer = ByteArray(rowStride)
        val compact: ByteBuffer = ByteBuffer.allocateDirect(width * height * pixelStride)
        for (row in 0 until height) {
            buffer.get(rowBuffer, 0, rowStride)
            compact.put(rowBuffer, 0, expectedStride)
        }
        compact.rewind()
        bitmap.copyPixelsFromBuffer(compact)
        return bitmap
    }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1

        const val MODEL_POSE_LANDMARKER_LITE = 0
        const val MODEL_POSE_LANDMARKER_FULL = 1
        const val MODEL_POSE_LANDMARKER_HEAVY = 2

        const val DEFAULT_POSE_DETECTION_CONFIDENCE = 0.5f
        const val DEFAULT_POSE_TRACKING_CONFIDENCE = 0.5f
        const val DEFAULT_POSE_PRESENCE_CONFIDENCE = 0.5f

        const val GPU_ERROR = 1
        const val OTHER_ERROR = 2
    }
}
