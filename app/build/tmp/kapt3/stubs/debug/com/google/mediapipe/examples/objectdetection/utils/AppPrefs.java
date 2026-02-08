package com.google.mediapipe.examples.objectdetection.utils;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0005\u00a8\u0006\u000b"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/utils/AppPrefs;", "", "<init>", "()V", "isPoseVerificationEnabled", "", "context", "Landroid/content/Context;", "setPoseVerificationEnabled", "", "enabled", "app_debug"})
public final class AppPrefs {
    @org.jetbrains.annotations.NotNull()
    public static final com.google.mediapipe.examples.objectdetection.utils.AppPrefs INSTANCE = null;
    
    private AppPrefs() {
        super();
    }
    
    public final boolean isPoseVerificationEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final void setPoseVerificationEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean enabled) {
    }
}