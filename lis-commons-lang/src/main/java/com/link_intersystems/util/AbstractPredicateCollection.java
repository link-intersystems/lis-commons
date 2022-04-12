package com.link_intersystems.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractPredicateCollection<T> implements Predicate<T>, Serializable {
    private boolean initialTestResult;
    protected Collection<Predicate<T>> predicates;

    public AbstractPredicateCollection(boolean initialTestResult, Collection<Predicate<T>> predicates) {
        this.initialTestResult = initialTestResult;
        this.predicates = new ArrayList<>(predicates);
    }

    @Override
    public boolean test(T t) {
        boolean test = initialTestResult;

        for (Predicate<T> predicate : predicates) {
            test = predicate.test(t);
            if (shouldBreak(test)) {
                break;
            }
        }
        return test;
    }

    protected abstract boolean shouldBreak(boolean latestPredicateResult);
}
