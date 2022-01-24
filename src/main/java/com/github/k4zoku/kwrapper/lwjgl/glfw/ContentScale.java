package com.github.k4zoku.kwrapper.lwjgl.glfw;

import com.github.k4zoku.kwrapper.lwjgl.common.pair.UnaryPair;

public class ContentScale extends UnaryPair<Float> {

    private final float xscale;
    private final float yscale;

    public ContentScale(Float key, Float value) {
        super(key, value);
        this.xscale = key;
        this.yscale = value;
    }

    public float getXscale() {
        return this.xscale;
    }

    public float getYscale() {
        return this.yscale;
    }
}
