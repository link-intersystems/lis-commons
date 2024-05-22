package com.link_intersystems.beans;

import java.util.function.Predicate;

/**
 * A {@link PropertyDesc} is a metadata description of a bean's property.
 * It is implementation-dependent what a property is in a specific case. So
 * it depends on the {@link BeanClass} and therefore the {@link BeansFactory} you
 * use.
 * <p>
 * E.g. if you use the default {@link BeansFactory}, which implements the Java Bean specification,
 * a property is something accessible through a getter and setter. But this definition might change, e.g.
 * if an implementation adapts Java Records to this bean api.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface PropertyDesc {

    /**
     * A Predicate that determines if a PropertyDesc is indexed.
     */
    public static final Predicate<? super PropertyDesc> INDEXED = PropertyDesc::isIndexed;

    /**
     * A {@link Predicate} that determines if a {@link PropertyDesc} is not indexed.
     */
    public static final Predicate<? super PropertyDesc> NONE_INDEXED = PropertyDesc.INDEXED.negate();

    public String getName();

    public Class<?> getType();

    public boolean isReadable();

    public boolean isWritable();

    public <T> T getPropertyValue(Object bean) throws PropertyReadException;

    public void setPropertyValue(Object bean, Object value);

    default public boolean isIndexed() {
        return getIndexedPropertyDesc() != null;
    }

    default public IndexedPropertyDesc getIndexedPropertyDesc() {
        if (this instanceof IndexedPropertyDesc) {
            return (IndexedPropertyDesc) this;
        }
        return null;
    }
}
