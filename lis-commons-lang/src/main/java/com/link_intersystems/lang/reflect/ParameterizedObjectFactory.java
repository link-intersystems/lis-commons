package com.link_intersystems.lang.reflect;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ParameterizedObjectFactory<T, P> {

    public T getObject(P parameter);
}
