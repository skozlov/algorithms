package com.github.skozlov.algorithms.util;

import java.util.NoSuchElementException;

import static java.lang.System.arraycopy;

public class Slice<T> {
    private final T[] array;
    private final int startIndex;
    private final int endIndexExclusive;

    public Slice(T[] array, int startIndex, int endIndexExclusive) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndexExclusive = endIndexExclusive;
    }

    public int getSize() {
        return endIndexExclusive - startIndex;
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    public T getHead() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty slice does not have a head");
        }
        return array[startIndex];
    }

    public Slice<T> tail() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty slice does not have a tail");
        }
        return new Slice<>(array, startIndex + 1, endIndexExclusive);
    }

    public void copyTo(T[] target, int targetIndex) {
        arraycopy(array, startIndex, target, targetIndex, getSize());
    }
}
