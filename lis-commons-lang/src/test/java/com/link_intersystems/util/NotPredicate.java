package com.link_intersystems.util;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NotPredicate<T> implements Predicate<T>, Serializable {

    private Predicate<T> predicate;

    public NotPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(T t) {
        return !predicate.test(t);
    }
}
