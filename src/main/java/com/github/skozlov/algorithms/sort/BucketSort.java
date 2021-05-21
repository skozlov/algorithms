package com.github.skozlov.algorithms.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.github.skozlov.algorithms.util.ArrayUtils.copy;
import static com.github.skozlov.algorithms.util.ArrayUtils.newArray;
import static java.lang.Math.*;

public class BucketSort {
    public <T> T[] sort(T[] array, Function<T, Double> getKey, Class<T> elementType) {
        T[] emptyArray = newArray(elementType, 0);
        if (array.length == 0) {
            return emptyArray;
        }
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (T e : array) {
            double key = getKey.apply(e);
            min = min(min, key);
            max = max(max, key);
        }
        if (min == max) {
            return copy(array, elementType);
        }
        @SuppressWarnings("unchecked")
        List<T>[] buckets =
                Stream.generate(() -> new ArrayList<T>(1))
                        .limit(array.length)
                        .toArray(len -> newArray(List.class, len));
        double bucketSize = (max - min) / array.length;
        for (T e : array) {
            double key = getKey.apply(e);
            int i = min((int) floor((key - min) / bucketSize), array.length - 1);
            buckets[i].add(e);
        }
        T[] result = newArray(elementType, array.length);
        int i = 0;
        var bucketElementsSort = getBucketElementsSort();
        for (var bucket : buckets) {
            T[] bucketElements = bucket.toArray(emptyArray);
            bucketElementsSort.sortInPlace(bucketElements, Comparator.comparing(getKey));
            System.arraycopy(bucketElements, 0, result, i, bucketElements.length);
            i += bucketElements.length;
        }
        return result;
    }

    protected InPlaceSort getBucketElementsSort() {
        return new InsertionSort();
    }
}
