/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.mediapipe.examples.objectdetection.fragments

import IntruderApiClient
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.mediapipe.examples.objectdetection.MainViewModel
import com.google.mediapipe.examples.objectdetection.ObjectDetectorHelper
import com.google.mediapipe.examples.objectdetection.R
import com.google.mediapipe.examples.objectdetection.databinding.FragmentCameraBinding
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult
import com.google.mediapipe.examples.objectdetection.utils.AppPrefs
import com.google.mediapipe.examples.objectdetection.utils.BooleanWindowFilter
import com.google.mediapipe.examples.objectdetection.utils.PoseLandmarkerHelper

// Connectivity Imports
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class CameraFragment : Fragment(), ObjectDetectorHelper.DetectorListener {

    private val intruderClient by lazy {
        IntruderApiClient(
            baseUrl = getString(R.string.red_base_url),
            apiKey = getString(R.string.red_api_key)
        )
    }
    private var netCallback: ConnectivityManager.NetworkCallback? = null
    private var lastPingMs = 0L
     var lastMotionActive: Boolean = false
    private val pingCooldownMs = 5_000L  // prevents spam
    private val TAG = "ObjectDetection"
    private val motionGate = MotionGate()
    private var _fragmentCameraBinding: FragmentCameraBinding? = null
    private val fragmentCameraBinding
        get() = _fragmentCameraBinding!!
    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private val viewModel: MainViewModel by activityViewModels()
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ExecutorService

    private lateinit var increasedAccuracySwitch: com.google.android.material.switchmaterial.SwitchMaterial

    // ===== Temporal persistence =====
    private val personFilter = BooleanWindowFilter(windowSize = 8, minHits = 3)
    private val poseFilter = BooleanWindowFilter(windowSize = 6, minHits = 2)

    // ===== Pose verification =====
    private var poseHelper: PoseLandmarkerHelper? = null
    private var lastPoseRunMs: Long = 0L
    private val poseMinIntervalMs = 250L // 4 Hz

    @Volatile private var latestPoseBitmap: android.graphics.Bitmap? = null
    @Volatile private var latestPoseTimestampMs: Long = 0L

    private fun ensurePoseHelper() {
        if (poseHelper == null) {
            poseHelper = PoseLandmarkerHelper(requireContext(), modelAssetPath = "pose_landmarker_lite.task")
        }
    }
    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(
                requireActivity(),
                R.id.fragment_container
            )
                .navigate(R.id.permissions_fragment)
        }

        backgroundExecutor.execute {
            if (objectDetectorHelper.isClosed()) {
                objectDetectorHelper.setupObjectDetector()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // save ObjectDetector settings
        if(this::objectDetectorHelper.isInitialized) {
            viewModel.setModel(objectDetectorHelper.currentModel)
            viewModel.setDelegate(objectDetectorHelper.currentDelegate)
            viewModel.setThreshold(objectDetectorHelper.threshold)
            viewModel.setMaxResults(objectDetectorHelper.maxResults)
            // Close the object detector and release resources
            backgroundExecutor.execute { objectDetectorHelper.clearObjectDetector() }
        }

    }

    override fun onDestroyView() {
        stopNetworkCallback()
        poseHelper?.close()
        poseHelper = null
        latestPoseBitmap = null
        _fragmentCameraBinding = null
        super.onDestroyView()

        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding =
            FragmentCameraBinding.inflate(inflater, container, false)

        return fragmentCameraBinding.root
    }

    private fun refreshNetworkLabels() {
        if (_fragmentCameraBinding == null) return
        fragmentCameraBinding.tvRedIp.text = "RED: ${getString(R.string.red_base_url)}"

        val ip = NetworkUtils.getPhoneIpv4(requireContext()) ?: "N/A"
        val transport = NetworkUtils.getActiveTransport(requireContext())
        fragmentCameraBinding.tvPhoneIp.text = "Phone IP ($transport): $ip"
    }

    private fun startNetworkCallback() {
        val cm = requireContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                activity?.runOnUiThread { refreshNetworkLabels() }
            }
            override fun onLost(network: Network) {
                activity?.runOnUiThread { refreshNetworkLabels() }
            }
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                activity?.runOnUiThread { refreshNetworkLabels() }
            }
        }
        netCallback = callback
        cm.registerDefaultNetworkCallback(callback)
    }

    private fun stopNetworkCallback() {
        val cb = netCallback ?: return
        val cm = requireContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(cb)
        netCallback = null
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Switch to activate increased accuracy mode
        increasedAccuracySwitch = view.findViewById(R.id.switch_increased_accuracy)
        // load saved state
        increasedAccuracySwitch.isChecked = AppPrefs.isPoseVerificationEnabled(requireContext())

        increasedAccuracySwitch.setOnCheckedChangeListener { _, isChecked ->
            AppPrefs.setPoseVerificationEnabled(requireContext(), isChecked)
        }
        // RED base URL from strings.xml (you already did this via IntruderApiClient)
        fragmentCameraBinding.tvRedIp.text = "RED: ${getString(R.string.red_base_url)}"

        // Phone IP (your NetworkUtils method)
        val phoneIp = NetworkUtils.getPhoneIpv4(requireContext()) ?: "N/A"
        fragmentCameraBinding.tvPhoneIp.text = "Phone IP (WiFi): $phoneIp"

        // Initial status
        fragmentCameraBinding.tvStatus.text = getString(R.string.status_idle)
        fragmentCameraBinding.tvStatus.setBackgroundResource(R.drawable.bg_oracle_badge_outline)

        // Initialize our background executor
        backgroundExecutor = Executors.newSingleThreadExecutor()

        // Create the ObjectDetectionHelper that will handle the inference
        backgroundExecutor.execute {
            objectDetectorHelper =
                ObjectDetectorHelper(
                    context = requireContext(),
                    threshold = viewModel.currentThreshold,
                    currentDelegate = viewModel.currentDelegate,
                    currentModel = viewModel.currentModel,
                    maxResults = viewModel.currentMaxResults,
                    objectDetectorListener = this,
                    runningMode = RunningMode.LIVE_STREAM
                )

            // Wait for the views to be properly laid out
            fragmentCameraBinding.viewFinder.post {
                // Set up the camera and its use cases
                setUpCamera()
            }
        }

        // Attach listeners to UI control widgets
        initBottomSheetControls()
        refreshNetworkLabels()
        startNetworkCallback()
        fragmentCameraBinding.overlay.setRunningMode(RunningMode.LIVE_STREAM)
    }

    private fun initBottomSheetControls() {
        // Init bottom sheet settings
        fragmentCameraBinding.bottomSheetLayout.maxResultsValue.text =
            viewModel.currentMaxResults.toString()
        fragmentCameraBinding.bottomSheetLayout.thresholdValue.text =
            String.format("%.2f", viewModel.currentThreshold)

        // When clicked, lower detection score threshold floor
        fragmentCameraBinding.bottomSheetLayout.thresholdMinus.setOnClickListener {
            if (objectDetectorHelper.threshold >= 0.1) {
                objectDetectorHelper.threshold -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise detection score threshold floor
        fragmentCameraBinding.bottomSheetLayout.thresholdPlus.setOnClickListener {
            if (objectDetectorHelper.threshold <= 0.8) {
                objectDetectorHelper.threshold += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, reduce the number of objects that can be detected at a time
        fragmentCameraBinding.bottomSheetLayout.maxResultsMinus.setOnClickListener {
            if (objectDetectorHelper.maxResults > 1) {
                objectDetectorHelper.maxResults--
                updateControlsUi()
            }
        }

        // When clicked, increase the number of objects that can be detected at a time
        fragmentCameraBinding.bottomSheetLayout.maxResultsPlus.setOnClickListener {
            if (objectDetectorHelper.maxResults < 5) {
                objectDetectorHelper.maxResults++
                updateControlsUi()
            }
        }

        // When clicked, change the underlying hardware used for inference. Current options are CPU
        // GPU, and NNAPI
        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.setSelection(
            viewModel.currentDelegate,
            false
        )
        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    try {
                        objectDetectorHelper.currentDelegate = p2
                        updateControlsUi()
                    } catch(e: UninitializedPropertyAccessException) {
                        Log.e(TAG, "ObjectDetectorHelper has not been initialized yet.")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }

        // When clicked, change the underlying model used for object detection
        fragmentCameraBinding.bottomSheetLayout.spinnerModel.setSelection(
            viewModel.currentModel,
            false
        )
        fragmentCameraBinding.bottomSheetLayout.spinnerModel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    try {
                        objectDetectorHelper.currentModel = p2
                        updateControlsUi()
                    } catch(e: UninitializedPropertyAccessException) {
                        Log.e(TAG, "ObjectDetectorHelper has not been initialized yet.")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }
    }

    // Update the values displayed in the bottom sheet. Reset detector.
    private fun updateControlsUi() {
        fragmentCameraBinding.bottomSheetLayout.maxResultsValue.text =
            objectDetectorHelper.maxResults.toString()
        fragmentCameraBinding.bottomSheetLayout.thresholdValue.text =
            String.format("%.2f", objectDetectorHelper.threshold)

        backgroundExecutor.execute {
            objectDetectorHelper.clearObjectDetector()
            objectDetectorHelper.setupObjectDetector()
        }

        fragmentCameraBinding.overlay.clear()
    }

    // Initialize CameraX, and prepare to bind the camera use cases
    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    // Declare and bind preview, capture and analysis use cases
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider =
            cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector =
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(backgroundExecutor) { imageProxy ->
                        val decision = motionGate.update(imageProxy)
                        lastMotionActive = decision.motionFrame

                        // Update UI mask (optional)
                        activity?.runOnUiThread {
                            fragmentCameraBinding.overlay.setMotionMask(decision.maskBitmap, decision.motionFrame)
                        }

                        if (!decision.motionFrame) {
                            // Reset persistence when motion stops
                            personFilter.reset()
                            poseFilter.reset()

                            imageProxy.close()
                            return@setAnalyzer
                        }

                        // Capture a copy of the current frame for optional pose verification later
                        // (Must do BEFORE passing ownership away)
                        try {
                            val bmp = rgba8888ImageProxyToBitmap(imageProxy)
                            latestPoseBitmap = bmp
                            latestPoseTimestampMs = System.currentTimeMillis()
                        } catch (e: Exception) {
                            Log.w(TAG, "Failed to capture pose bitmap: ${e.message}")
                        }

                        // IMPORTANT: do NOT close imageProxy here.
                        // Pass ownership to ObjectDetectorHelper, which will close it.
                        objectDetectorHelper.detectLivestreamFrame(imageProxy)
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(fragmentCameraBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            fragmentCameraBinding.viewFinder.display.rotation
    }

    // Update UI after objects have been detected. Extracts original image height/width
    // to scale and place bounding boxes properly through OverlayView
    override fun onResults(resultBundle: ObjectDetectorHelper.ResultBundle) {
        if (_fragmentCameraBinding == null) return

        val detectionResult = resultBundle.results[0]

        var bestPersonScore = 0.0f
        for (det in detectionResult.detections()) {
            for (cat in det.categories()) {
                if (cat.categoryName().equals("person", ignoreCase = true)) {
                    if (cat.score() > bestPersonScore) bestPersonScore = cat.score()
                }
            }
        }

        val personThisFrame = bestPersonScore >= 0.80f
        val personPersistent = personFilter.update(personThisFrame)

        val motionActive = lastMotionActive

        // ===== UI updates (run on UI thread) =====
        activity?.runOnUiThread {
            if (_fragmentCameraBinding == null) return@runOnUiThread

            // Status badge should reflect "personThisFrame" visually,
            // but your "confirmed intruder" should rely on personPersistent/pose below.
            when {
                personThisFrame -> {
                    fragmentCameraBinding.tvStatus.text = getString(R.string.status_person)
                    fragmentCameraBinding.tvStatus.setBackgroundResource(R.drawable.bg_oracle_badge_solid)
                }
                motionActive -> {
                    fragmentCameraBinding.tvStatus.text = getString(R.string.status_motion)
                    fragmentCameraBinding.tvStatus.setBackgroundResource(R.drawable.bg_oracle_badge_outline)
                }
                else -> {
                    fragmentCameraBinding.tvStatus.text = getString(R.string.status_idle)
                    fragmentCameraBinding.tvStatus.setBackgroundResource(R.drawable.bg_oracle_badge_outline)
                }
            }

            fragmentCameraBinding.overlay.setPersonActive(personThisFrame)

            if (personThisFrame) {
                fragmentCameraBinding.overlay.setResults(
                    detectionResult,
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    resultBundle.inputImageRotation
                )
            } else {
                fragmentCameraBinding.overlay.clear()
            }
            fragmentCameraBinding.overlay.invalidate()
        }

        // ===== Intruder confirmation pipeline (background thread) =====
        backgroundExecutor.execute {
            val poseEnabled = AppPrefs.isPoseVerificationEnabled(requireContext())

            // If person isn't persistent yet, don't proceed
            if (!personPersistent) {
                poseFilter.reset()
                return@execute
            }

            // Fast path: no pose
            if (!poseEnabled) {
                pingIntruder(bestPersonScore)
                return@execute
            }

            // Pose path (optional toggle)
            val nowMs = System.currentTimeMillis()
            if ((nowMs - lastPoseRunMs) < poseMinIntervalMs) {
                // Do not penalize poseFilter when we didn't run pose
                return@execute
            }

            val bmp = latestPoseBitmap ?: return@execute
            ensurePoseHelper()

            // Build MPImage from bitmap
            val mpImage = com.google.mediapipe.framework.image.BitmapImageBuilder(bmp).build()
            val ts = latestPoseTimestampMs.takeIf { it > 0L } ?: nowMs

            val poseResult = poseHelper?.detectVideo(mpImage, ts)
            lastPoseRunMs = nowMs

            val hasPoseThisCheck = poseResult != null && poseResult.landmarks().isNotEmpty()
            val posePersistent = poseFilter.update(hasPoseThisCheck)

            if (posePersistent) {
                pingIntruder(bestPersonScore)
            }
        }
    }

    private fun pingIntruder(bestPersonScore: Float) {
        val now = System.currentTimeMillis()
        if (now - lastPingMs < pingCooldownMs) return
        lastPingMs = now

        val json = """
        {
          "event_type": "PERSON_DETECTED",
          "timestamp_ms": $now,
          "device_id": "android-phone-01",
          "score": $bestPersonScore
        }
    """.trimIndent()

        Log.d(TAG, "Intruder confirmed -> pinging server: $json")
        intruderClient.sendIntruderEvent(json)
    }


    override fun onError(error: String, errorCode: Int) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            if (errorCode == ObjectDetectorHelper.GPU_ERROR) {
                fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.setSelection(
                    ObjectDetectorHelper.DELEGATE_CPU, false
                )
            }
        }
    }

    private fun rgba8888ImageProxyToBitmap(imageProxy: androidx.camera.core.ImageProxy): android.graphics.Bitmap {
        val width = imageProxy.width
        val height = imageProxy.height
        val plane = imageProxy.planes[0]
        val buffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride // should be 4 for RGBA_8888

        val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)

        buffer.rewind()

        // If rowStride matches width * 4, we can copy directly
        val expectedStride = width * pixelStride
        if (rowStride == expectedStride) {
            bitmap.copyPixelsFromBuffer(buffer)
            return bitmap
        }

        // Otherwise, compact row by row
        val rowBuffer = ByteArray(rowStride)
        val compactBuffer = java.nio.ByteBuffer.allocateDirect(width * height * pixelStride)
        for (row in 0 until height) {
            buffer.get(rowBuffer, 0, rowStride)
            compactBuffer.put(rowBuffer, 0, expectedStride)
        }
        compactBuffer.rewind()
        bitmap.copyPixelsFromBuffer(compactBuffer)
        return bitmap
    }
}
