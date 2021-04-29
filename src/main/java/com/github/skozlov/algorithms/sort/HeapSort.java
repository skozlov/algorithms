package com.github.skozlov.algorithms.sort;

import com.github.skozlov.algorithms.BinaryHeap;
import com.github.skozlov.algorithms.util.ArrayUtils;

import java.util.Comparator;

public class HeapSort implements FunctionalSort {
    public static final HeapSort INSTANCE = new HeapSort();

    @Override
    public <T> T[] sort(T[] elements, Class<T> elementType, Comparator<T> comparator) {
        var heap = new BinaryHeap<>(comparator, elements);
        var sorted = ArrayUtils.create(elementType, elements.length);
        for (int i = sorted.length - 1; i >= 0; i--) {
            //noinspection OptionalGetWithoutIsPresent
            sorted[i] = heap.removeMax().get();
        }
        return sorted;
    }
}
