package com.google.mediapipe.examples.objectdetection.utils;

/**
 * Simple wrapper around SharedPreferences for persisting application settings.
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\n2\u0006\u0010\f\u001a\u00020\rH\u0002J\u000e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0005J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u0014\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0005J\u000e\u0010\u0015\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u0016\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0005J\u000e\u0010\u0017\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u0018\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\rJ\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001a\u001a\u00020\u0005H\u0002J\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\rJ\u0016\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\u001cR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/utils/AppPrefs;", "", "<init>", "()V", "PREFS_NAME", "", "KEY_RED_HOST", "KEY_RED_PORT", "KEY_RED_API_KEY", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "ctx", "Landroid/content/Context;", "getRedHost", "setRedHost", "", "raw", "getRedPort", "", "setRedPort", "getRedApiKey", "setRedApiKey", "getRedBaseUrl", "clearRedOverrides", "normalizeBaseUrl", "input", "isPoseVerificationEnabled", "", "context", "setPoseVerificationEnabled", "enabled", "app_debug"})
public final class AppPrefs {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "oracle_secure_vision_prefs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_RED_HOST = "red_host";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_RED_PORT = "red_port";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_RED_API_KEY = "red_api_key";
    @org.jetbrains.annotations.NotNull()
    public static final com.google.mediapipe.examples.objectdetection.utils.AppPrefs INSTANCE = null;
    
    private AppPrefs() {
        super();
    }
    
    private final android.content.SharedPreferences prefs(android.content.Context ctx) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRedHost(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return null;
    }
    
    public final void setRedHost(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, @org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
    }
    
    public final int getRedPort(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return 0;
    }
    
    public final void setRedPort(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, @org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRedApiKey(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return null;
    }
    
    public final void setRedApiKey(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, @org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRedBaseUrl(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        return null;
    }
    
    public final void clearRedOverrides(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
    }
    
    private final java.lang.String normalizeBaseUrl(java.lang.String input) {
        return null;
    }
    
    public final boolean isPoseVerificationEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final void setPoseVerificationEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean enabled) {
    }
}