package com.github.skozlov.algorithms.sort;

import com.github.skozlov.algorithms.util.Slice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;

import static com.github.skozlov.algorithms.util.ArrayUtils.swap;
import static java.util.Collections.singletonList;

public class QuickSort implements InPlaceSort {
    private static final Random RANDOM = new Random();

    public static final Function<Slice<?>, Integer> PIVOT_FIRST = Slice::getStartIndex;

    public static final Function<Slice<?>, Integer> PIVOT_RANDOM =
            slice -> slice.getStartIndex() + RANDOM.nextInt(slice.getSize());

    private final Function<Slice<?>, Integer> getPivotIndex;

    public QuickSort(Function<Slice<?>, Integer> getPivotIndex) {
        this.getPivotIndex = getPivotIndex;
    }

    public QuickSort(){
        this(getPivotMedianOfNRandom(3));
    }

    @Override
    public <T> void sortInPlace(T[] elements, Comparator<T> comparator) {
        var partStartsReversed = new ArrayList<>(singletonList(0));
        do {
            int startIndex = partStartsReversed.remove(partStartsReversed.size() - 1);
            int endIndexExclusive = partStartsReversed.isEmpty()
                    ? elements.length
                    : partStartsReversed.get(partStartsReversed.size() - 1);
            if (endIndexExclusive - startIndex > 1) {
                int part2Index = partition(new Slice<>(elements, startIndex, endIndexExclusive), comparator);
                partStartsReversed.add(part2Index);
                partStartsReversed.add(startIndex);
            }
        }
        while (!partStartsReversed.isEmpty());
    }

    private <T> int partition(Slice<T> slice, Comparator<T> comparator) {
        var array = slice.getArray();
        int startIndex = slice.getStartIndex();
        int pivotIndex = getPivotIndex.apply(slice);
        swap(array, startIndex, pivotIndex);
        var pivot = array[startIndex];
        int rightIndex = slice.getEndIndexExclusive() - 1;
        int leftIndex = startIndex;
        for (;;) {
            //noinspection StatementWithEmptyBody
            for (; comparator.compare(array[rightIndex], pivot) > 0; rightIndex--);
            //noinspection StatementWithEmptyBody
            for (; comparator.compare(array[leftIndex], pivot) < 0; leftIndex++);
            if (leftIndex < rightIndex) {
                swap(array, leftIndex, rightIndex);
                leftIndex++;
                rightIndex--;
            }
            else {
                return rightIndex + 1;
            }
        }
    }

    public static Function<Slice<?>, Integer> getPivotMedianOfNRandom(int n) {
        return slice -> {
            Integer[] indexes = new Integer[n];
            for (int i = 0; i < n; i++) {
                indexes[i] = PIVOT_RANDOM.apply(slice);
            }
            InsertionSort.INSTANCE.sortInPlace(indexes);
            return indexes[n / 2];
        };
    }
}
