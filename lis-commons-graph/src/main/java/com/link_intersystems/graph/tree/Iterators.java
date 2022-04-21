package com.link_intersystems.graph.tree;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Iterators {


    public static <E> Stream<E> toStream(Iterator<E> iterator) {
        Iterable<E> iterable = () -> iterator;
        return toStream(iterable);
    }

    public static <E> Stream<E> toStream(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }


    public static <E> List<E> toList(Iterable<E> iterable) {
        return toList(iterable.iterator());
    }

    public static <E> List<E> toList(Iterator<E> iterator) {
        return toStream(iterator).collect(Collectors.toList());
    }
}
