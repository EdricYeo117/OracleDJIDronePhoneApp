package com.google.mediapipe.examples.objectdetection.fragments;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u00d2\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\u00a2\u0006\u0004\b\u0003\u0010\u0004J\b\u0010<\u001a\u00020=H\u0002J\b\u0010>\u001a\u00020=H\u0016J\b\u0010?\u001a\u00020=H\u0016J\b\u0010@\u001a\u00020=H\u0016J$\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020D2\b\u0010E\u001a\u0004\u0018\u00010F2\b\u0010G\u001a\u0004\u0018\u00010HH\u0016J\b\u0010I\u001a\u00020=H\u0002J\b\u0010J\u001a\u00020=H\u0002J\b\u0010K\u001a\u00020=H\u0002J\u001a\u0010L\u001a\u00020=2\u0006\u0010M\u001a\u00020B2\b\u0010G\u001a\u0004\u0018\u00010HH\u0017J\b\u0010N\u001a\u00020=H\u0002J\b\u0010O\u001a\u00020=H\u0002J\b\u0010P\u001a\u00020=H\u0002J\b\u0010Q\u001a\u00020=H\u0003J\u0010\u0010R\u001a\u00020=2\u0006\u0010S\u001a\u00020TH\u0016J\u0010\u0010U\u001a\u00020=2\u0006\u0010V\u001a\u00020WH\u0016J\u0010\u0010X\u001a\u00020=2\u0006\u0010Y\u001a\u00020ZH\u0002J\u0018\u0010[\u001a\u00020=2\u0006\u0010\\\u001a\u00020\u00172\u0006\u0010]\u001a\u00020^H\u0016J\u0010\u0010_\u001a\u00020:2\u0006\u0010`\u001a\u00020aH\u0002R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\u00020\u001b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020 X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010!\u001a\u00020\"8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b%\u0010\n\u001a\u0004\b#\u0010$R\u0010\u0010&\u001a\u0004\u0018\u00010\'X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010-X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020/X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u000203X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u000106X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u00109\u001a\u0004\u0018\u00010:X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006b"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/fragments/CameraFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper$DetectorListener;", "<init>", "()V", "intruderClient", "LIntruderApiClient;", "getIntruderClient", "()LIntruderApiClient;", "intruderClient$delegate", "Lkotlin/Lazy;", "netCallback", "Landroid/net/ConnectivityManager$NetworkCallback;", "lastPingMs", "", "lastMotionActive", "", "getLastMotionActive", "()Z", "setLastMotionActive", "(Z)V", "pingCooldownMs", "TAG", "", "motionGate", "Lcom/google/mediapipe/examples/objectdetection/fragments/MotionGate;", "_fragmentCameraBinding", "Lcom/google/mediapipe/examples/objectdetection/databinding/FragmentCameraBinding;", "fragmentCameraBinding", "getFragmentCameraBinding", "()Lcom/google/mediapipe/examples/objectdetection/databinding/FragmentCameraBinding;", "objectDetectorHelper", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper;", "viewModel", "Lcom/google/mediapipe/examples/objectdetection/MainViewModel;", "getViewModel", "()Lcom/google/mediapipe/examples/objectdetection/MainViewModel;", "viewModel$delegate", "preview", "Landroidx/camera/core/Preview;", "imageAnalyzer", "Landroidx/camera/core/ImageAnalysis;", "camera", "Landroidx/camera/core/Camera;", "cameraProvider", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "backgroundExecutor", "Ljava/util/concurrent/ExecutorService;", "increasedAccuracySwitch", "Lcom/google/android/material/switchmaterial/SwitchMaterial;", "personFilter", "Lcom/google/mediapipe/examples/objectdetection/utils/BooleanWindowFilter;", "poseFilter", "poseHelper", "Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper;", "lastPoseRunMs", "poseMinIntervalMs", "latestPoseBitmap", "Landroid/graphics/Bitmap;", "latestPoseTimestampMs", "ensurePoseHelper", "", "onResume", "onPause", "onDestroyView", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "refreshNetworkLabels", "startNetworkCallback", "stopNetworkCallback", "onViewCreated", "view", "initBottomSheetControls", "updateControlsUi", "setUpCamera", "bindCameraUseCases", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onResults", "resultBundle", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper$ResultBundle;", "pingIntruder", "bestPersonScore", "", "onError", "error", "errorCode", "", "rgba8888ImageProxyToBitmap", "imageProxy", "Landroidx/camera/core/ImageProxy;", "app_debug"})
public final class CameraFragment extends androidx.fragment.app.Fragment implements com.google.mediapipe.examples.objectdetection.ObjectDetectorHelper.DetectorListener {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy intruderClient$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private android.net.ConnectivityManager.NetworkCallback netCallback;
    private long lastPingMs = 0L;
    private boolean lastMotionActive = false;
    private final long pingCooldownMs = 5000L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "ObjectDetection";
    @org.jetbrains.annotations.NotNull()
    private final com.google.mediapipe.examples.objectdetection.fragments.MotionGate motionGate = null;
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
    @org.jetbrains.annotations.Nullable()
    private volatile android.graphics.Bitmap latestPoseBitmap;
    @kotlin.jvm.Volatile()
    private volatile long latestPoseTimestampMs = 0L;
    
    public CameraFragment() {
        super();
    }
    
    private final IntruderApiClient getIntruderClient() {
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
    
    @android.annotation.SuppressLint(value = {"UnsafeOptInUsageError"})
    private final void bindCameraUseCases() {
    }
    
    @java.lang.Override()
    public void onConfigurationChanged(@org.jetbrains.annotations.NotNull()
    android.content.res.Configuration newConfig) {
    }
    
    @java.lang.Override()
    public void onResults(@org.jetbrains.annotations.NotNull()
    com.google.mediapipe.examples.objectdetection.ObjectDetectorHelper.ResultBundle resultBundle) {
    }
    
    private final void pingIntruder(float bestPersonScore) {
    }
    
    @java.lang.Override()
    public void onError(@org.jetbrains.annotations.NotNull()
    java.lang.String error, int errorCode) {
    }
    
    private final android.graphics.Bitmap rgba8888ImageProxyToBitmap(androidx.camera.core.ImageProxy imageProxy) {
        return null;
    }
}