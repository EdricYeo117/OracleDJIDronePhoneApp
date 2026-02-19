package com.google.mediapipe.examples.objectdetection;

/**
 * A custom view that renders:
 * 1. Bounding boxes for detected objects.
 * 2. Classification labels and confidence scores.
 * 3. Pose landmarks (skeletons) if enabled.
 * 4. Motion masks (for debugging background subtraction).
 *
 * It handles coordinate mapping from the image space (normalized or pixel) to the view space.
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 ^2\u00020\u0001:\u0001^B\u001b\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0006\u00102\u001a\u000203J\u000e\u00104\u001a\u0002032\u0006\u0010\u001c\u001a\u00020\u001dJ\"\u00106\u001a\u0002032\b\u00107\u001a\u0004\u0018\u00010\u001f2\u0006\u00108\u001a\u00020!2\b\b\u0002\u00109\u001a\u00020\u0017J&\u0010:\u001a\u0002032\u0006\u0010;\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010<\u001a\u00020\u0017J\u000e\u0010=\u001a\u0002032\u0006\u00108\u001a\u00020!J(\u0010>\u001a\u0002032\b\u0010?\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010<\u001a\u00020\u0017J\b\u0010@\u001a\u000203H\u0002J\u0018\u0010A\u001a\u0002032\b\u0010B\u001a\u0004\u0018\u00010\u001f2\u0006\u0010 \u001a\u00020!J\u000e\u0010C\u001a\u0002032\u0006\u0010D\u001a\u00020!J\u0010\u0010E\u001a\u0002032\u0006\u0010F\u001a\u00020GH\u0016J(\u0010H\u001a\u0002032\u0006\u0010I\u001a\u00020\u00172\u0006\u0010J\u001a\u00020\u00172\u0006\u0010K\u001a\u00020\u00172\u0006\u0010L\u001a\u00020\u0017H\u0014J0\u0010M\u001a\u0002032\u0006\u0010F\u001a\u00020G2\u0006\u00107\u001a\u00020\u001f2\u0006\u0010N\u001a\u00020\u000f2\u0006\u0010O\u001a\u00020\u00172\u0006\u0010P\u001a\u00020\u000bH\u0002J\u0010\u0010Q\u001a\u0002032\u0006\u0010F\u001a\u00020GH\u0002J\u0010\u0010R\u001a\u0002032\u0006\u0010F\u001a\u00020GH\u0002J\b\u0010S\u001a\u000203H\u0002J\u0010\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020UH\u0002J\b\u0010W\u001a\u000203H\u0002J\u0018\u0010X\u001a\u00020Y2\u0006\u0010Z\u001a\u00020\u00152\u0006\u0010[\u001a\u00020\u0015H\u0002J\u0018\u0010\\\u001a\u00020Y2\u0006\u0010Z\u001a\u00020\u00152\u0006\u0010[\u001a\u00020\u0015H\u0002J\b\u0010]\u001a\u000203H\u0002R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006_"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/OverlayView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "results", "Lcom/google/mediapipe/tasks/vision/objectdetector/ObjectDetectorResult;", "boxPaint", "Landroid/graphics/Paint;", "textBackgroundPaint", "textPaint", "bounds", "Landroid/graphics/Rect;", "poseResults", "Lcom/google/mediapipe/tasks/vision/poselandmarker/PoseLandmarkerResult;", "posePointPaint", "poseLinePaint", "scaleFactor", "", "outputWidth", "", "outputHeight", "outputRotate", "offsetX", "offsetY", "runningMode", "Lcom/google/mediapipe/tasks/vision/core/RunningMode;", "motionMask", "Landroid/graphics/Bitmap;", "motionActive", "", "personActive", "maskPaint", "poseEnabled", "transformMatrix", "Landroid/graphics/Matrix;", "tmpPts", "", "poseOutputWidth", "poseOutputHeight", "poseOutputRotate", "poseTransformMatrix", "poseTmpPts", "bgSubBitmap", "bgSubEnabled", "bgSubMode", "bgPaint", "clear", "", "setRunningMode", "pipBorderPaint", "setBackgroundSubtraction", "bitmap", "enabled", "mode", "setResults", "detectionResults", "imageRotation", "setPoseEnabled", "setPoseResults", "poseLandmarkerResults", "updatePoseTransformMatrix", "setMotionMask", "mask", "setPersonActive", "active", "draw", "canvas", "Landroid/graphics/Canvas;", "onSizeChanged", "w", "h", "oldw", "oldh", "drawBitmapRotatedIntoRect", "dst", "rotationDegrees", "paint", "drawObjectDetections", "drawPose", "recalcScaleFactor", "mapRectToView", "Landroid/graphics/RectF;", "src", "updateTransformMatrix", "mapPosePointToView", "Landroid/graphics/PointF;", "x", "y", "mapPointToView", "initPaints", "Companion", "app_debug"})
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
    private float offsetX = 0.0F;
    private float offsetY = 0.0F;
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
    private int poseOutputWidth = 0;
    private int poseOutputHeight = 0;
    private int poseOutputRotate = 0;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Matrix poseTransformMatrix = null;
    @org.jetbrains.annotations.NotNull()
    private final float[] poseTmpPts = null;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap bgSubBitmap;
    private boolean bgSubEnabled = false;
    private int bgSubMode = 0;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint bgPaint = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint pipBorderPaint = null;
    private static final int BOUNDING_RECT_TEXT_PADDING = 8;
    private static final float POSE_STROKE_WIDTH = 12.0F;
    @org.jetbrains.annotations.NotNull()
    private static final int[][] POSE_CONNECTIONS = {{0, 1}, {1, 2}, {2, 3}, {3, 7}, {0, 4}, {4, 5}, {5, 6}, {6, 8}, {9, 10}, {11, 12}, {11, 13}, {13, 15}, {15, 17}, {15, 19}, {15, 21}, {17, 19}, {12, 14}, {14, 16}, {16, 18}, {16, 20}, {16, 22}, {18, 20}, {11, 23}, {12, 24}, {23, 24}, {23, 25}, {24, 26}, {25, 27}, {26, 28}, {27, 29}, {28, 30}, {29, 31}, {30, 32}, {27, 31}, {28, 32}};
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
    
    public final void setBackgroundSubtraction(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap bitmap, boolean enabled, int mode) {
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
    
    private final void updatePoseTransformMatrix() {
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
    
    @java.lang.Override()
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    }
    
    private final void drawBitmapRotatedIntoRect(android.graphics.Canvas canvas, android.graphics.Bitmap bitmap, android.graphics.Rect dst, int rotationDegrees, android.graphics.Paint paint) {
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
    
    private final android.graphics.PointF mapPosePointToView(float x, float y) {
        return null;
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
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0015\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/OverlayView$Companion;", "", "<init>", "()V", "BOUNDING_RECT_TEXT_PADDING", "", "POSE_STROKE_WIDTH", "", "POSE_CONNECTIONS", "", "", "[[I", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}