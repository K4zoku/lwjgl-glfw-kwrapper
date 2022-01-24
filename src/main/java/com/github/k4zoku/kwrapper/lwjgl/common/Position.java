package com.github.k4zoku.kwrapper.lwjgl.common;

import com.github.k4zoku.kwrapper.lwjgl.common.pair.UnaryPair;

public class Position<T extends Number> extends UnaryPair<T> {
    private final T x;
    private final T y;

    public Position(T x, T y) {
        super(x, y);
        this.x = key;
        this.y = value;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}
