package com.github.skozlov.algorithms.sort;

import com.github.skozlov.algorithms.util.ArrayUtils;

import java.util.Comparator;

public interface InPlaceSort {
    <T> void sortInPlace(T[] elements, Comparator<T> comparator);

    default FunctionalSort toFunctional() {
        return new FunctionalSort() {
            @Override
            public <T> T[] sort(T[] elements, Class<T> elementType, Comparator<T> comparator) {
                T[] copy = ArrayUtils.copy(elements, elementType);
                InPlaceSort.this.sortInPlace(copy, comparator);
                return copy;
            }
        };
    }
}
