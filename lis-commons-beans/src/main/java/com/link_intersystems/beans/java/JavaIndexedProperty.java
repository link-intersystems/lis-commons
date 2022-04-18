/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans.java;

import com.link_intersystems.beans.IndexedProperty;
import com.link_intersystems.beans.IndexedPropertyDesc;
import com.link_intersystems.beans.PropertyReadException;
import com.link_intersystems.beans.PropertyWriteException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Provides access to an indexed java bean property.
 *
 * <pre>
 * public class SomeBean {
 *
 *    private String[] internalNames = new String[0];
 *
 *    public void setNames(String[] names){
 *      this.internalNames = names.clone();
 *    }
 *
 *    public String[] getNames(){
 *    	return internalNames.clone();
 *    }
 *
 *    public void setNames(int index, String name){
 *      if(index >= internalNames.length){
 *      	String[] newArr = new String[index];
 *      	System.arraycopy(internalNames, 0, newArr, 0, internalNames.length);
 *          internalNames = newArr;
 *      }
 *      this.internalName[index] = name;
 *    }
 *
 *    public String getNames(int index){
 *    	if(index >= internalNames.length){
 *         return null;
 *      }
 *    	return internalNames[index];
 *    }
 *
 * }
 *
 * ...
 * SomeBean someBean = new SomeBean();
 *
 * Bean<SomeBean> bean = new Bean<SomeBean>(someBean);
 * IndexedProperty<String> namesProp = bean.getIndexedProperty("names");
 * namesProp.setValue(0, "Hello");
 * namesProp.setValue(1, "World");
 *
 * assertTrue(Arrays.equals(new String[]{"Hello", World}, someBean.getNames()));
 * assertTrue(Arrays.equals(new String[]{"Hello", World}, bean.getValue()));
 * </pre>
 *
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class JavaIndexedProperty extends JavaProperty implements IndexedProperty {

    private static final long serialVersionUID = 3014890786938775513L;

    public JavaIndexedProperty(JavaBean<?> bean, JavaPropertyDesc indexedPropertyDescriptor) {
        super(bean, indexedPropertyDescriptor);
    }

    public JavaIndexedPropertyDesc getJavaPropertyDesc() {
        return (JavaIndexedPropertyDesc) super.getJavaPropertyDesc();
    }

    @Override
    public IndexedPropertyDesc getPropertyDesc() {
        return (IndexedPropertyDesc) super.getPropertyDesc();
    }

    /**
     * Get the element value at the specified index of this
     * {@link JavaIndexedProperty}.
     *
     * @param index the index of the element to get.
     * @return the element value at the specified index of this
     * {@link JavaIndexedProperty}.
     * @since 1.2.0;
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(int index) {
        JavaBean<?> bean = getBean();
        Method indexedReadMethod = getIndexedReadMethod();
        if (indexedReadMethod == null) {

            throw new PropertyReadException(bean.getBeanClass().getType(), getPropertyDesc().getName());
        }
        try {
            Object target = bean.getObject();
            Object elementValue = invoke(indexedReadMethod, target, index);
            return (T) elementValue;
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            throw new PropertyReadException(bean.getBeanClass().getType(), getPropertyDesc().getName(), targetException);
        } catch (IllegalAccessException e) {
            throw new PropertyReadException(bean.getBeanClass().getType(), getPropertyDesc().getName(), e);
        }
    }

    /**
     * set the element value at the specified index of this
     * {@link JavaIndexedProperty}.
     *
     * @param index        the index of the element to set.
     * @param elementValue the element to set at the specified index.
     * @since 1.2.0;
     */
    @Override
    public void setValue(int index, Object elementValue) {
        JavaBean<?> bean = getBean();

        Method indexedWriteMethod = getIndexedWriteMethod();
        if (indexedWriteMethod == null) {

            throw new PropertyWriteException(bean.getBeanClass().getType(), getPropertyDesc().getName());
        }
        try {
            Object target = bean.getObject();
            invoke(indexedWriteMethod, target, index, elementValue);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            throw new PropertyWriteException(bean.getBeanClass().getType(), getPropertyDesc().getName(), targetException);
        } catch (IllegalAccessException e) {
            throw new PropertyWriteException(bean.getBeanClass().getType(), getPropertyDesc().getName(), e);
        }
    }

    /**
     * The Method that represents this {@link JavaIndexedProperty}'s index write
     * accessor (e.g. setPROPERTY(int index, TYPE value)).
     *
     * @return the Method that represents this {@link JavaIndexedProperty}'s index
     * write accessor (e.g. setPROPERTY(int index, TYPE value)) or
     * <code>null</code> if no write index method exists.
     * @since 1.2.0;
     */
    protected final Method getIndexedWriteMethod() {
        JavaIndexedPropertyDesc indexedPropertyDescriptor = getJavaPropertyDesc();
        Method indexedWriteMethod = indexedPropertyDescriptor.getJavaPropertyDescriptor().getIndexedWriteMethod();
        return indexedWriteMethod;
    }

    /**
     * The Method that represents this {@link JavaIndexedProperty}'s index read
     * accessor (e.g. TYPE getPROPERTY(int index)).
     *
     * @return the Method that represents this {@link JavaIndexedProperty}'s index
     * read accessor (e.g. TYPE getPROPERTY(int index)) or
     * <code>null</code> if no read index method exists.
     * @since 1.2.0;
     */
    protected final Method getIndexedReadMethod() {
        JavaIndexedPropertyDesc indexedPropertyDescriptor = getJavaPropertyDesc();
        Method indexedWriteMethod = indexedPropertyDescriptor.getJavaPropertyDescriptor().getIndexedReadMethod();
        return indexedWriteMethod;
    }

    /**
     * The name of this property + "[]" to indicate that it is an indexed
     * property.
     */
    @Override
    public String toString() {
        return super.getName() + "[]";
    }

    @Override
    public int hashCode() {
        if (isReadable()) {
            return hashCodeByArray();
        } else {
            return hashCodeByIndex();
        }
    }

    private int hashCodeByArray() {
        Object[] value = getValue();
        return Arrays.hashCode(value);
    }

    private int hashCodeByIndex() {
        final int prime = 31;
        int result = 1;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                Object value = getValue(i);
                result = prime * result + (value == null ? 0 : value.hashCode());
            } catch (PropertyReadException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IndexOutOfBoundsException) {
                    break;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JavaIndexedProperty other = (JavaIndexedProperty) obj;

        JavaIndexedPropertyDesc propertyDescriptor = getJavaPropertyDesc();
        JavaIndexedPropertyDesc otherPropertyDescriptor = other.getJavaPropertyDesc();

        if (!propertyDescriptor.equals(otherPropertyDescriptor)) {
            return false;
        }

        if (isReadable()) {
            return equalsByArray(other);
        } else {
            return equalsByIndex(other);
        }
    }

    private boolean equalsByArray(JavaIndexedProperty other) {
        if (!other.isReadable()) {
            return false;
        }

        Object[] value = getValue();
        Object[] otherValue = other.getValue();

        return Arrays.equals(value, otherValue);
    }

    private boolean equalsByIndex(JavaIndexedProperty other) {
        boolean equal = true;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                Object value = getValue(i);

                try {
                    Object otherValue = other.getValue(i);

                    equal = Objects.equals(value, otherValue);
                    if (!equal) {
                        break;
                    }
                } catch (PropertyReadException e) {
                    equal = false;
                    Throwable cause = e.getCause();
                    if (cause instanceof IndexOutOfBoundsException) {
                        break;
                    } else {
                        throw new RuntimeException(e);
                    }
                }

            } catch (PropertyReadException e) {
                try {
                    other.getValue(i);
                    equal = false;
                } catch (PropertyReadException pe) {
                    Throwable cause = pe.getCause();
                    if (!(cause instanceof IndexOutOfBoundsException)) {
                        throw new RuntimeException(e);
                    }
                }

                Throwable cause = e.getCause();
                if (cause instanceof IndexOutOfBoundsException) {
                    break;
                } else {
                    throw new RuntimeException(e);
                }
            }

        }

        return equal;
    }

}
