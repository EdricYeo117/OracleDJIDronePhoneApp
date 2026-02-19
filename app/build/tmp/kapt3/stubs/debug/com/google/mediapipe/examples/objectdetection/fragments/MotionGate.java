package com.google.mediapipe.examples.objectdetection.fragments;

/**
 * Drop-in MotionGate:
 * - More stringent default thresholds (tunable)
 * - Refined background update: updates primarily on unchanged pixels (less “self-canceling”)
 * - Border-safe majority filter
 * - Optional global mean-diff suppression for exposure changes
 *
 * Also includes DROP-IN display helpers at bottom:
 * - alpha8MaskToArgbOverlay()
 * - alpha8ToGrayArgb()
 * - transformForPreview()
 * - renderOverlayOnPreview()
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0014\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001Ba\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u00a2\u0006\u0004\b\u000e\u0010\u000fJ \u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0003H\u0002J\u0018\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/fragments/MotionGate;", "", "downsampleStep", "", "alpha", "", "diffThreshold", "ratioThreshold", "minConsecutive", "cooldownMs", "", "globalChangeIgnoreRatio", "warmupFrames", "meanDiffIgnore", "<init>", "(IFIFIJFIF)V", "bg", "", "bgW", "bgH", "consecutive", "lastTriggerMs", "frameCount", "readLuma", "buffer", "Ljava/nio/ByteBuffer;", "pos", "pixelStride", "update", "Lcom/google/mediapipe/examples/objectdetection/fragments/MotionDecision;", "image", "Landroidx/camera/core/ImageProxy;", "nowMs", "app_debug"})
public final class MotionGate {
    private final int downsampleStep = 0;
    private final float alpha = 0.0F;
    private final int diffThreshold = 0;
    private final float ratioThreshold = 0.0F;
    private final int minConsecutive = 0;
    private final long cooldownMs = 0L;
    private final float globalChangeIgnoreRatio = 0.0F;
    private final int warmupFrames = 0;
    private final float meanDiffIgnore = 0.0F;
    @org.jetbrains.annotations.Nullable()
    private float[] bg;
    private int bgW = 0;
    private int bgH = 0;
    private int consecutive = 0;
    private long lastTriggerMs = 0L;
    private int frameCount = 0;
    
    public MotionGate(int downsampleStep, float alpha, int diffThreshold, float ratioThreshold, int minConsecutive, long cooldownMs, float globalChangeIgnoreRatio, int warmupFrames, float meanDiffIgnore) {
        super();
    }
    
    private final int readLuma(java.nio.ByteBuffer buffer, int pos, int pixelStride) {
        return 0;
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