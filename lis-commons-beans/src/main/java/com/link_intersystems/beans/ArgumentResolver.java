package com.link_intersystems.beans;

import java.lang.reflect.Parameter;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ArgumentResolver {

    public static final ArgumentResolver NULL_INSTANCE = new ArgumentResolver() {
        @Override
        public boolean canResolveArgument(Parameter parameter) {
            return false;
        }

        @Override
        public Object resolveArgument(Parameter parameter) throws ArgumentResolveException {
            return null;
        }
    };

    default boolean canResolveArguments(Parameter[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            if (!canResolveArgument(parameters[i])) {
                return false;
            }
        }

        return true;
    }

    boolean canResolveArgument(Parameter parameter);

    default Object[] resolveArguments(Parameter[] parameters) throws ArgumentResolveException {
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            args[i] = resolveArgument(parameters[i]);
        }

        return args;
    }

    public Object resolveArgument(Parameter parameter) throws ArgumentResolveException;
}
