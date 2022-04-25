package com.link_intersystems.beans;

import java.lang.reflect.Parameter;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ArgumentResolver {

    public Object resolveArgument(Parameter parameter);
}
