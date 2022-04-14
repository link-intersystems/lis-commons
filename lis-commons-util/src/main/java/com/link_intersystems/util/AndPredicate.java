package com.link_intersystems.util;

import java.util.Collection;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AndPredicate<T> extends AbstractPredicateCollection<T> {

    @SafeVarargs
    public AndPredicate(Predicate<T>... predicates) {
        this(asList(predicates));
    }

    public AndPredicate(Collection<Predicate<T>> predicates) {
        super(false, predicates);
    }

    @Override
    protected boolean shouldBreak(boolean latestPredicateResult) {
        return !latestPredicateResult;
    }

}
