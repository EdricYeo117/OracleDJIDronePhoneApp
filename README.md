# Oracle DJI Drone Phone App - Object Detection with Motion Analysis

## Overview

This Android application integrates real-time object detection with advanced motion detection capabilities for drone-based surveillance and monitoring. The app uses MediaPipe's object detection models combined with background subtraction techniques to identify and track objects while filtering out static background elements.

### Key Features

- **Real-time Object Detection**: Powered by MediaPipe Tasks with support for multiple pre-trained models
- **Background Subtraction**: Custom motion detection using exponential moving average (EMA) for background modeling
- **Motion-Triggered Analysis**: Smart triggering system that reduces false positives from camera shake and lighting changes
- **REST API Integration** _(Planned)_: Remote control and data streaming capabilities for drone coordination
- **Multi-Source Input**: Support for live camera feed, gallery images, and video files
- **Flexible Model Selection**: Choose from MobileNetV2, EfficientDet Lite 0, or EfficientDet Lite 2

![Object Detection Demo](object_detection.gif?raw=true "Object Detection Demo")

---

## Architecture

### Core Components

#### 1. **Object Detection Engine**

- **Framework**: MediaPipe Tasks Vision
- **Models Available**:
  - [MobileNetV2 SSD](https://storage.cloud.google.com/tf_model_garden/vision/qat/mobilenetv2_ssd_coco/mobilenetv2_ssd_256_uint8.tflite) - Optimized for speed
  - [EfficientDet Lite 0](https://storage.googleapis.com/mediapipe-tasks/object_detector/efficientdet_lite0_uint8.tflite) - Balanced performance
  - [EfficientDet Lite 2](https://storage.googleapis.com/mediapipe-tasks/object_detector/efficientdet_lite2_uint8.tflite) - Higher accuracy
- **Output**: Bounding boxes, object classes, confidence scores

#### 2. **Background Subtraction System**

The `MotionGate` class implements sophisticated background subtraction using:

- **Exponential Moving Average (EMA)**: Adaptive background model that adjusts to gradual lighting changes
- **Downsampled Processing**: Configurable downsampling (default 4x) for performance optimization
- **Smart Filtering**:
  - Pixel-level difference thresholding
  - Global change detection to ignore lighting shifts
  - Consecutive frame validation to reduce noise
  - Configurable cooldown period between triggers

**Configuration Parameters**:

```kotlin
downsampleStep: Int = 4        // Sampling interval (2-8 typical)
alpha: Float = 0.05f           // EMA update rate (0.02-0.10)
diffThreshold: Int = 18        // Pixel difference threshold (10-30)
ratioThreshold: Float = 0.02f  // Changed pixel ratio (0.01-0.06)
minConsecutive: Int = 3        // Required motion frames
cooldownMs: Long = 10_000L     // Minimum time between triggers
globalChangeIgnoreRatio: Float = 0.60f  // Ignore exposure shifts
warmupFrames: Int = 15         // Background stabilization period
```

#### 3. **REST API Integration** _(In Development)_

Planned features for remote control and data streaming:

- **Endpoints**:
  - `POST /api/detection/start` - Start detection session
  - `POST /api/detection/stop` - Stop detection session
  - `GET /api/detection/results` - Retrieve detection results
  - `GET /api/motion/status` - Get motion detection status
  - `POST /api/config` - Update detection parameters
  - `WS /api/stream` - WebSocket for real-time video/data streaming

- **Authentication**: Token-based authentication for secure drone control
- **Data Format**: JSON responses with detection metadata and base64-encoded frames

---

## Technical Stack

### Android Platform

- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 34 (Android 14)
- **Language**: Kotlin
- **Build System**: Gradle

### Key Dependencies

```gradle
// Camera
androidx.camera:camera-core:1.4.2
androidx.camera:camera-camera2:1.4.2
androidx.camera:camera-lifecycle:1.4.2

// MediaPipe
com.google.mediapipe:tasks-vision:0.10.29

// Navigation
androidx.navigation:navigation-fragment-ktx:2.3.5

// UI
com.google.android.material:material:1.0.0
```

---

## Getting Started

### Prerequisites

- **[Android Studio](https://developer.android.com/studio/index.html)** (Tested on Android Studio Dolphin or later)
- Physical Android device with:
  - Minimum Android 7.0 (SDK 24)
  - Camera hardware
  - Developer mode enabled
- Alternatively, use an Android emulator for gallery-based testing (limited functionality)

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/EdricYeo117/OracleDJIDronePhoneApp.git
   cd OracleDJIDronePhoneApp
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and click OK
   - Trust the project when prompted

3. **Sync Dependencies**
   - Android Studio will automatically trigger a Gradle sync
   - If not, click "File > Sync Project with Gradle Files"
   - Model files will be automatically downloaded via `download_models.gradle`

4. **Connect Device & Run**
   - Enable USB debugging on your Android device
   - Connect via USB
   - Click the green "Run" button in Android Studio
   - Select your device from the deployment target

### Using Custom Models

To use your own TFLite models:

1. Place `.tflite` files in `app/src/main/assets/`
2. Comment out the auto-download in `app/build.gradle`:
   ```gradle
   // apply from:'download_models.gradle'
   ```
3. Update model references in `ObjectDetectorHelper.kt`

---

## Usage

### Camera Mode

- **Live Detection**: Real-time object detection with motion filtering
- **Motion Indicator**: Visual feedback when motion is detected
- **Adjustable Settings**:
  - Detection threshold
  - Max results
  - Model selection
  - Delegate (CPU/GPU)

### Gallery Mode

- Import images or videos from device storage
- Process pre-recorded content
- Useful for testing without physical camera

### Motion Detection Workflow

1. **Initialization**: Background model builds for first 15 frames (warmup)
2. **Background Modeling**: EMA continuously adapts to gradual changes
3. **Motion Detection**: Compares current frame to background model
4. **Validation**: Requires 3+ consecutive motion frames to trigger
5. **Cooldown**: 10-second pause between triggers to prevent spam
6. **Object Detection**: Triggered only when motion is validated

---

## Planned Features

### REST API Integration

- **Remote Configuration**: Adjust detection parameters via HTTP
- **Live Streaming**: WebSocket-based video feed transmission
- **Result Retrieval**: Query detection history and analytics
- **Multi-Device Coordination**: Synchronize multiple drone cameras

### Additional Enhancements

- **DJI SDK Integration**: Direct drone camera control
- **Cloud Storage**: Automatic upload of detection events
- **Advanced Analytics**: Object tracking across frames
- **Geolocation Tagging**: GPS coordinates for detections
- **Alert System**: Push notifications for specific objects

---

## Project Structure & File Descriptions

### Application Entry Points

- **`MainActivity.kt`**: The single-activity entry point for the application. It sets up the navigation host and handles top-level UI binding.
- **`MainViewModel.kt`**: A shared ViewModel that persists object detection settings (threshold, max results, delegate type, model type) across configuration changes and fragments.

### Fragments (UI & Logic)

- **`CameraFragment.kt`**: The core screen for live object detection.
  - Manages CameraX preview and image analysis use cases.
  - Integrates `MotionGate` for background subtraction.
  - Handles the "Intruder" confirmation logic using `personFilter`, `poseFilter`, and `PoseLandmarkerHelper`.
  - Updates the overlay and status indicators in real-time.
- **`GalleryFragment.kt`**: Allows users to run object detection on static images or video files from their device storage.
- **`PermissionsFragment.kt`**: Handles runtime camera permission requests before navigating to the camera screen.

### Core Detection Logic

- **`ObjectDetectorHelper.kt`**: A wrapper around MediaPipe's `ObjectDetector`.
  - Handles initialization of the TFLite model.
  - Provides methods for live stream, image, and video inference.
  - Manages hardware delegates (CPU/GPU).
- **`PoseLandmarkerHelper.kt`**: A wrapper around MediaPipe's `PoseLandmarker`.
  - Used for secondary verification of "person" detections to reduce false positives.
  - Detects 33 body landmarks on a timestamped image.
- **`IntruderApiClient.kt`**: A Retrofit-style HTTP client (using OkHttp) for sending "Intruder Alert" events to a remote server.
- **`MotionGate.kt`**: Custom motion detection logic.
  - Uses an Exponential Moving Average (EMA) to model the background.
  - Compares the current frame against the background to detect changes.
  - Filters out global lighting changes and noise to trigger detection only on significant motion.

### Utilities

- **`OverlayView.kt`**: A custom `View` that draws bounding boxes, classification labels, and pose skeletons on top of the camera preview.
- **`YuvToRgbConverter.kt`**: Uses RenderScript to efficiently convert YUV_420_888 camera buffers to ARGB_8888 Bitmaps for processing.
- **`BooleanWindowFilter.kt`**: A sliding window utility that returns `true` only if a condition is met `minHits` times within the last `windowSize` frames. Used for temporal consistency.
- **`NetworkUtils.kt`**: Helper functions to retrieve the device's IP address and active network transport type (WiFi/Cellular).
- **`AppPrefs.kt`**: Simple `SharedPreferences` wrapper for persisting toggle states (e.g., "Increased Accuracy" mode).
- **`ImageProxyUtils.kt`**: Helpers for converting CameraX `ImageProxy` objects to `Bitmap`s, handling rotation and mirroring.

---

## Detailed Component Documentation

### Intruder Detection API (`IntruderApiClient`)

The app can send JSON events to a remote server when a person is confirmed.

- **Endpoint**: `POST /v1/intrusion/events`
- **Payload**:
  ```json
  {
    "event_type": "PERSON_DETECTED",
    "timestamp_ms": 1707423984000,
    "device_id": "android-phone-01",
    "score": 0.92
  }
  ```

### Motion Gate Logic (`MotionGate`)

To save power and reduce false positives, the heavy object detection model is only triggered when "motion" is detected.

1. **Downsampling**: Frames are downsampled (default 6x) to speed up pixel comparison.
2. **EMA Background**: `bg[i] = bg[i] + alpha * (current[i] - bg[i])`. This constantly adapts to slow lighting changes.
3. **Diff Threshold**: Pixels differing by more than `diffThreshold` (default 28) are marked as "changed".
4. **Global Change Rejection**: If >60% of pixels change at once (e.g., turning on a light), it is ignored as a global shift.
5. **Trigger**: If the ratio of changed pixels exceeds `ratioThreshold` (3%) for `minConsecutive` frames (3), motion is flagged.

---

## Performance Optimization

### Background Subtraction Tuning

For different scenarios, adjust `MotionGate` parameters:

**High-Speed Drone Flight** (reduce false negatives):

```kotlin
diffThreshold = 15
ratioThreshold = 0.015f
minConsecutive = 2
```

**Stable Surveillance** (reduce false positives):

```kotlin
diffThreshold = 25
ratioThreshold = 0.04f
minConsecutive = 5
```

**Low-Light Conditions** (adapt to noise):

```kotlin
alpha = 0.08f  // Faster adaptation
diffThreshold = 25
globalChangeIgnoreRatio = 0.70f
```

---

## Contributing

We welcome contributions! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is based on the TensorFlow/MediaPipe examples and is licensed under the Apache License 2.0.

```
Copyright 2022 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## Acknowledgments

- **MediaPipe Team** - For the object detection framework
- **TensorFlow Team** - For pre-trained models
- **DJI** - For drone SDK and platform
- **Contributors** - Community improvements and bug reports

---

## Support & Contact

For issues, questions, or feature requests:

- **GitHub Issues**: [Report a bug](https://github.com/EdricYeo117/OracleDJIDronePhoneApp/issues)
- **Documentation**: [Wiki](https://github.com/EdricYeo117/OracleDJIDronePhoneApp/wiki)

---

**Status**: Active Development | **Version**: 1.0.0 | **Last Updated**: February 2026
