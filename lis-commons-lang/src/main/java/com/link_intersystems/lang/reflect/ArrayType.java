package com.link_intersystems.lang.reflect;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Represents an array's component type of type T.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ArrayType<T> implements Serializable {

    private final Class<T[]> arrayType;
    private Class<T> componentType;

    @SuppressWarnings("unchecked")
    public ArrayType(Class<T> componentType) {
        this.componentType = componentType;
        arrayType = (Class<T[]>) Array.newInstance(componentType, 0).getClass();
    }

    @SuppressWarnings("unchecked")
    public T[] newInstance(int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    public Class<T[]> getType() {
        return arrayType;
    }

    public ArrayType<T[]> getNextDimension() {
        Class<T[]> thisDimension = getType();
        return new ArrayType<>(thisDimension);
    }
}
