package com.link_intersystems.util;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that returns true if an object it is tested against is
 * equal to the object it was constructed with according to the equal definition
 * of {@link Objects#equals(Object, Object)}. This is the main difference to using
 * a method reference like: someObject::equals, because {@link Objects#equals(Object, Object)}
 * is <code>null</code> safe and contains true if both are <code>null</code>.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class EqualPredicate<T> implements Predicate<T> {

    private T equalTo;

    public EqualPredicate(T equalTo) {
        this.equalTo = equalTo;
    }

    @Override
    public boolean test(T t) {
        return Objects.equals(t, equalTo);
    }
}
