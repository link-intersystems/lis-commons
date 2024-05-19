package com.link_intersystems.beans;

import java.util.function.Predicate;

/**
 * A {@link PropertyDesc}
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface PropertyDesc {

    public static final Predicate<? super PropertyDesc> PREDICATE = IndexedPropertyDesc.PREDICATE.negate();

    String getName();

    Class<?> getType();

    boolean isReadable();

    boolean isWritable();

    public <T> T getPropertyValue(Object bean) throws PropertyReadException;

    public void setPropertyValue(Object bean, Object value);
}
