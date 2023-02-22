package com.link_intersystems.swing.selection;

import java.util.Iterator;
import java.util.List;

public interface StructuredSelection<E> extends Selection, Iterable<E> {

    public int size();

    default public E getFirstElement() {
        List<E> list = toList();

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    default Iterator<E> iterator() {
        return toList().iterator();
    }

    public List<E> toList();
}
