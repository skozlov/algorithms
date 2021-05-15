package com.github.skozlov.algorithms.sort;

import java.util.function.Function;

public class RadixSort {
    private RadixSort() {
    }

    public static <T> T[] sort(T[] array, Function<T, Integer> getKey, Class<T> elementType) {
        T[] sorted = null;
        for (int shift = 0; shift < Integer.SIZE; shift++) {
            T[] toSort = sorted == null ? array : sorted;
            int finalShift = shift;
            sorted = CountingSort.sort(toSort, getKey.andThen(key -> (key >> finalShift) & 1), elementType);
        }
        return sorted;
    }
}
