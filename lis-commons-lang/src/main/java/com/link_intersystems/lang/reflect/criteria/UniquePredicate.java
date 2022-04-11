package com.link_intersystems.lang.reflect.criteria;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class UniquePredicate<T> implements Predicate<T> {

    private Set<T> uniqueElements = new HashSet<>();

    @Override
    public boolean test(T t) {
        return uniqueElements.add(t);
    }
}
