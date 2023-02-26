package com.link_intersystems.swing.selection;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractSelection<E> implements Selection<E> {

    public boolean isEmpty() {
        return size() == 0;
    }

    public E getFirstElement() {
        List<E> list = toList();

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public Iterator<E> iterator() {
        return toList().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSelection<?> that = (AbstractSelection<?>) o;

        return Objects.equals(toList(), that.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toList());
    }

}
