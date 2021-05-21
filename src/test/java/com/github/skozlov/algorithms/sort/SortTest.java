package com.github.skozlov.algorithms.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SortTest {
    @Test
    void insertionSort(){
        test(InsertionSort.INSTANCE);
    }

    @Test
    void mergeSort(){
        test(MergeSort.INSTANCE);
    }

    @Test
    void heapSort(){
        test(HeapSort.INSTANCE);
    }

    @Test
    void quickSort(){
        test(new QuickSort(QuickSort.PIVOT_FIRST));
        test(new QuickSort(QuickSort.PIVOT_RANDOM));
        test(new QuickSort());
    }

    @Test
    void countingSort() {
        testIntSort(array -> CountingSort.sort(array, Function.identity(), Integer.class));
    }

    @Test
    void radixSort() {
        testIntSort(array -> RadixSort.sort(array, Function.identity(), Integer.class));
    }

    @Test
    void bucketSort() {
        testDoubleSort(array -> new BucketSort().sort(array, Function.identity(), Double.class));
    }

    private void test(InPlaceSort sort) {
        test(sort.toFunctional());
    }

    private void test(FunctionalSort sort) {
        testIntSort(array -> sort.sort(array, Integer.class));
    }

    private void testIntSort(Function<Integer[], Integer[]> sort){
        test(sort, new int[0], new int[0]);
        test(sort, new int[]{1}, new int[]{1});
        test(sort, new int[]{1, 2, 3}, new int[]{1, 2, 3});
        test(sort, new int[]{3, 2, 1}, new int[]{1, 2, 3});
        test(sort, new int[]{3, 2, 2, 1}, new int[]{1, 2, 2, 3});
        test(sort, new int[]{1, 2, 1}, new int[]{1, 1, 2});
    }

    private void testDoubleSort(Function<Double[], Double[]> sort){
        test(sort, new double[0], new double[0]);
        test(sort, new double[]{1}, new double[]{1});
        test(sort, new double[]{1, 2, 3}, new double[]{1, 2, 3});
        test(sort, new double[]{3, 2, 1}, new double[]{1, 2, 3});
        test(sort, new double[]{3, 2, 2, 1}, new double[]{1, 2, 2, 3});
        test(sort, new double[]{1, 2, 1}, new double[]{1, 1, 2});
    }

    private void test(Function<Integer[], Integer[]> sort, int[] arrayToSort, int[] expectedResult){
        Integer[] boxed = Arrays.stream(arrayToSort).boxed().toArray(Integer[]::new);
        int[] result = Arrays.stream(sort.apply(boxed)).mapToInt(i -> i).toArray();
        assertArrayEquals(expectedResult, result);
    }

    private void test(Function<Double[], Double[]> sort, double[] arrayToSort, double[] expectedResult){
        Double[] boxed = Arrays.stream(arrayToSort).boxed().toArray(Double[]::new);
        double[] result = Arrays.stream(sort.apply(boxed)).mapToDouble(d -> d).toArray();
        assertArrayEquals(expectedResult, result);
    }
}
