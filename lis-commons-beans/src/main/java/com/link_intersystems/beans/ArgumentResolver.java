package com.link_intersystems.beans;

import java.lang.reflect.Parameter;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ArgumentResolver {

    default Object[] resolveArguments(Parameter[] parameters) throws ArgumentResolveException {
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            args[i] = resolveArgument(parameters[i]);
        }

        return args;
    }

    public Object resolveArgument(Parameter parameter) throws ArgumentResolveException;
}
