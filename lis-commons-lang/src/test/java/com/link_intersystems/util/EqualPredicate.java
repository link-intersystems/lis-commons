package com.link_intersystems.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class EqualPredicate<T extends  Serializable> implements Predicate<T>, Serializable {

    private T equalTo;

    public EqualPredicate(T equalTo) {
        this.equalTo = equalTo;
    }

    @Override
    public boolean test(T t) {
        return Objects.equals(t, equalTo);
    }
}
