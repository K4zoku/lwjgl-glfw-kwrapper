package com.github.k4zoku.kwrapper.lwjgl.common.geometry;

public class Size<T extends Number> {
    private final T width;
    private final T height;

    public Size(T width, T height) {
        this.width = width;
        this.height = height;
    }

    public T getWidth() {
        return this.width;
    }

    public T getHeight() {
        return this.height;
    }

}
