package com.link_intersystems.util;

import java.util.function.Predicate;

/**
 * {@link Predicate} that returns true only for the first evaluation.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 */
public class FirstPredicate<T> implements Predicate<T> {

    private boolean first = true;

    public boolean test(T object) {
        if (first) {
            first = false;
            return true;
        }
        return false;
    }
}