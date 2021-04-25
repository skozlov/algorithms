package com.github.skozlov.algorithms.sort;

import com.github.skozlov.algorithms.util.ArrayUtils;
import com.github.skozlov.algorithms.util.Slice;

import java.util.Comparator;

import static java.lang.Math.min;

public class MergeSort implements FunctionalSort {
    public static final MergeSort INSTANCE = new MergeSort();

    @Override
    public <T> T[] sort(T[] elements, Class<T> elementType, Comparator<T> comparator) {
        T[] source = ArrayUtils.copy(elements, elementType);
        T[] target = ArrayUtils.create(elementType, elements.length);
        for (int partSize = 1; partSize < elements.length; partSize *= 2) {
            for (int leftPartIndex = 0; leftPartIndex < elements.length; leftPartIndex += partSize * 2) {
                int rightPartIndex = min(leftPartIndex + partSize, elements.length);
                var leftPart = new Slice<>(source, leftPartIndex, rightPartIndex);
                var rightPart = new Slice<>(source, rightPartIndex, min(rightPartIndex + partSize, elements.length));
                merge(leftPart, rightPart, target, leftPartIndex, comparator);
            }
            var x = source;
            source = target;
            target = x;
        }
        return source;
    }

    private static <T> void merge(
            Slice<T> part1,
            Slice<T> part2,
            T[] target,
            int targetStartIndex,
            Comparator<T> comparator
    ) {
        int targetIndex = targetStartIndex;
        for (; !part1.isEmpty() && !part2.isEmpty(); targetIndex++) {
            T head1 = part1.getHead();
            T head2 = part2.getHead();
            if (comparator.compare(head1, head2) <= 0) {
                target[targetIndex] = head1;
                part1 = part1.tail();
            } else {
                target[targetIndex] = head2;
                part2 = part2.tail();
            }
        }
        part1.copyTo(target, targetIndex);
        part2.copyTo(target, targetIndex);
    }
}
