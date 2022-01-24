package com.github.k4zoku.kwrapper.lwjgl.common;

public class Position<T extends Number> {
    private final T x;
    private final T y;

    public Position(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}
