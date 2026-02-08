package com.google.mediapipe.examples.objectdetection;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0018\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u000bH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/YuvToRgbConverter;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "rs", "Landroid/renderscript/RenderScript;", "script", "Landroid/renderscript/ScriptIntrinsicYuvToRGB;", "yuvBuffer", "", "inAlloc", "Landroid/renderscript/Allocation;", "outAlloc", "yuvToRgb", "", "image", "Landroidx/camera/core/ImageProxy;", "output", "Landroid/graphics/Bitmap;", "nv21Size", "", "imageToNv21", "out", "app_debug"})
public final class YuvToRgbConverter {
    @org.jetbrains.annotations.NotNull()
    private final android.renderscript.RenderScript rs = null;
    @org.jetbrains.annotations.NotNull()
    private final android.renderscript.ScriptIntrinsicYuvToRGB script = null;
    @org.jetbrains.annotations.Nullable()
    private byte[] yuvBuffer;
    @org.jetbrains.annotations.Nullable()
    private android.renderscript.Allocation inAlloc;
    @org.jetbrains.annotations.Nullable()
    private android.renderscript.Allocation outAlloc;
    
    public YuvToRgbConverter(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void yuvToRgb(@org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageProxy image, @org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap output) {
    }
    
    private final int nv21Size(androidx.camera.core.ImageProxy image) {
        return 0;
    }
    
    private final void imageToNv21(androidx.camera.core.ImageProxy image, byte[] out) {
    }
}