package com.google.mediapipe.examples.objectdetection;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rH\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0016\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0017R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/google/mediapipe/examples/objectdetection/YuvToRgbConverter;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "inAlloc", "Landroid/renderscript/Allocation;", "outAlloc", "rs", "Landroid/renderscript/RenderScript;", "script", "Landroid/renderscript/ScriptIntrinsicYuvToRGB;", "yuvBuffer", "", "imageToNv21", "", "image", "Landroidx/camera/core/ImageProxy;", "out", "nv21Size", "", "yuvToRgb", "output", "Landroid/graphics/Bitmap;", "app_debug"})
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