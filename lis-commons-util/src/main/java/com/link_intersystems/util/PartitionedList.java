package com.link_intersystems.util;

import java.util.AbstractList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * An unmodifiable partitioned perspective on a {@link List}.
 *
 * @param <T>
 */
public class PartitionedList<T> extends AbstractList<List<T>> {

    private final List<T> list;
    private final int partitionSize;

    public PartitionedList(List<T> list, int partitionSize) {
        if (partitionSize < 1) {
            throw new IllegalArgumentException("partitionSize must be at least 1");
        }
        this.list = requireNonNull(list);
        this.partitionSize = requireNonNull(partitionSize);
    }

    @Override
    public int size() {
        return (int) Math.ceil(list.size() / (double) partitionSize);
    }

    @Override
    public List<T> get(int index) {
        int size = size();
        if (index >= size) {
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds " + size);
        }
        int partitionStart = index * partitionSize;
        int partitionEnd = Math.min(partitionStart + partitionSize, list.size());


        return list.subList(partitionStart, partitionEnd);
    }
}
