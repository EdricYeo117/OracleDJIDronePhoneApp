package com.google.mediapipe.examples.objectdetection.utils

/**
 * A sliding window filter for boolean values.
 *
 * Used to smooth out flickering detections.
 * @param windowSize The number of recent frames to consider.
 * @param minHits The minimum number of 'true' values required in the window to return 'true'.
 */
class BooleanWindowFilter(
    private val windowSize: Int,
    private val minHits: Int
) {
    private val window = ArrayDeque<Boolean>(windowSize)

    fun update(hit: Boolean): Boolean {
        if (window.size == windowSize) window.removeFirst()
        window.addLast(hit)
        return window.count { it } >= minHits
    }

    fun reset() = window.clear()
}
