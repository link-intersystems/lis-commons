package com.link_intersystems.util;

import java.util.Collection;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class OrPredicate<T> extends AbstractPredicateCollection<T> {

    @SafeVarargs
    public OrPredicate(Predicate<T>... predicates) {
        this(asList(predicates));
    }

    public OrPredicate(Collection<Predicate<T>> predicates) {
        super(true, predicates);
    }

    @Override
    protected boolean shouldBreak(boolean latestPredicateResult) {
        return latestPredicateResult;
    }
}
