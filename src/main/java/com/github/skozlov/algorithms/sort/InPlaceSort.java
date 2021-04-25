package com.github.skozlov.algorithms.sort;

import java.util.Comparator;

public interface InPlaceSort {
    <T> void sortInPlace(T[] elements, Comparator<T> comparator);

    default <T extends Comparable<T>> void sortInPlace(T[] elements) {
        sortInPlace(elements, Comparator.naturalOrder());
    }
}
