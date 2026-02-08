package com.google.mediapipe.examples.objectdetection.utils

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
