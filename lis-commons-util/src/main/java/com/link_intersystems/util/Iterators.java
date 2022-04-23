package com.link_intersystems.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public static <E> Iterator<E> filtered(Iterator<E> iterator, Predicate<E> filterPredicate) {
        return toStream(iterator).filter(filterPredicate).iterator();
    }

    public static <E> Iterator<E> chained(Iterator<E>... iterators) {
        return chained(Arrays.asList(iterators));
    }

    public static <E> Iterator<E> chained(List<Iterator<E>> iterators) {
        return iterators.stream().map(Iterators::toStream).flatMap(Function.identity()).iterator();
    }

    public static <S, T> Iterator<T> transformed(Iterator<S> iterator, Function<S, T> transformer) {
        return toStream(iterator).map(transformer).iterator();
    }
}
