package com.github.skozlov.algorithms;

import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryHeapTest {
    @Test
    void constructor(){
        Map<List<Integer>, List<Integer>> sourceToExpectedResult = Map.of(
                emptyList(), emptyList(),
                singletonList(0), singletonList(0),
                asList(2, 1), asList(2, 1),
                asList(1, 2), asList(2, 1),
                asList(1, 2, 3, 4, 5, 6, 7), asList(7, 5, 6, 4, 2, 1, 3),
                asList(7, 6, 5, 4, 3, 2, 1), asList(7, 6, 5, 4, 3, 2, 1)
        );
        sourceToExpectedResult.forEach((source, expectedResult) ->
                assertEquals(
                        expectedResult,
                        new BinaryHeap<>(Comparator.naturalOrder(), source.toArray(new Integer[0])).toList()
                )
        );
    }

    @Test
    void add(){
        var heap = new BinaryHeap<Integer>(Comparator.naturalOrder());
        assertEquals(emptyList(), heap.toList());

        heap.add(1);
        assertEquals(singletonList(1), heap.toList());

        heap.add(0);
        assertEquals(asList(1, 0), heap.toList());

        heap.add(3);
        assertEquals(asList(3, 0, 1), heap.toList());

        heap.add(2);
        assertEquals(asList(3, 2, 1, 0), heap.toList());

        heap.add(4);
        assertEquals(asList(4, 3, 1, 0, 2), heap.toList());
    }

    @Test
    void getMax(){
        assertEquals(Optional.empty(), new BinaryHeap<Integer>(Comparator.naturalOrder()).getMax());
        assertEquals(Optional.of(7), new BinaryHeap<>(Comparator.naturalOrder(), 1, 2, 3, 4, 5, 6, 7).getMax());
    }

    @Test
    void removeMax(){
        var heap = new BinaryHeap<Integer>(Comparator.naturalOrder());
        assertEquals(Optional.empty(), heap.removeMax());
        assertEquals(emptyList(), heap.toList());

        heap = new BinaryHeap<>(Comparator.naturalOrder(), 7, 6, 5, 4, 3, 2, 1);
        assertEquals(Optional.of(7), heap.removeMax());
        assertEquals(asList(6, 4, 5, 1, 3, 2), heap.toList());
    }
}
