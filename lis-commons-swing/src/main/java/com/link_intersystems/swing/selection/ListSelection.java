package com.link_intersystems.swing.selection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSelection<E> extends AbstractList<E> implements Selection<E> {

    private List<E> list = new ArrayList<>();

    @Override
    public void add(int index, E element) {
        list.add(index, element);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<E> toList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }
}
