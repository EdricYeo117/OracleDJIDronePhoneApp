package com.google.mediapipe.examples.objectdetection.utils

import android.content.Context
import com.google.mediapipe.examples.objectdetection.R
import com.google.mediapipe.examples.objectdetection.utils.SettingsKeys
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/**
 * Simple wrapper around SharedPreferences for persisting application settings.
 */
object AppPrefs {
    private const val PREFS_NAME = "oracle_secure_vision_prefs"

    private const val KEY_RED_HOST = "red_host"
    private const val KEY_RED_PORT = "red_port"
    private const val KEY_RED_API_KEY = "red_api_key"

    private fun prefs(ctx: Context) =
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getRedHost(ctx: Context): String {
        val def = ctx.getString(R.string.red_host).trim()
        val v = prefs(ctx).getString(KEY_RED_HOST, null)?.trim().orEmpty()
        return if (v.isBlank()) def else v
    }

    fun setRedHost(ctx: Context, raw: String) {
        val v = raw.trim()
        val e = prefs(ctx).edit()
        if (v.isBlank()) e.remove(KEY_RED_HOST) else e.putString(KEY_RED_HOST, v)
        e.apply()
    }

    fun getRedPort(ctx: Context): Int {
        val def = ctx.getString(R.string.red_port).trim().toIntOrNull() ?: 8080
        val v = prefs(ctx).getInt(KEY_RED_PORT, -1)
        return if (v <= 0) def else v
    }

    fun setRedPort(ctx: Context, raw: String) {
        val s = raw.trim()
        val e = prefs(ctx).edit()
        if (s.isBlank()) {
            e.remove(KEY_RED_PORT).apply()
            return
        }
        val p = s.toIntOrNull()
        if (p == null || p !in 1..65535) {
            // invalid port -> don't save garbage
            return
        }
        e.putInt(KEY_RED_PORT, p).apply()
    }

    fun getRedApiKey(ctx: Context): String {
        val def = ctx.getString(R.string.red_api_key).trim()
        val v = prefs(ctx).getString(KEY_RED_API_KEY, null)?.trim().orEmpty()
        return if (v.isBlank()) def else v
    }

    fun setRedApiKey(ctx: Context, raw: String) {
        val v = raw.trim()
        val e = prefs(ctx).edit()
        if (v.isBlank()) e.remove(KEY_RED_API_KEY) else e.putString(KEY_RED_API_KEY, v)
        e.apply()
    }

    fun getRedBaseUrl(ctx: Context): String {
        val scheme = ctx.getString(R.string.red_scheme).trim().ifBlank { "http" }
        val host = getRedHost(ctx)
        val port = getRedPort(ctx)
        return "$scheme://$host:$port"
    }

    fun clearRedOverrides(ctx: Context) {
        prefs(ctx).edit()
            .remove(KEY_RED_HOST)
            .remove(KEY_RED_PORT)
            .remove(KEY_RED_API_KEY)
            .apply()
    }

    private fun normalizeBaseUrl(input: String): String? {
        val s = input.trim()
        if (s.isEmpty()) return null
        val withScheme =
            if (s.startsWith("http://") || s.startsWith("https://")) s else "http://$s"

        val url = withScheme.toHttpUrlOrNull() ?: return null
        // keep scheme/host/port; drop trailing slash
        return url.newBuilder().encodedPath("/").build().toString().trimEnd('/')
    }

    fun isPoseVerificationEnabled(context: Context): Boolean {
        val sp = context.getSharedPreferences(SettingsKeys.PREFS_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(SettingsKeys.KEY_USE_POSE_VERIFICATION, false)
    }

    fun setPoseVerificationEnabled(context: Context, enabled: Boolean) {
        val sp = context.getSharedPreferences(SettingsKeys.PREFS_NAME, Context.MODE_PRIVATE)
        sp.edit().putBoolean(SettingsKeys.KEY_USE_POSE_VERIFICATION, enabled).apply()
    }
}
