package com.github.skozlov.algorithms.sort;

import java.util.Comparator;

public class InsertionSort implements InPlaceSort {
    public static final InsertionSort INSTANCE = new InsertionSort();

    @Override
    public <T> void sortInPlace(T[] elements, Comparator<T> comparator) {
        for (int insertedIndex = 1; insertedIndex < elements.length; insertedIndex++) {
            T toInsert = elements[insertedIndex];
            int targetIndex = insertedIndex;
            for (; targetIndex > 0; targetIndex--) {
                T toShift = elements[targetIndex - 1];
                if (comparator.compare(toShift, toInsert) > 0) {
                    elements[targetIndex] = toShift;
                }
                else {
                    break;
                }
            }
            elements[targetIndex] = toInsert;
        }
    }
}
