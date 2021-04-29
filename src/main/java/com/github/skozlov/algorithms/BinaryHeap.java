package com.github.skozlov.algorithms;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.skozlov.algorithms.util.ListUtils.swap;

public class BinaryHeap<T> implements PriorityQueue<T> {
    private final List<T> elements;
    private final Comparator<T> comparator;

    @SafeVarargs
    public BinaryHeap(Comparator<T> comparator, T... elements) {
        this.comparator = comparator;
        this.elements = new ArrayList<>(Arrays.asList(elements));
        build(this.elements, comparator);
    }

    public List<T> toList(){
        return elements;
    }

    @Override
    public void add(T t) {
        elements.add(t);
        floatUp(elements, elements.size() - 1, comparator);
    }

    @Override
    public Optional<T> getMax(){
        return elements.isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
    }

    @Override
    public Optional<T> removeMax() {
        var max = getMax();
        max.ifPresent(__ -> {
            if (elements.size() == 1) {
                elements.clear();
            }
            else {
                elements.set(0, elements.remove(elements.size() - 1));
                sink(elements, 0, comparator);
            }
        });
        return max;
    }

    private static <T> void build(List<T> elements, Comparator<T> comparator) {
        int maxInnerNodeNumber = elements.size() / 2;
        for (int index = maxInnerNodeNumber - 1; index >= 0; index--) {
            sink(elements, index, comparator);
        }
    }

    private static <T> void sink(List<T> elements, int index, Comparator<T> comparator) {
        int elementNumber = index + 1;
        int leftChildIndex = elementNumber * 2 - 1;
        var rightChildIndex = leftChildIndex + 1;
        var childIndexes =
                Stream.of(leftChildIndex, rightChildIndex)
                        .filter(i -> i < elements.size())
                        .collect(Collectors.toSet());
        Comparator<Integer> indexComparator = Comparator.comparing(elements::get, comparator);
        var maxChildIndex = childIndexes.stream().max(indexComparator);
        maxChildIndex.ifPresent(childIndex -> {
            if (indexComparator.compare(index, childIndex) < 0) {
                swap(elements, index, childIndex);
                sink(elements, childIndex, comparator);
            }
        });
    }

    private static <T> void floatUp(List<T> elements, int index, Comparator<T> comparator) {
        if (index == 0) {
            return;
        }
        int elementNumber = index + 1;
        int parentIndex = elementNumber / 2 - 1;
        if (Comparator.comparing(elements::get, comparator).compare(index, parentIndex) > 0) {
            swap(elements, index, parentIndex);
            floatUp(elements, parentIndex, comparator);
        }
    }
}
