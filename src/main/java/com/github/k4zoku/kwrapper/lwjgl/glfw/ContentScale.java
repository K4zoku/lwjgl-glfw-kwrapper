package com.github.k4zoku.kwrapper.lwjgl.glfw;

public class ContentScale {

    private final float xscale;
    private final float yscale;

    public ContentScale(Float xscale, Float yscale) {
        this.xscale = xscale;
        this.yscale = yscale;
    }

    public float getXscale() {
        return this.xscale;
    }

    public float getYscale() {
        return this.yscale;
    }
}
