package com.link_intersystems.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ChainedIterator<E> implements Iterator<E> {

    private Iterator<Iterator<E>> chain;
    private Iterator<E> current = Collections.emptyIterator();

    @SafeVarargs
    public ChainedIterator(Iterator<E>... iterators) {
        this(asList(iterators));
    }

    public ChainedIterator(Collection<Iterator<E>> iterators) {
        chain = iterators.iterator();
    }

    @Override
    public boolean hasNext() {
        while (!current.hasNext() && chain.hasNext()) {
            current = chain.next();
        }

        return current.hasNext();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return current.next();
    }
}
