package com.link_intersystems.beans;

import java.lang.reflect.Array;

import static java.util.Objects.*;

class ExactPropertyCopyStrategy implements PropertyCopyStrategy {

    private static interface IndexedPropertyIterator {

        public boolean next();

        public int getIndex();

        public Object getValue();

        Object toArray();
    }

    private static class OnTheFlyIndexPropertyIterator implements IndexedPropertyIterator {

        public static final int INCREASE_ARRAY_SIZE = 16;
        private int index = 0;
        private IndexedProperty indexedProperty;
        private Object value;


        public OnTheFlyIndexPropertyIterator(IndexedProperty indexedProperty) {
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
        public Object getValue() {
            return value;
        }

        @Override
        public Object toArray() {
            Class<?> type = indexedProperty.getPropertyDesc().getType();
            Object targetArray = Array.newInstance(type, INCREASE_ARRAY_SIZE);
            int i = 0;


            for (; i <= Integer.MAX_VALUE; i++) {
                try {
                    Object valueAtIndex = indexedProperty.getValue(i);
                    int targetArrayLength = Array.getLength(targetArray);
                    if (i >= targetArrayLength) {
                        Object newTargetArray = Array.newInstance(type, targetArrayLength + INCREASE_ARRAY_SIZE);
                        System.arraycopy(targetArray, 0, newTargetArray, 0, targetArrayLength);
                        targetArray = newTargetArray;
                    }
                    Array.set(targetArray, i, valueAtIndex);
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }

            if(i < Array.getLength(targetArray)){
                Object newTargetArray = Array.newInstance(type, i);
                System.arraycopy(targetArray, 0, newTargetArray, 0, i);
                targetArray = newTargetArray;
            }

            return targetArray;
        }
    }

    private static class ArrayIndexedPropertyIterator implements IndexedPropertyIterator {

        private static final Object[] EMPTY = new Object[0];

        private int index = -1;
        private final Object array;

        public ArrayIndexedPropertyIterator(Object array) {
            this.array = array == null ? EMPTY : array;
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
        public Object getValue() {
            return Array.get(array, index);
        }

        @Override
        public Object toArray() {
            return array == EMPTY ? null : array;
        }
    }

    @Override
    public void copy(Property sourceProperty, Property targetProperty) {
        boolean indexedSourceProperty = sourceProperty instanceof IndexedProperty;
        boolean indexedTargetProperty = targetProperty instanceof IndexedProperty;

        boolean typesDiffer = indexedSourceProperty ^ indexedTargetProperty;
        if (typesDiffer) {
            throwDifferentTypesException(sourceProperty, targetProperty);
        }

        if (indexedSourceProperty && indexedTargetProperty) {
            copyIndexedProperty((IndexedProperty) sourceProperty, (IndexedProperty) targetProperty);
        } else {
            Object value = sourceProperty.getValue();
            targetProperty.setValue(value);
        }
    }

    private void copyIndexedProperty(IndexedProperty sourceProperty, IndexedProperty targetProperty) {
        IndexedPropertyIterator sourcePropertyValuesIterator = getIndexedPropertyIterator(sourceProperty);

        if (targetProperty.getPropertyDesc().isWritable()) {
            Object array = sourcePropertyValuesIterator.toArray();
            targetProperty.setValue(array);
        } else {
            while (sourcePropertyValuesIterator.next()) {
                int index = sourcePropertyValuesIterator.getIndex();
                Object value = sourcePropertyValuesIterator.getValue();
                targetProperty.setValue(index, value);
            }
        }
    }

    private IndexedPropertyIterator getIndexedPropertyIterator(IndexedProperty property) {
        if (property.getPropertyDesc().isReadable()) {
            Object value = property.getValue();
            return new ArrayIndexedPropertyIterator(value);
        } else {
            return new OnTheFlyIndexPropertyIterator(property);
        }
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
