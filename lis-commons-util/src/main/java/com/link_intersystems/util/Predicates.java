package com.link_intersystems.util;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Predicates {

    public static <T> Predicate<T> firstPredicate() {
        return new Predicate<T>() {

            private boolean first = true;

            @Override
            public boolean test(T t) {
                if (first) {
                    first = false;
                    return true;
                }
                return false;
            }
        };
    }

    public static <T> Predicate<T> equal(T equalTo) {
        return equal(Objects::equals, equalTo);
    }

    public static <T> Predicate<T> equal(Equality<T> equality, T equalTo) {
        return t -> equality.isEqual(t, equalTo);
    }
}
