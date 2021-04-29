package com.github.skozlov.algorithms;

import java.util.Optional;

public interface PriorityQueue<T> {
    void add(T t);
    Optional<T> getMax();
    Optional<T> removeMax();
}
