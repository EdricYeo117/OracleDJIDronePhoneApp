package com.google.mediapipe.examples.objectdetection.fragments;

/**
 * The core fragment for the live camera feed.
 *
 * Responsibilities:
 * - Setting up CameraX (Preview and ImageAnalysis).
 * - Running background subtraction via [MotionGate].
 * - Running object detection via [ObjectDetectorHelper].
 * - Running pose verification via [PoseLandmarkerHelper].
 * - Updating the UI [OverlayView] and status text.
 * - Sending intruder alerts via [IntruderApiClient].
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u00dc\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 ~2\u00020\u00012\u00020\u0002:\u0001~B\u0007\u00a2\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010M\u001a\u00020NH\u0002J\b\u0010O\u001a\u00020NH\u0016J\b\u0010P\u001a\u00020NH\u0016J\b\u0010Q\u001a\u00020NH\u0016J$\u0010R\u001a\u00020S2\u0006\u0010T\u001a\u00020U2\b\u0010V\u001a\u0004\u0018\u00010W2\b\u0010X\u001a\u0004\u0018\u00010YH\u0016J\b\u0010Z\u001a\u00020NH\u0002J\b\u0010[\u001a\u00020NH\u0002J\b\u0010\\\u001a\u00020NH\u0002J\u001a\u0010]\u001a\u00020N2\u0006\u0010^\u001a\u00020S2\b\u0010X\u001a\u0004\u0018\u00010YH\u0017J\b\u0010_\u001a\u00020NH\u0002J\b\u0010`\u001a\u00020NH\u0002J\b\u0010a\u001a\u00020NH\u0002J\u0018\u0010b\u001a\u00020F2\u0006\u0010c\u001a\u00020F2\u0006\u0010d\u001a\u00020=H\u0002J\b\u0010e\u001a\u00020NH\u0003J\u0010\u0010f\u001a\u00020N2\u0006\u0010g\u001a\u00020hH\u0016J\u0010\u0010i\u001a\u00020N2\u0006\u0010j\u001a\u00020kH\u0016J\u0012\u0010l\u001a\u00020\u00182\b\u0010m\u001a\u0004\u0018\u00010\u0018H\u0002J\u0018\u0010n\u001a\u00020N2\u0006\u0010o\u001a\u00020p2\u0006\u0010q\u001a\u00020\u0018H\u0002J\u0010\u0010r\u001a\u00020N2\u0006\u0010s\u001a\u00020pH\u0002J\b\u0010t\u001a\u00020NH\u0002J\u0018\u0010u\u001a\u00020N2\u0006\u0010v\u001a\u00020\u00182\u0006\u0010w\u001a\u00020=H\u0016J\u0012\u0010x\u001a\u00020\f2\b\u0010y\u001a\u0004\u0018\u00010zH\u0002J\u0010\u0010{\u001a\u00020F2\u0006\u0010|\u001a\u00020}H\u0002R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001d\u001a\u00020\u001c8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010 \u001a\u00020!X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\"\u001a\u00020#8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b&\u0010\'\u001a\u0004\b$\u0010%R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010-X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000201X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u000204X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000206X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u000206X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u00108\u001a\u0004\u0018\u000109X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020=X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010E\u001a\u0004\u0018\u00010FX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u000204X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u000204X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u007f"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/fragments/CameraFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper$DetectorListener;", "<init>", "()V", "intruderClient", "LIntruderApiClient;", "netCallback", "Landroid/net/ConnectivityManager$NetworkCallback;", "lastPingMs", "", "lastMotionActive", "", "getLastMotionActive", "()Z", "setLastMotionActive", "(Z)V", "analyzeUntilMs", "analyzeHoldMs", "lastDetectMs", "detectIntervalNoMotionMs", "detectIntervalMotionMs", "pingCooldownMs", "TAG", "", "motionGate", "Lcom/google/mediapipe/examples/objectdetection/fragments/MotionGateV2;", "_fragmentCameraBinding", "Lcom/google/mediapipe/examples/objectdetection/databinding/FragmentCameraBinding;", "fragmentCameraBinding", "getFragmentCameraBinding", "()Lcom/google/mediapipe/examples/objectdetection/databinding/FragmentCameraBinding;", "objectDetectorHelper", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper;", "viewModel", "Lcom/google/mediapipe/examples/objectdetection/MainViewModel;", "getViewModel", "()Lcom/google/mediapipe/examples/objectdetection/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "preview", "Landroidx/camera/core/Preview;", "imageAnalyzer", "Landroidx/camera/core/ImageAnalysis;", "camera", "Landroidx/camera/core/Camera;", "cameraProvider", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "backgroundExecutor", "Ljava/util/concurrent/ExecutorService;", "intruderExecutor", "increasedAccuracySwitch", "Lcom/google/android/material/switchmaterial/SwitchMaterial;", "personFilter", "Lcom/google/mediapipe/examples/objectdetection/utils/BooleanWindowFilter;", "poseFilter", "poseHelper", "Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper;", "lastPoseRunMs", "poseMinIntervalMs", "latestPoseRotationDegrees", "", "intruderPresent", "lastSeenMs", "lastEnterPingMs", "lastHeartbeatMs", "exitGraceMs", "enterCooldownMs", "heartbeatEveryMs", "latestPoseBitmap", "Landroid/graphics/Bitmap;", "latestPoseTimestampMs", "bgSubSwitch", "showBgSub", "showMotionMask", "motionGateSwitch", "motionGateEnabled", "ensurePoseHelper", "", "onResume", "onPause", "onDestroyView", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "refreshNetworkLabels", "startNetworkCallback", "stopNetworkCallback", "onViewCreated", "view", "initBottomSheetControls", "updateControlsUi", "setUpCamera", "rotateBitmap", "src", "rotationDegrees", "bindCameraUseCases", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onResults", "resultBundle", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper$ResultBundle;", "maskKey", "k", "sendIntruderPing", "score", "", "eventType", "onIntruderConfirmed", "bestPersonScore", "onNoIntruder", "onError", "error", "errorCode", "isLikelyFullBodyPose", "pose", "Lcom/google/mediapipe/tasks/vision/poselandmarker/PoseLandmarkerResult;", "rgba8888ImageProxyToBitmap", "imageProxy", "Landroidx/camera/core/ImageProxy;", "Companion", "app_debug"})
public final class CameraFragment extends androidx.fragment.app.Fragment implements com.google.mediapipe.examples.objectdetection.ObjectDetectorHelper.DetectorListener {
    @org.jetbrains.annotations.Nullable()
    private android.net.ConnectivityManager.NetworkCallback netCallback;
    private long lastPingMs = 0L;
    private boolean lastMotionActive = false;
    private long analyzeUntilMs = 0L;
    private final long analyzeHoldMs = 1500L;
    private long lastDetectMs = 0L;
    private final long detectIntervalNoMotionMs = 400L;
    private final long detectIntervalMotionMs = 0L;
    private final long pingCooldownMs = 5000L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "ObjectDetection";
    @org.jetbrains.annotations.NotNull()
    private final com.google.mediapipe.examples.objectdetection.fragments.MotionGateV2 motionGate = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.mediapipe.examples.objectdetection.databinding.FragmentCameraBinding _fragmentCameraBinding;
    private com.google.mediapipe.examples.objectdetection.ObjectDetectorHelper objectDetectorHelper;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.core.Preview preview;
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.core.ImageAnalysis imageAnalyzer;
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.core.Camera camera;
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.lifecycle.ProcessCameraProvider cameraProvider;
    
    /**
     * Blocking ML operations are performed using this executor
     */
    private java.util.concurrent.ExecutorService backgroundExecutor;
    private java.util.concurrent.ExecutorService intruderExecutor;
    private com.google.android.material.switchmaterial.SwitchMaterial increasedAccuracySwitch;
    @org.jetbrains.annotations.NotNull()
    private final com.google.mediapipe.examples.objectdetection.utils.BooleanWindowFilter personFilter = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.mediapipe.examples.objectdetection.utils.BooleanWindowFilter poseFilter = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper poseHelper;
    private long lastPoseRunMs = 0L;
    private final long poseMinIntervalMs = 250L;
    @kotlin.jvm.Volatile()
    private volatile int latestPoseRotationDegrees = 0;
    private boolean intruderPresent = false;
    private long lastSeenMs = 0L;
    private long lastEnterPingMs = 0L;
    private long lastHeartbeatMs = 0L;
    private final long exitGraceMs = 1200L;
    private final long enterCooldownMs = 4000L;
    private final long heartbeatEveryMs = 15000L;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile android.graphics.Bitmap latestPoseBitmap;
    @kotlin.jvm.Volatile()
    private volatile long latestPoseTimestampMs = 0L;
    private com.google.android.material.switchmaterial.SwitchMaterial bgSubSwitch;
    @kotlin.jvm.Volatile()
    private volatile boolean showBgSub = false;
    private boolean showMotionMask = false;
    private com.google.android.material.switchmaterial.SwitchMaterial motionGateSwitch;
    @kotlin.jvm.Volatile()
    private volatile boolean motionGateEnabled = true;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG_CLIENT = "IntruderClientInit";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG_PING = "IntruderPing";
    @org.jetbrains.annotations.NotNull()
    public static final com.google.mediapipe.examples.objectdetection.fragments.CameraFragment.Companion Companion = null;
    
    public CameraFragment() {
        super();
    }
    
    private final IntruderApiClient intruderClient() {
        return null;
    }
    
    public final boolean getLastMotionActive() {
        return false;
    }
    
    public final void setLastMotionActive(boolean p0) {
    }
    
    private final com.google.mediapipe.examples.objectdetection.databinding.FragmentCameraBinding getFragmentCameraBinding() {
        return null;
    }
    
    private final com.google.mediapipe.examples.objectdetection.MainViewModel getViewModel() {
        return null;
    }
    
    private final void ensurePoseHelper() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    private final void refreshNetworkLabels() {
    }
    
    private final void startNetworkCallback() {
    }
    
    private final void stopNetworkCallback() {
    }
    
    @java.lang.Override()
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initBottomSheetControls() {
    }
    
    private final void updateControlsUi() {
    }
    
    private final void setUpCamera() {
    }
    
    private final android.graphics.Bitmap rotateBitmap(android.graphics.Bitmap src, int rotationDegrees) {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"UnsafeOptInUsageError"})
    private final void bindCameraUseCases() {
    }
    
    @java.lang.Override()
    public void onConfigurationChanged(@org.jetbrains.annotations.NotNull()
    android.content.res.Configuration newConfig) {
    }
    
    /**
     * Callback from [ObjectDetectorHelper].
     *
     * This method:
     * 1. Filters results for "person" class with high confidence.
     * 2. Updates persistence filters to avoid flickering.
     * 3. Updates the UI overlay.
     * 4. Triggers secondary pose verification if enabled.
     * 5. Sends an alert if an intruder is confirmed.
     *
     * @param resultBundle The detection results including inference time and image dimensions.
     */
    @java.lang.Override()
    public void onResults(@org.jetbrains.annotations.NotNull()
    com.google.mediapipe.examples.objectdetection.ObjectDetectorHelper.ResultBundle resultBundle) {
    }
    
    private final java.lang.String maskKey(java.lang.String k) {
        return null;
    }
    
    private final void sendIntruderPing(float score, java.lang.String eventType) {
    }
    
    private final void onIntruderConfirmed(float bestPersonScore) {
    }
    
    private final void onNoIntruder() {
    }
    
    @java.lang.Override()
    public void onError(@org.jetbrains.annotations.NotNull()
    java.lang.String error, int errorCode) {
    }
    
    private final boolean isLikelyFullBodyPose(com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult pose) {
        return false;
    }
    
    private final android.graphics.Bitmap rgba8888ImageProxyToBitmap(androidx.camera.core.ImageProxy imageProxy) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/fragments/CameraFragment$Companion;", "", "<init>", "()V", "TAG_CLIENT", "", "TAG_PING", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}