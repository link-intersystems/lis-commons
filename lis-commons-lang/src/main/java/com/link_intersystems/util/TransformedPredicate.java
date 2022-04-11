package com.link_intersystems.util;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TransformedPredicate<T, R> implements Predicate<T> {

    private Function<T, R> transformer;
    private Predicate<R> resultPredicate;

    public TransformedPredicate(Function<T, R> transformer, Predicate<R> resultPredicate) {
        this.transformer = transformer;
        this.resultPredicate = resultPredicate;
    }

    @Override
    public boolean test(T t) {
        R transformed = transformer.apply(t);
        return resultPredicate.test(transformed);
    }
}
