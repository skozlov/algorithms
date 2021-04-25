package com.github.skozlov.algorithms.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SortTest {
    @Test
    void insertionSort(){
        test(InsertionSort.INSTANCE);
    }

    private void test(InPlaceSort sort){
        test(sort, new int[0], new int[0]);
        test(sort, new int[]{1}, new int[]{1});
        test(sort, new int[]{1, 2, 3}, new int[]{1, 2, 3});
        test(sort, new int[]{3, 2, 1}, new int[]{1, 2, 3});
        test(sort, new int[]{3, 2, 2, 1}, new int[]{1, 2, 2, 3});
    }

    private void test(InPlaceSort sort, int[] arrayToSort, int[] expectedResult){
        Integer[] array = Arrays.stream(arrayToSort).boxed().toArray(Integer[]::new);
        sort.sortInPlace(array);
        int[] result = Arrays.stream(array).mapToInt(i -> i).toArray();
        assertArrayEquals(expectedResult, result);
    }
}
