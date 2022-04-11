package com.link_intersystems.util;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AndPredicate<T> implements Predicate<T>, Serializable {

    private Collection<Predicate<T>> predicates;

    @SafeVarargs
    public AndPredicate(Predicate<T>... predicates) {
        this(asList(predicates));
    }

    public AndPredicate(Collection<Predicate<T>> predicates) {
        this.predicates = new ArrayList<>(predicates);
    }

    @Override
    public boolean test(T t) {
        boolean test = false;

        for (Predicate<T> predicate : predicates) {
            test = predicate.test(t);
            if (!test) {
                break;
            }
        }
        return test;
    }
}
