package com.link_intersystems.beans;

import java.io.Serializable;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Decorates a bean as a Map.
 */
public class BeanMapDecorator extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -6218854323681382593L;
    private Bean<?> bean;

    public BeanMapDecorator(Bean<?> bean) {
        this.bean = requireNonNull(bean);
    }

    public static Object indexSetter(int index, Object value) {
        return new IndexSetter(index, value);
    }

    private PropertyDescList getAllProperties() {
        BeanClass<?> beanClass = bean.getBeanClass();
        return beanClass.getProperties();
    }

    public int size() {
        return getAllProperties().size();
    }

    public boolean isEmpty() {
        /*
         * Since all objects extend Object they have at least one property
         * 'class'. Thus a bean map decorator can never be empty.
         */
        return false;
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        String propertyName = key.toString();
        BeanClass<?> beanClass = bean.getBeanClass();
        return beanClass.getProperties().filter(PropertyDesc.PREDICATE).containsProperty(propertyName);
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }
        String propertyName = key.toString();


        PropertyDescList properties = getAllProperties();
        PropertyDesc propertyDesc = properties.getByName(propertyName);

        return get(propertyDesc);
    }

    private Object get(PropertyDesc propertyDesc) {
        if (propertyDesc == null) {
            return null;
        }
        BeanClass<?> beanClass = bean.getBeanClass();
        String propertyName = propertyDesc.getName();
        boolean isIndexedProperty = beanClass.getProperties().filter(IndexedPropertyDesc.PREDICATE).containsProperty(propertyName);

        if (isIndexedProperty) {
            IndexedProperty property = (IndexedProperty) bean.getIndexedProperties().getByDesc(propertyDesc);
            checkReadAccess(property);
            return new IndexedValue(property);
        } else {
            Property property = bean.getSingleProperties().getByDesc(propertyDesc);
            checkReadAccess(property);
            return property.getValue();
        }
    }


    /**
     * @see BeanMapDecorator#indexSetter(int, Object) to set indexed property values.
     */
    public Object put(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "BeanMapDecorator does not support putting 'null' keys, because a bean can never have a 'null' property.");
        }
        BeanClass<?> beanClass = bean.getBeanClass();
        PropertyDescList properties = beanClass.getProperties();
        PropertyDesc propertyDesc = properties.getByName(key);

        if (propertyDesc == null) {
            throw new NoSuchPropertyException(
                    bean.getBeanClass().getType(),
                    key);
        }

        return put(propertyDesc, value);
    }

    private Object put(PropertyDesc propertyDesc, Object value) {
        Object previousValue = null;

        if (propertyDesc instanceof IndexedPropertyDesc) {
            if (!(value instanceof IndexSetter)) {
                throw new IllegalArgumentException(
                        "Property named "
                                + propertyDesc.getName()
                                + " is an indexed property. To set an indexed property's value you must use "
                                + IndexSetter.class.getSimpleName() + " to wrap the value.");
            }
            IndexSetter indexedValueSet = IndexSetter.class.cast(value);
            IndexedProperty indexedProperty = (IndexedProperty) bean.getIndexedProperties().getByDesc(propertyDesc);
            checkWriteAccess(indexedProperty);
            Object element = indexedValueSet.getElement();
            indexedProperty.setValue(indexedValueSet.getIndex(), element);
        } else {
            Property property = bean.getSingleProperties().getByDesc(propertyDesc);
            checkWriteAccess(property);
            previousValue = getValueIfReadable(propertyDesc.getName());
            property.setValue(value);

        }

        return previousValue;
    }

    private Object getValueIfReadable(String propertyName) {
        Property property = bean.getProperties().getByName(propertyName);
        if (property.getPropertyDesc().isReadable()) {
            return property.getValue();
        }
        return null;
    }

    private void checkWriteAccess(IndexedProperty property) {
        if (!property.getPropertyDesc().isIndexedWritable()) {
            throw new UnsupportedOperationException(
                    "BeanMapDecorator can not put property "
                            + property
                            + ", because the indexed property has no indexed "
                            + "setter method, e.g. void setter(int index, PropertyType value); ");
        }
    }

    private void checkWriteAccess(Property property) {
        if (!property.getPropertyDesc().isWritable()) {
            throw new UnsupportedOperationException(
                    "BeanMapDecorator can not put property " + property
                            + ", because the property is not writable");
        }
    }

    private void checkReadAccess(IndexedProperty property) {
        if (!property.getPropertyDesc().isIndexedReadable()) {
            throw new UnsupportedOperationException(
                    "BeanMapDecorator can not get property "
                            + property
                            + ", because the indexed property has no indexed "
                            + "getter method, e.g. PropertyType getter(int index);");
        }
    }

    private void checkReadAccess(Property property) {
        if (!property.getPropertyDesc().isReadable()) {
            throw new UnsupportedOperationException(
                    "BeanMapDecorator can not get property " + property
                            + ", because the property is not readable");
        }
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException(
                "BeanMapDecorator does not support remove");
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation iterates over the specified map's <tt>entrySet()</tt>
     * collection, and calls this map's <tt>put</tt> operation once for each
     * entry returned by the iteration.
     *
     * <p>
     * Note that this implementation throws an
     * <tt>UnsupportedOperationException</tt> if this map does not support the
     * <tt>put</tt> operation and the specified map is nonempty.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (Map.Entry<? extends String, ? extends Object> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    public void clear() {
        throw new UnsupportedOperationException(
                "A BeanMapDecorator can not be cleared, because"
                        + " properties of a bean belong to the bean's"
                        + " class and therefore can not be removed at runtime.");
    }

    public Set<String> keySet() {
        BeanClass<?> beanClass = bean.getBeanClass();
        PropertyDescList allProperties = beanClass.getProperties();
        return new LinkedHashSet<>(allProperties.getPropertyNames());
    }

    public Collection<Object> values() {
        Collection<Object> values = new AbstractCollection<Object>() {

            @Override
            public Iterator<Object> iterator() {
                Iterator<PropertyDesc> readablePropertyDescs = bean
                        .getBeanClass()
                        .getProperties().stream()
                        .filter(PropertyDesc::isReadable)
                        .iterator();

                return new Iterator<Object>() {

                    public boolean hasNext() {
                        return readablePropertyDescs.hasNext();
                    }

                    public Object next() {
                        PropertyDesc propertyDesc = readablePropertyDescs.next();
                        if (propertyDesc instanceof IndexedPropertyDesc) {
                            IndexedProperty indexedProperty = (IndexedProperty) bean.getIndexedProperties().getByDesc(propertyDesc);
                            return new IndexedValue(indexedProperty);
                        } else {
                            return propertyDesc.getPropertyValue(bean.getBeanObject());
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Bean properties can not be removed.");
                    }
                };
            }

            @Override
            public int size() {
                return bean.getBeanClass().getProperties().getPropertyNames().size();
            }
        };
        return values;
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        Set<java.util.Map.Entry<String, Object>> entries = new AbstractSet<Map.Entry<String, Object>>() {

            @Override
            public Iterator<java.util.Map.Entry<String, Object>> iterator() {
                Iterator<PropertyDesc> propertyDescIterator = bean.getBeanClass().getProperties().iterator();

                return new Iterator<Map.Entry<String, Object>>() {


                    public boolean hasNext() {
                        return propertyDescIterator.hasNext();
                    }

                    public java.util.Map.Entry<String, Object> next() {
                        PropertyDesc propertyDesc = propertyDescIterator.next();

                        return new Map.Entry<String, Object>() {

                            public Object setValue(Object value) {
                                return put(propertyDesc, value);
                            }

                            public Object getValue() {
                                return get(propertyDesc);
                            }

                            public String getKey() {
                                return propertyDesc.getName();
                            }
                        };
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size() {
                return BeanMapDecorator.this.size();
            }
        };
        return entries;
    }

    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    public static final class IndexedValue {

        private final IndexedProperty indexedProperty;

        private IndexedValue(IndexedProperty indexedProperty) {
            this.indexedProperty = indexedProperty;
        }

        public Object getElement(int index) {
            return indexedProperty.getValue(index);
        }

    }

    /**
     * Use {@link BeanMapDecorator#indexSetter(int, Object)} to create an instance.
     */
    public static final class IndexSetter {

        private final int index;
        private final Object element;

        private IndexSetter(int index, Object element) {
            this.index = index;
            this.element = element;
        }

        public int getIndex() {
            return index;
        }

        public Object getElement() {
            return element;
        }

    }
}
