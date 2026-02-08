package com.google.mediapipe.examples.objectdetection.utils

import android.content.Context
import com.google.mediapipe.examples.objectdetection.SettingsKeys

object AppPrefs {
    fun isPoseVerificationEnabled(context: Context): Boolean {
        val sp = context.getSharedPreferences(SettingsKeys.PREFS_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(SettingsKeys.KEY_USE_POSE_VERIFICATION, false)
    }

    fun setPoseVerificationEnabled(context: Context, enabled: Boolean) {
        val sp = context.getSharedPreferences(SettingsKeys.PREFS_NAME, Context.MODE_PRIVATE)
        sp.edit().putBoolean(SettingsKeys.KEY_USE_POSE_VERIFICATION, enabled).apply()
    }
}
