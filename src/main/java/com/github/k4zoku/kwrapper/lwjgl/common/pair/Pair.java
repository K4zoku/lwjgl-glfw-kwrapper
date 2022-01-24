package com.github.k4zoku.kwrapper.lwjgl.common.pair;

public class Pair<K, V> {

    protected final K key;
    protected final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

}
