package com.link_intersystems.util;

import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TransformedPredicate<S, T> implements Predicate<S> {

    private Transformer<S, T> transformer;
    private Predicate<T> resultPredicate;

    public TransformedPredicate(Transformer<S, T> transformer, Predicate<T> resultPredicate) {
        this.transformer = transformer;
        this.resultPredicate = resultPredicate;
    }

    @Override
    public boolean test(S t) {
        T transformed = transformer.transform(t);
        return resultPredicate.test(transformed);
    }
}
