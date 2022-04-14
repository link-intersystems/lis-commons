package com.link_intersystems.util;

import java.util.Collection;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class OrAbstractPredicate<T> extends AbstractPredicateCollection<T> {

    @SafeVarargs
    public OrAbstractPredicate(Predicate<T>... predicates) {
        this(asList(predicates));
    }

    public OrAbstractPredicate(Collection<Predicate<T>> predicates) {
        super(true, predicates);
    }

    @Override
    protected boolean shouldBreak(boolean latestPredicateResult) {
        return latestPredicateResult;
    }
}
