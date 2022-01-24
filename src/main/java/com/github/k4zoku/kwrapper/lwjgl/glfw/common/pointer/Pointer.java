package com.github.k4zoku.kwrapper.lwjgl.glfw.common.pointer;

public class Pointer {
    private long pointer;

    public Pointer(long pointer) {
        this.pointer = pointer;
    }

    public final long getPointer() {
        return this.pointer;
    }

    public void setPointer(long pointer) {
        this.pointer = pointer;
    }
}
