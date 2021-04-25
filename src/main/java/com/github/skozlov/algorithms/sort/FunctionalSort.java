package com.github.skozlov.algorithms.sort;

import java.util.Comparator;

public interface FunctionalSort {
    <T> T[] sort(T[] elements, Class<T> elementType, Comparator<T> comparator);

    default <T extends Comparable<T>> T[] sort(T[] elements, Class<T> elementType) {
        return sort(elements, elementType, Comparator.naturalOrder());
    }
}
