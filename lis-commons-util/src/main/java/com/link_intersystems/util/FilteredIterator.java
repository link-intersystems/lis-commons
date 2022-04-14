package com.link_intersystems.util;

import java.util.Iterator;
import java.util.function.Predicate;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FilteredIterator<E> implements Iterator<E> {

    private final Iterator<E> filtered;

    public FilteredIterator(Iterator<E> iterator, Predicate<E> filter) {
        filtered = stream(spliteratorUnknownSize(iterator, ORDERED), false).filter(filter).iterator();
    }

    @Override
    public boolean hasNext() {
        return filtered.hasNext();
    }

    @Override
    public E next() {
        return filtered.next();
    }
}
