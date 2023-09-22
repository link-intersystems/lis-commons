package com.link_intersystems.util;

import java.util.List;

import static java.util.Objects.*;

public class ListSequence<E> implements Sequence<E> {

    private final List<E> list;

    public ListSequence(List<E> list) {
        this.list = requireNonNull(list);
    }

    @Override
    public E elementAt(int index) {
        return list.get(index);
    }

    @Override
    public int length() {
        return list.size();
    }
}
