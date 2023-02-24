package com.link_intersystems.swing.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface Selection<E> extends Iterable<E> {

    public static <E> Selection<E> empty() {
        return new Selection<E>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public List<E> toList() {
                return Collections.emptyList();
            }
        };
    }

    public static <E> Selection<E> adapted(Selection<? extends E> subtypeSelection) {
        return new Selection<E>() {
            @Override
            public int size() {
                return subtypeSelection.size();
            }

            @Override
            public List<E> toList() {
                List<? extends E> subtypeList = subtypeSelection.toList();
                return new ArrayList<>(subtypeList);
            }
        };
    }

    default public boolean isEmpty() {
        return size() == 0;
    }

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
