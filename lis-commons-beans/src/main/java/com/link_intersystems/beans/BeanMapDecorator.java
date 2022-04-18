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

    public static Object indexedValueSetter(int index, Object value) {
        return new IndexedElementSetter(index, value);
    }

    public int size() {
        BeanClass<?> beanClass = bean.getBeanClass();
        return beanClass.getProperties().size();
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
        return beanClass.hasAnyProperty(propertyName);
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }
        String propertyName = key.toString();
        BeanClass<?> beanClass = bean.getBeanClass();

        PropertyDescList<? extends PropertyDesc> properties = beanClass.getProperties();
        PropertyDesc propertyDesc = properties.getByName(propertyName);

        if (propertyDesc == null) {
            return null;
        }

        boolean isIndexedProperty = beanClass.hasIndexedProperty(propertyName);

        if (isIndexedProperty) {
            IndexedProperty property = (IndexedProperty) bean.getProperty(propertyDesc);
            checkReadAccess(property);
            return new IndexedValue(property);
        } else {
            Property property = bean.getProperty(propertyDesc);
            checkReadAccess(property);
            return property.getValue();
        }
    }

    public Object put(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "BeanMapDecorator does not support putting 'null' keys, because a bean can never have a 'null' property.");
        }
        String propertyName = key;
        BeanClass<?> beanClass = bean.getBeanClass();
        PropertyDescList<? extends PropertyDesc> properties = beanClass.getProperties();
        PropertyDesc propertyDesc = properties.getByName(propertyName);

        if (propertyDesc == null) {
            throw new NoSuchPropertyException(bean.getBeanClass().getType(),
                    key);
        }
        Object previousValue = null;

        if (propertyDesc instanceof IndexedPropertyDesc) {
            if (!(value instanceof IndexedElementSetter)) {
                throw new IllegalArgumentException(
                        "Property named "
                                + key
                                + " is an indexed property. To set an indexed property's value you must use "
                                + IndexedElementSetter.class.getSimpleName());
            }
            IndexedElementSetter indexedValueSet = IndexedElementSetter.class
                    .cast(value);
            IndexedProperty indexedProperty = (IndexedProperty) bean.getProperty(propertyDesc);
            checkWriteAccess(indexedProperty);
            Object element = indexedValueSet.getElement();
            indexedProperty.setValue(indexedValueSet.getIndex(), element);
        } else {
            Property property = bean.getProperty(propertyDesc);
            checkWriteAccess(property);
            previousValue = getValueIfReadable(key);
            property.setValue(value);

        }

        return previousValue;
    }

    private Object getValueIfReadable(String propertyName) {
        Property property = bean.getProperty(propertyName);
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
        Set<String> keySet = new AbstractSet<String>() {

            @Override
            public Iterator<String> iterator() {
                return bean.getBeanClass().getProperties().getAllPropertyNames().iterator();
            }

            @Override
            public int size() {
                return bean.getBeanClass().getProperties().getAllPropertyNames().size();
            }
        };
        return keySet;
    }

    public Collection<Object> values() {
        Collection<Object> values = new AbstractCollection<Object>() {

            @Override
            public Iterator<Object> iterator() {
                return new Iterator<Object>() {
                    private Iterator<String> propertsNameIterator = bean
                            .getBeanClass().getProperties().getAllPropertyNames().iterator();

                    public boolean hasNext() {
                        return propertsNameIterator.hasNext();
                    }

                    public Object next() {
                        String propertyName = propertsNameIterator.next();
                        PropertyDesc propertyDesc = bean.getBeanClass().getProperties().getByName(propertyName);
                        if (propertyDesc instanceof IndexedPropertyDesc) {
                            IndexedProperty indexedProperty = (IndexedProperty) bean.getProperty(propertyDesc);
                            return new IndexedValue(indexedProperty);
                        } else {
                            return bean.getProperty(propertyName).getValue();
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public int size() {
                return bean.getBeanClass().getProperties().getAllPropertyNames().size();
            }
        };
        return values;
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        Set<java.util.Map.Entry<String, Object>> entries = new AbstractSet<Map.Entry<String, Object>>() {

            @Override
            public Iterator<java.util.Map.Entry<String, Object>> iterator() {

                return new Iterator<Map.Entry<String, Object>>() {
                    private Iterator<String> propertyNamesIterator = bean
                            .getBeanClass().getProperties().getAllPropertyNames().iterator();

                    public boolean hasNext() {
                        return propertyNamesIterator.hasNext();
                    }

                    public java.util.Map.Entry<String, Object> next() {
                        final String propertyName = propertyNamesIterator
                                .next();

                        return new Map.Entry<String, Object>() {

                            public Object setValue(Object value) {
                                return put(propertyName, value);
                            }

                            public Object getValue() {
                                return get(propertyName);
                            }

                            public String getKey() {
                                return propertyName;
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

    public static final class IndexedElementSetter {

        private final int index;
        private final Object element;

        private IndexedElementSetter(int index, Object element) {
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
