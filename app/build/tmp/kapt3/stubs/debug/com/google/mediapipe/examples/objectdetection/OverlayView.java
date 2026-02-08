package com.google.mediapipe.examples.objectdetection;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0011\u0018\u0000 C2\u00020\u0001:\u0001CB\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010&\u001a\u00020\'J\u0010\u0010(\u001a\u00020\'2\u0006\u0010)\u001a\u00020*H\u0016J\u0010\u0010+\u001a\u00020\'2\u0006\u0010)\u001a\u00020*H\u0002J\u0010\u0010,\u001a\u00020\'2\u0006\u0010)\u001a\u00020*H\u0002J\b\u0010-\u001a\u00020\'H\u0002J\u0018\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u001f2\u0006\u00101\u001a\u00020\u001fH\u0002J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u000203H\u0002J\b\u00105\u001a\u00020\'H\u0002J\u0018\u00106\u001a\u00020\'2\b\u00107\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\f\u001a\u00020\rJ\u000e\u00108\u001a\u00020\'2\u0006\u00109\u001a\u00020\rJ\u000e\u0010:\u001a\u00020\'2\u0006\u0010;\u001a\u00020\rJ(\u0010<\u001a\u00020\'2\b\u0010=\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010>\u001a\u00020\u0011J&\u0010?\u001a\u00020\'2\u0006\u0010@\u001a\u00020\u001b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010>\u001a\u00020\u0011J\u000e\u0010A\u001a\u00020\'2\u0006\u0010\u001c\u001a\u00020\u001dJ\b\u0010B\u001a\u00020\'H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006D"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/OverlayView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "bounds", "Landroid/graphics/Rect;", "boxPaint", "Landroid/graphics/Paint;", "maskPaint", "motionActive", "", "motionMask", "Landroid/graphics/Bitmap;", "outputHeight", "", "outputRotate", "outputWidth", "personActive", "poseEnabled", "poseLinePaint", "posePointPaint", "poseResults", "Lcom/google/mediapipe/tasks/vision/poselandmarker/PoseLandmarkerResult;", "results", "Lcom/google/mediapipe/tasks/vision/objectdetector/ObjectDetectorResult;", "runningMode", "Lcom/google/mediapipe/tasks/vision/core/RunningMode;", "scaleFactor", "", "textBackgroundPaint", "textPaint", "tmpPts", "", "transformMatrix", "Landroid/graphics/Matrix;", "clear", "", "draw", "canvas", "Landroid/graphics/Canvas;", "drawObjectDetections", "drawPose", "initPaints", "mapPointToView", "Landroid/graphics/PointF;", "x", "y", "mapRectToView", "Landroid/graphics/RectF;", "src", "recalcScaleFactor", "setMotionMask", "mask", "setPersonActive", "active", "setPoseEnabled", "enabled", "setPoseResults", "poseLandmarkerResults", "imageRotation", "setResults", "detectionResults", "setRunningMode", "updateTransformMatrix", "Companion", "app_debug"})
public final class OverlayView extends android.view.View {
    @org.jetbrains.annotations.Nullable()
    private com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult results;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Paint boxPaint;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Paint textBackgroundPaint;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Paint textPaint;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Rect bounds;
    @org.jetbrains.annotations.Nullable()
    private com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult poseResults;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Paint posePointPaint;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Paint poseLinePaint;
    private float scaleFactor = 1.0F;
    private int outputWidth = 0;
    private int outputHeight = 0;
    private int outputRotate = 0;
    @org.jetbrains.annotations.NotNull()
    private com.google.mediapipe.tasks.vision.core.RunningMode runningMode = com.google.mediapipe.tasks.vision.core.RunningMode.IMAGE;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap motionMask;
    private boolean motionActive = false;
    private boolean personActive = false;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint maskPaint = null;
    private boolean poseEnabled = true;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Matrix transformMatrix = null;
    @org.jetbrains.annotations.NotNull()
    private final float[] tmpPts = null;
    private static final int BOUNDING_RECT_TEXT_PADDING = 8;
    private static final float POSE_STROKE_WIDTH = 12.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.google.mediapipe.examples.objectdetection.OverlayView.Companion Companion = null;
    
    public OverlayView(@org.jetbrains.annotations.Nullable()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public final void clear() {
    }
    
    public final void setRunningMode(@org.jetbrains.annotations.NotNull()
    com.google.mediapipe.tasks.vision.core.RunningMode runningMode) {
    }
    
    public final void setResults(@org.jetbrains.annotations.NotNull()
    com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult detectionResults, int outputHeight, int outputWidth, int imageRotation) {
    }
    
    public final void setPoseEnabled(boolean enabled) {
    }
    
    /**
     * Call this from your analyzer when you have a PoseLandmarkerResult
     * (or set to null to hide pose).
     *
     * IMPORTANT: pass the same output dims + rotation you pass into setResults()
     * so both overlays align to the same preview transform.
     */
    public final void setPoseResults(@org.jetbrains.annotations.Nullable()
    com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult poseLandmarkerResults, int outputHeight, int outputWidth, int imageRotation) {
    }
    
    public final void setMotionMask(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap mask, boolean motionActive) {
    }
    
    public final void setPersonActive(boolean active) {
    }
    
    @java.lang.Override()
    public void draw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    private final void drawObjectDetections(android.graphics.Canvas canvas) {
    }
    
    private final void drawPose(android.graphics.Canvas canvas) {
    }
    
    private final void recalcScaleFactor() {
    }
    
    /**
     * Maps an un-rotated rect in output-image coordinates into view coordinates,
     * applying rotation and then scaleFactor (same logic you already used).
     */
    private final android.graphics.RectF mapRectToView(android.graphics.RectF src) {
        return null;
    }
    
    private final void updateTransformMatrix() {
    }
    
    /**
     * Maps a point in output-image coordinates into view coordinates,
     * applying the same rotation transform and scaleFactor.
     */
    private final android.graphics.PointF mapPointToView(float x, float y) {
        return null;
    }
    
    private final void initPaints() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/OverlayView$Companion;", "", "()V", "BOUNDING_RECT_TEXT_PADDING", "", "POSE_STROKE_WIDTH", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}