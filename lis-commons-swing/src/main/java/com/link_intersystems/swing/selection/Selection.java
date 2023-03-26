package com.link_intersystems.swing.selection;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface Selection<E> extends Iterable<E> {

    public static <E> Selection<E> empty() {
        return new Selection<E>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public E getFirstElement() {
                return null;
            }

            @Override
            public List<E> toList() {
                return Collections.emptyList();
            }

            @Override
            public Iterator<E> iterator() {
                return Collections.emptyIterator();
            }
        };
    }

    public int size();

    public boolean isEmpty();

    public E getFirstElement();

    public List<E> toList();

    default <R> Optional<R> mapFirstElement(Function<E, R> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        }

        E firstElement = getFirstElement();
        return Optional.of(firstElement).map(mapper);
    }
}
