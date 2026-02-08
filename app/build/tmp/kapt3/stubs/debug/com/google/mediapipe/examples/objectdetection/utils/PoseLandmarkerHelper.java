package com.google.mediapipe.examples.objectdetection.utils;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 C2\u00020\u0001:\u0003ABCBa\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\t\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\u0004\b\u0011\u0010\u0012B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\u0011\u0010\u0013J\u0006\u0010&\u001a\u00020\'J\u0006\u0010(\u001a\u00020)J\u0006\u0010*\u001a\u00020)J\u0006\u0010+\u001a\u00020)J\u0018\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201J\u0010\u00102\u001a\u0004\u0018\u0001032\u0006\u00104\u001a\u000205J\u0018\u00106\u001a\u0004\u0018\u0001032\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u000201J\u0016\u0010:\u001a\u00020)2\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\'J\u0010\u0010>\u001a\u00020)2\u0006\u0010?\u001a\u00020\u0005H\u0002J\u0010\u0010@\u001a\u0002052\u0006\u0010;\u001a\u00020<H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\n\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0015\"\u0004\b\u0019\u0010\u0017R\u001a\u0010\u000b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0015\"\u0004\b\u001b\u0010\u0017R\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\u000e\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001d\"\u0004\b!\u0010\u001fR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006D"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper;", "", "context", "Landroid/content/Context;", "runningMode", "Lcom/google/mediapipe/tasks/vision/core/RunningMode;", "modelAssetPath", "", "minPoseDetectionConfidence", "", "minPoseTrackingConfidence", "minPosePresenceConfidence", "currentDelegate", "", "currentModel", "poseLandmarkerHelperListener", "Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$LandmarkerListener;", "<init>", "(Landroid/content/Context;Lcom/google/mediapipe/tasks/vision/core/RunningMode;Ljava/lang/String;FFFIILcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$LandmarkerListener;)V", "(Landroid/content/Context;Ljava/lang/String;)V", "getMinPoseDetectionConfidence", "()F", "setMinPoseDetectionConfidence", "(F)V", "getMinPoseTrackingConfidence", "setMinPoseTrackingConfidence", "getMinPosePresenceConfidence", "setMinPosePresenceConfidence", "getCurrentDelegate", "()I", "setCurrentDelegate", "(I)V", "getCurrentModel", "setCurrentModel", "poseLandmarker", "Lcom/google/mediapipe/tasks/vision/poselandmarker/PoseLandmarker;", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "isClose", "", "setupPoseLandmarker", "", "clearPoseLandmarker", "close", "detectVideo", "Lcom/google/mediapipe/tasks/vision/poselandmarker/PoseLandmarkerResult;", "mpImage", "Lcom/google/mediapipe/framework/image/MPImage;", "timestampMs", "", "detectImage", "Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$ResultBundle;", "bitmap", "Landroid/graphics/Bitmap;", "detectVideoFile", "uri", "Landroid/net/Uri;", "intervalMs", "detectLiveStream", "imageProxy", "Landroidx/camera/core/ImageProxy;", "isFrontCamera", "ensureInit", "requiredMode", "rgba8888ImageProxyToBitmap", "LandmarkerListener", "ResultBundle", "Companion", "app_debug"})
public final class PoseLandmarkerHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.mediapipe.tasks.vision.core.RunningMode runningMode = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String modelAssetPath = null;
    private float minPoseDetectionConfidence;
    private float minPoseTrackingConfidence;
    private float minPosePresenceConfidence;
    private int currentDelegate;
    private int currentModel;
    @org.jetbrains.annotations.Nullable()
    private final com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.LandmarkerListener poseLandmarkerHelperListener = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker poseLandmarker;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicBoolean closed = null;
    public static final int DELEGATE_CPU = 0;
    public static final int DELEGATE_GPU = 1;
    public static final int MODEL_POSE_LANDMARKER_LITE = 0;
    public static final int MODEL_POSE_LANDMARKER_FULL = 1;
    public static final int MODEL_POSE_LANDMARKER_HEAVY = 2;
    public static final float DEFAULT_POSE_DETECTION_CONFIDENCE = 0.5F;
    public static final float DEFAULT_POSE_TRACKING_CONFIDENCE = 0.5F;
    public static final float DEFAULT_POSE_PRESENCE_CONFIDENCE = 0.5F;
    public static final int GPU_ERROR = 1;
    public static final int OTHER_ERROR = 2;
    @org.jetbrains.annotations.NotNull()
    public static final com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.Companion Companion = null;
    
    public PoseLandmarkerHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.google.mediapipe.tasks.vision.core.RunningMode runningMode, @org.jetbrains.annotations.Nullable()
    java.lang.String modelAssetPath, float minPoseDetectionConfidence, float minPoseTrackingConfidence, float minPosePresenceConfidence, int currentDelegate, int currentModel, @org.jetbrains.annotations.Nullable()
    com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.LandmarkerListener poseLandmarkerHelperListener) {
        super();
    }
    
    public final float getMinPoseDetectionConfidence() {
        return 0.0F;
    }
    
    public final void setMinPoseDetectionConfidence(float p0) {
    }
    
    public final float getMinPoseTrackingConfidence() {
        return 0.0F;
    }
    
    public final void setMinPoseTrackingConfidence(float p0) {
    }
    
    public final float getMinPosePresenceConfidence() {
        return 0.0F;
    }
    
    public final void setMinPosePresenceConfidence(float p0) {
    }
    
    public final int getCurrentDelegate() {
        return 0;
    }
    
    public final void setCurrentDelegate(int p0) {
    }
    
    public final int getCurrentModel() {
        return 0;
    }
    
    public final void setCurrentModel(int p0) {
    }
    
    /**
     * Convenience constructor that matches YOUR CameraFragment usage:
     * PoseLandmarkerHelper(requireContext(), modelAssetPath = "pose_landmarker_lite.task")
     *
     * For your pose-verification pipeline (timestamped frames), VIDEO mode is correct.
     */
    public PoseLandmarkerHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String modelAssetPath) {
        super();
    }
    
    public final boolean isClose() {
        return false;
    }
    
    public final void setupPoseLandmarker() {
    }
    
    public final void clearPoseLandmarker() {
    }
    
    public final void close() {
    }
    
    /**
     * Your CameraFragment calls:
     *  val poseResult = poseHelper?.detectVideo(mpImage, ts)
     *
     * This is VIDEO mode inference. Must call detectForVideo().
     */
    @org.jetbrains.annotations.Nullable()
    public final com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult detectVideo(@org.jetbrains.annotations.NotNull()
    com.google.mediapipe.framework.image.MPImage mpImage, long timestampMs) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.ResultBundle detectImage(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.ResultBundle detectVideoFile(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, long intervalMs) {
        return null;
    }
    
    public final void detectLiveStream(@org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageProxy imageProxy, boolean isFrontCamera) {
    }
    
    private final void ensureInit(com.google.mediapipe.tasks.vision.core.RunningMode requiredMode) {
    }
    
    /**
     * Converts RGBA_8888 ImageProxy to Bitmap.
     * IMPORTANT: CameraX analyzer must be configured with:
     *  .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
     */
    private final android.graphics.Bitmap rgba8888ImageProxyToBitmap(androidx.camera.core.ImageProxy imageProxy) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$Companion;", "", "<init>", "()V", "DELEGATE_CPU", "", "DELEGATE_GPU", "MODEL_POSE_LANDMARKER_LITE", "MODEL_POSE_LANDMARKER_FULL", "MODEL_POSE_LANDMARKER_HEAVY", "DEFAULT_POSE_DETECTION_CONFIDENCE", "", "DEFAULT_POSE_TRACKING_CONFIDENCE", "DEFAULT_POSE_PRESENCE_CONFIDENCE", "GPU_ERROR", "OTHER_ERROR", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH&\u00a8\u0006\u000b\u00c0\u0006\u0003"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$LandmarkerListener;", "", "onError", "", "error", "", "errorCode", "", "onResults", "resultBundle", "Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$ResultBundle;", "app_debug"})
    public static abstract interface LandmarkerListener {
        
        public abstract void onError(@org.jetbrains.annotations.NotNull()
        java.lang.String error, int errorCode);
        
        public abstract void onResults(@org.jetbrains.annotations.NotNull()
        com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.ResultBundle resultBundle);
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u00a2\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\bH\u00c6\u0003J\t\u0010\u0016\u001a\u00020\bH\u00c6\u0003J7\u0010\u0017\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011\u00a8\u0006\u001e"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper$ResultBundle;", "", "results", "", "Lcom/google/mediapipe/tasks/vision/poselandmarker/PoseLandmarkerResult;", "inferenceTime", "", "inputImageHeight", "", "inputImageWidth", "<init>", "(Ljava/util/List;JII)V", "getResults", "()Ljava/util/List;", "getInferenceTime", "()J", "getInputImageHeight", "()I", "getInputImageWidth", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class ResultBundle {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult> results = null;
        private final long inferenceTime = 0L;
        private final int inputImageHeight = 0;
        private final int inputImageWidth = 0;
        
        public ResultBundle(@org.jetbrains.annotations.NotNull()
        java.util.List<? extends com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult> results, long inferenceTime, int inputImageHeight, int inputImageWidth) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult> getResults() {
            return null;
        }
        
        public final long getInferenceTime() {
            return 0L;
        }
        
        public final int getInputImageHeight() {
            return 0;
        }
        
        public final int getInputImageWidth() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult> component1() {
            return null;
        }
        
        public final long component2() {
            return 0L;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final int component4() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper.ResultBundle copy(@org.jetbrains.annotations.NotNull()
        java.util.List<? extends com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult> results, long inferenceTime, int inputImageHeight, int inputImageWidth) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}