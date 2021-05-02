package com.github.skozlov.algorithms.sort;

import java.util.function.Function;

import static com.github.skozlov.algorithms.util.ArrayUtils.newArray;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CountingSort {
    private CountingSort(){
    }

    public static <T> T[] sort(T[] array, Function<T, Integer> getKey, Class<T> elementType) {
        if (array.length == 0) {
            return newArray(elementType, 0);
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (T e : array) {
            int key = getKey.apply(e);
            min = min(min, key);
            max = max(max, key);
        }
        int[] ranks = new int[max - min + 1];
        for (T e : array) {
            ranks[getKey.apply(e) - min]++;
        }
        for (int i = 1; i < ranks.length; i++) {
            ranks[i] += ranks[i-1];
        }
        T[] sorted = newArray(elementType, array.length);
        for (int i = array.length - 1; i >= 0; i--) {
            T e = array[i];
            int rank = ranks[getKey.apply(e) - min]--;
            sorted[rank-1] = e;
        }
        return sorted;
    }
}
