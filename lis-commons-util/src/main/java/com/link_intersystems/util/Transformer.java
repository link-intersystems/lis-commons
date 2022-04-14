package com.link_intersystems.util;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@FunctionalInterface
public interface Transformer<S, T> {

    public T transform(S source);
}
