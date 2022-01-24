package com.github.k4zoku.kwrapper.lwjgl.common;

public class Geometry<T extends Number> {

    private final Position<T> position;
    private final Size<T> size;

    public Geometry(T x, T y, T width, T height) {
        this.position = new Position<>(x, y);
        this.size = new Size<>(width, height);
    }

    public Geometry(Position<T> position, Size<T> size) {
        this.position = position;
        this.size = size;
    }

    public T getX() {
        return position.getX();
    }

    public T getY() {
        return position.getY();
    }

    public T getWidth() {
        return size.getWidth();
    }

    public T getHeight() {
        return size.getHeight();
    }

}
