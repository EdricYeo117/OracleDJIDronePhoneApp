package com.google.mediapipe.examples.objectdetection.fragments;

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
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0014\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001BW\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u00a2\u0006\u0004\b\r\u0010\u000eJ\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0003X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/fragments/MotionGate;", "", "downsampleStep", "", "alpha", "", "diffThreshold", "ratioThreshold", "minConsecutive", "cooldownMs", "", "globalChangeIgnoreRatio", "warmupFrames", "<init>", "(IFIFIJFI)V", "bg", "", "bgW", "bgH", "consecutive", "lastTriggerMs", "frameCount", "bgUpdateMaxDiff", "update", "Lcom/google/mediapipe/examples/objectdetection/fragments/MotionDecision;", "image", "Landroidx/camera/core/ImageProxy;", "nowMs", "app_debug"})
public final class MotionGate {
    private final int downsampleStep = 0;
    private final float alpha = 0.0F;
    private final int diffThreshold = 0;
    private final float ratioThreshold = 0.0F;
    private final int minConsecutive = 0;
    private final long cooldownMs = 0L;
    private final float globalChangeIgnoreRatio = 0.0F;
    private final int warmupFrames = 0;
    @org.jetbrains.annotations.Nullable()
    private float[] bg;
    private int bgW = 0;
    private int bgH = 0;
    private int consecutive = 0;
    private long lastTriggerMs = 0L;
    private int frameCount = 0;
    private final int bgUpdateMaxDiff = 12;
    
    public MotionGate(int downsampleStep, float alpha, int diffThreshold, float ratioThreshold, int minConsecutive, long cooldownMs, float globalChangeIgnoreRatio, int warmupFrames) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.mediapipe.examples.objectdetection.fragments.MotionDecision update(@org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageProxy image, long nowMs) {
        return null;
    }
    
    public MotionGate() {
        super();
    }
}