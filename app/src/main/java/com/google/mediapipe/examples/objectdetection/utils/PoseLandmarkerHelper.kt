package com.google.mediapipe.examples.objectdetection.utils

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseLandmarkerHelper(
    context: Context,
    modelAssetPath: String
) {
    private val landmarker: PoseLandmarker

    init {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath(modelAssetPath)
            .build()

        val options = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.VIDEO)
            .build()

        landmarker = PoseLandmarker.createFromOptions(context, options)
    }

    fun detectVideo(mpImage: MPImage, timestampMs: Long): PoseLandmarkerResult {
        // PoseLandmarkerResult exposes landmarks() in Java/Kotlin API
        return landmarker.detectForVideo(mpImage, timestampMs)
    }

    fun close() {
        landmarker.close()
    }
}
