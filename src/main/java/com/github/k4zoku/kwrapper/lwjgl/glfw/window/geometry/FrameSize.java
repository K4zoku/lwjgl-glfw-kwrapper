package com.github.k4zoku.kwrapper.lwjgl.glfw.window.geometry;

public class FrameSize {

    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public FrameSize(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
