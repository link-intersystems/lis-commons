package com.link_intersystems.beans;

import java.lang.reflect.Array;

import static java.util.Objects.*;

public interface PropertyCopyStrategies {

    /**
     * Copies only {@link Property}s to {@link Property}s and {@link IndexedProperty}s to {@link IndexedProperty}s.
     * In case of {@link IndexedProperty}s the implementation prefers the array accessors over the index based accessors.
     * So if a bean declares a getter that returns an array it will be used over the index getter. E.g.
     *
     * <pre>
     *     class ABean {
     *
     *         // will be preferred over the index getter
     *         public String[] getNames(){
     *         }
     *
     *         public String getNames(int index){
     *         }
     *     }
     * </pre>
     * The same applies for array and index setters.
     */
    public static PropertyCopyStrategy EXACT = new ExactPropertyCopyStrategy();

    /**
     * Adapts {@link Property}s to {@link IndexedProperty}s and vice versa. E.g. if you have some beans like the following:
     * <pre>
     *     class SourceBean {
     *
     *         public String[] getNames(){
     *         }
     *
     *         public String getName(){
     *         }
     *
     *     }
     *
     *     class TargetBean {
     *         public void setNames(int index, String value){
     *         }
     *
     *         public void setName(String[] nameValues){
     *         }
     *     }
     * </pre>
     * <p>
     * the {@link PropertyCopyStrategies#ADAPTED} can copy the SourceBean.getNames() to the TargetBean using the index setter
     * and can adapt the SourceBean.getName single value to the TargetBean.setName(String[]) as an one element array.
     * <p/>
     * When a single source value is <code>null</code>, e.g. <code>public String getName()</code>, it is either adapted
     * as a single array with one <code>null</code> element on the indexed target property or one call to the index setter
     * <code>setName(0, null)</code>.
     */
    public static PropertyCopyStrategy ADAPTED = new AdaptedPropertyCopyStrategy();
}

class ExactPropertyCopyStrategy implements PropertyCopyStrategy {


    @Override
    public void copy(Property sourceProperty, Property targetProperty) {
        boolean indexedSourceProperty = sourceProperty instanceof IndexedProperty;
        boolean indexedTargetProperty = targetProperty instanceof IndexedProperty;

        boolean typesDiffer = indexedSourceProperty ^ indexedTargetProperty;
        if (typesDiffer) {
            throwDifferentTypesException(sourceProperty, targetProperty);
        }

        if (indexedSourceProperty && indexedTargetProperty) {
            copyProperty(sourceProperty, targetProperty);
        } else {
            Object value = sourceProperty.getValue();
            targetProperty.setValue(value);
        }
    }

    protected void copyProperty(Property sourceProperty, Property targetProperty) {
        PropertyValueIterator sourceValueIterator = getPropertyValueIterator(sourceProperty);
        PropertyValueEdit propertyValueEdit = getPropertyValueEdit(targetProperty);
        while (sourceValueIterator.next()) {
            int index = sourceValueIterator.getIndex();
            Object value = sourceValueIterator.getArray();
            propertyValueEdit.setValue(index, value);
        }
        propertyValueEdit.commit();
    }

    protected PropertyValueEdit getPropertyValueEdit(Property property) {
        if (property.getPropertyDesc().isWritable()) {
            return new ArraySetterPropertyValueEdit(property);
        } else if (property instanceof IndexedProperty) {
            IndexedProperty indexedProperty = (IndexedProperty) property;
            return new IndexMethodPropertyValueEdit(indexedProperty);
        }

        throw new IllegalArgumentException("");
    }

    protected PropertyValueIterator getPropertyValueIterator(Property property) {
        if (property instanceof IndexedProperty) {
            IndexedProperty indexedProperty = (IndexedProperty) property;
            if (property.getPropertyDesc().isReadable()) {
                return new DefaultPropertyValueIterator(property);
            } else {
                return new IndexGetterPropertyValueIterator(indexedProperty);
            }
        } else if (property.getPropertyDesc().isReadable()) {
            return new DefaultPropertyValueIterator(property);
        }

        throw new IllegalArgumentException();
    }

    private void throwDifferentTypesException(Property sourceProperty, Property targetProperty) {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(" can not copy properties of different types:\n");
        sb.append("\t");
        sb.append(sourceProperty);
        sb.append("\n");
        sb.append("\t");
        sb.append(targetProperty);
        throw new IllegalArgumentException(sb.toString());
    }
}

class AdaptedPropertyCopyStrategy extends ExactPropertyCopyStrategy {

    @Override
    public void copy(Property sourceProperty, Property targetProperty) {
        copyProperty(sourceProperty, targetProperty);
    }

}

interface PropertyValueEdit {

    public void setValue(int index, Object value);

    public void commit();

}

class ArraySetterPropertyValueEdit implements PropertyValueEdit {

    private Property property;
    private DynamicArray array;

    public ArraySetterPropertyValueEdit(Property property) {
        this.property = requireNonNull(property);
        if (!property.getPropertyDesc().isWritable()) {
            throw new IllegalArgumentException(property + " does not declare an setter for " + property.getPropertyDesc().getType().getName() + "[]");
        }
        PropertyDesc propertyDesc = property.getPropertyDesc();
        Class<?> type = propertyDesc.getType();
        type = type.getComponentType() == null ? type : type.getComponentType();
        array = new DynamicArray<>(type);
    }

    @Override
    public void setValue(int index, Object value) {
        array.set(index, value);
    }

    @Override
    public void commit() {
        Object[] arrayToSet = this.array.getArray();
        property.setValue(arrayToSet);
    }
}

class DynamicArray<T> {

    static final int INCREASE_ARRAY_SIZE = 16;

    private final Class<?> componentType;
    private final int increaseSize;
    private Object array;
    private int maxIndex = -1;

    public DynamicArray(Class<T> componentType) {
        this(componentType, INCREASE_ARRAY_SIZE);
    }

    public DynamicArray(Class<T> componentType, int increaseSize) {
        this.componentType = requireNonNull(componentType);
        this.increaseSize = increaseSize;
        array = Array.newInstance(componentType, increaseSize);
    }

    public void set(int index, T value) {
        while (index >= Array.getLength(array)) {
            int arrayLength = Array.getLength(array);
            Object biggerArray = Array.newInstance(componentType, index + increaseSize);
            System.arraycopy(array, 0, biggerArray, 0, arrayLength);
            array = biggerArray;
        }
        Array.set(array, index, value);
        maxIndex = Math.max(maxIndex, index);
    }

    public T[] getArray() {
        if ((maxIndex + 1) < Array.getLength(array)) {
            Object fittingArray = Array.newInstance(componentType, maxIndex + 1);
            System.arraycopy(array, 0, fittingArray, 0, Array.getLength(fittingArray));
            return (T[]) fittingArray;
        }
        return (T[]) array;
    }
}

class IndexMethodPropertyValueEdit implements PropertyValueEdit {

    private IndexedProperty indexedProperty;

    public IndexMethodPropertyValueEdit(IndexedProperty indexedProperty) {
        this.indexedProperty = requireNonNull(indexedProperty);
        if (!indexedProperty.getPropertyDesc().isIndexedWritable()) {
            throw new IllegalArgumentException(indexedProperty + " does not declare an index setter.");
        }
    }

    @Override
    public void setValue(int index, Object value) {
        indexedProperty.setValue(index, value);
    }

    @Override
    public void commit() {

    }
}

interface PropertyValueIterator {

    public boolean next();

    public int getIndex();

    public Object getArray();

}

class IndexGetterPropertyValueIterator implements PropertyValueIterator {


    private int index = 0;
    private IndexedProperty indexedProperty;
    private Object value;


    public IndexGetterPropertyValueIterator(IndexedProperty indexedProperty) {
        this.indexedProperty = requireNonNull(indexedProperty);
    }

    @Override
    public boolean next() {
        try {
            value = indexedProperty.getValue(index++);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public int getIndex() {
        return index - 1;
    }

    @Override
    public Object getArray() {
        return value;
    }

}

class DefaultPropertyValueIterator implements PropertyValueIterator {

    private Object array;
    private int index = -1;

    public DefaultPropertyValueIterator(Property property) {
        PropertyDesc propertyDesc = property.getPropertyDesc();
        Object value = property.getValue();
        if (value == null) {
            array = Array.newInstance(propertyDesc.getType(), 1);
        } else {
            if (value.getClass().isArray()) {
                array = value;
            } else {
                Class<?> type = propertyDesc.getType();
                array = Array.newInstance(type, 1);
                Array.set(array, 0, value);
            }
        }
    }

    @Override
    public boolean next() {
        int length = Array.getLength(array);
        return ++index < length;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Object getArray() {
        return Array.get(array, index);
    }

}

