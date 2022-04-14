package com.link_intersystems.util;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Iterators {


    public static <E> List<E> toList(Iterator<E> iterator) {
        Iterable<E> iterable = () -> iterator;
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
