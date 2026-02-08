package com.google.mediapipe.examples.objectdetection.fragments;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.google.mediapipe.examples.objectdetection.R;

public class CameraFragmentDirections {
  private CameraFragmentDirections() {
  }

  @CheckResult
  @NonNull
  public static NavDirections actionCameraToPermissions() {
    return new ActionOnlyNavDirections(R.id.action_camera_to_permissions);
  }
}
