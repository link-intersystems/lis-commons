package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface PropertyDesc {
    String getName();

    Class<?> getType();

    boolean isReadable();

    boolean isWritable();

    Class<?> getDeclaringClass();

    public <T> T getPropertyValue(Object bean) throws PropertyReadException;

    public void setPropertyValue(Object bean, Object value);
}
