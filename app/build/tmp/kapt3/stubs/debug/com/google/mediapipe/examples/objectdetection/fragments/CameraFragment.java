package com.google.mediapipe.examples.objectdetection.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00d4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010;\u001a\u00020<H\u0003J\b\u0010=\u001a\u00020<H\u0002J\b\u0010>\u001a\u00020<H\u0002J\u0010\u0010?\u001a\u00020<2\u0006\u0010@\u001a\u00020AH\u0016J$\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\b\u0010F\u001a\u0004\u0018\u00010G2\b\u0010H\u001a\u0004\u0018\u00010IH\u0016J\b\u0010J\u001a\u00020<H\u0016J\u0018\u0010K\u001a\u00020<2\u0006\u0010L\u001a\u00020\u00052\u0006\u0010M\u001a\u00020NH\u0016J\b\u0010O\u001a\u00020<H\u0016J\u0010\u0010P\u001a\u00020<2\u0006\u0010Q\u001a\u00020RH\u0016J\b\u0010S\u001a\u00020<H\u0016J\u001a\u0010T\u001a\u00020<2\u0006\u0010U\u001a\u00020C2\b\u0010H\u001a\u0004\u0018\u00010IH\u0017J\u0010\u0010V\u001a\u00020<2\u0006\u0010W\u001a\u00020XH\u0002J\b\u0010Y\u001a\u00020<H\u0002J\u0010\u0010Z\u001a\u00020%2\u0006\u0010[\u001a\u00020\\H\u0002J\b\u0010]\u001a\u00020<H\u0002J\b\u0010^\u001a\u00020<H\u0002J\b\u0010_\u001a\u00020<H\u0002J\b\u0010`\u001a\u00020<H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u000e\u0010!\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\"X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u000102X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\"X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u00104\u001a\u0004\u0018\u000105X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u00106\u001a\u0002078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b:\u0010\u001a\u001a\u0004\b8\u00109\u00a8\u0006a"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/fragments/CameraFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper$DetectorListener;", "()V", "TAG", "", "_fragmentCameraBinding", "Lcom/google/mediapipe/examples/objectdetection/databinding/FragmentCameraBinding;", "backgroundExecutor", "Ljava/util/concurrent/ExecutorService;", "camera", "Landroidx/camera/core/Camera;", "cameraProvider", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "fragmentCameraBinding", "getFragmentCameraBinding", "()Lcom/google/mediapipe/examples/objectdetection/databinding/FragmentCameraBinding;", "imageAnalyzer", "Landroidx/camera/core/ImageAnalysis;", "increasedAccuracySwitch", "Lcom/google/android/material/switchmaterial/SwitchMaterial;", "intruderClient", "LIntruderApiClient;", "getIntruderClient", "()LIntruderApiClient;", "intruderClient$delegate", "Lkotlin/Lazy;", "lastMotionActive", "", "getLastMotionActive", "()Z", "setLastMotionActive", "(Z)V", "lastPingMs", "", "lastPoseRunMs", "latestPoseBitmap", "Landroid/graphics/Bitmap;", "latestPoseTimestampMs", "motionGate", "Lcom/google/mediapipe/examples/objectdetection/fragments/MotionGate;", "netCallback", "Landroid/net/ConnectivityManager$NetworkCallback;", "objectDetectorHelper", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper;", "personFilter", "Lcom/google/mediapipe/examples/objectdetection/utils/BooleanWindowFilter;", "pingCooldownMs", "poseFilter", "poseHelper", "Lcom/google/mediapipe/examples/objectdetection/utils/PoseLandmarkerHelper;", "poseMinIntervalMs", "preview", "Landroidx/camera/core/Preview;", "viewModel", "Lcom/google/mediapipe/examples/objectdetection/MainViewModel;", "getViewModel", "()Lcom/google/mediapipe/examples/objectdetection/MainViewModel;", "viewModel$delegate", "bindCameraUseCases", "", "ensurePoseHelper", "initBottomSheetControls", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onError", "error", "errorCode", "", "onPause", "onResults", "resultBundle", "Lcom/google/mediapipe/examples/objectdetection/ObjectDetectorHelper$ResultBundle;", "onResume", "onViewCreated", "view", "pingIntruder", "bestPersonScore", "", "refreshNetworkLabels", "rgba8888ImageProxyToBitmap", "imageProxy", "Landroidx/camera/core/ImageProxy;", "setUpCamera", "startNetworkCallback", "stopNetworkCallback", "updateControlsUi", "app_debug"})
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