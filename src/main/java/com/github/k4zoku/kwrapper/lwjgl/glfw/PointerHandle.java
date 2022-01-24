package com.github.k4zoku.kwrapper.lwjgl.glfw;

public class PointerHandle {
    private long handle;

    public PointerHandle(long handle) {
        this.handle = handle;
    }

    public final long getHandle() {
        return this.handle;
    }

    public void setHandle(long handle) {
        this.handle = handle;
    }
}
