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

import com.link_intersystems.beans.*;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * Encapsulates access to a java bean property.
 *
 * <pre>
 * public class SomeBean {
 *
 *    private String internalName;
 *
 *    public void setName(String name){
 *      this.internalName = name;
 *    }
 *
 *    public String getName(){
 *    	return internalName;
 *    }
 *
 * }
 *
 * ...
 * SomeBean someBean = new SomeBean();
 *
 * Bean<SomeBean> bean = new Bean<SomeBean>();
 * Property<String> nameProp = bean.getProperty("name");
 * nameProp.setValue("Hello");
 *
 * assertEquals("Hello", someBean.getName());
 * </pre>
 *
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * <p>
 * the {@link JavaProperty}'s type.
 * @since 1.2.0;
 */
public class JavaProperty extends DefaultProperty implements Serializable, Property {

    private static final long serialVersionUID = -6759158627808430975L;

    JavaProperty(JavaBean<?> bean, JavaPropertyDesc propertyDescriptor) {
        super(bean, propertyDescriptor);
    }

    protected PropertyDescriptor getJavaPropertyDescriptor() {
        return ((JavaPropertyDesc) getPropertyDesc()).getJavaPropertyDescriptor();
    }

    /**
     * @return the bean object of this {@link JavaProperty}.
     * @since 1.2.0;
     */
    protected final JavaBean<?> getBean() {
        return (JavaBean<?>) super.getBean();
    }

    /**
     * The name of this {@link JavaProperty}.
     *
     * @return name of this {@link JavaProperty}.
     * @since 1.2.0;
     */
    public String getName() {
        return getPropertyDesc().getName();
    }

    /**
     * Creates a {@link PropertyEditor} for this {@link JavaProperty}.
     *
     * @return the property editor for this property.
     * @since 1.2.0;
     */
    public PropertyEditor createPropertyEditor() throws PropertyEditorNotAvailableException {
        JavaBean<?> beanObj = getBean();
        Object bean = beanObj.getBeanObject();

        PropertyDescriptor javaPropertyDescriptor = getJavaPropertyDescriptor();

        PropertyEditor propertyEditor = javaPropertyDescriptor.createPropertyEditor(bean);

        if (propertyEditor == null) {
            Class<?> propertyType = getPropertyDesc().getType();
            propertyEditor = PropertyEditorManager.findEditor(propertyType);
        }

        if (propertyEditor == null) {
            String msg = MessageFormat.format("No property editor available for {0}", this);
            throw new PropertyEditorNotAvailableException(msg);
        }

        return propertyEditor;
    }

    /**
     * Get this {@link JavaProperty}'s value as text, according to the associated
     * {@link PropertyEditor}'s return value.
     *
     * @since 1.2.0;
     */
    public String getValueAsText() throws PropertyEditorNotAvailableException {
        try {
            PropertyEditor propertiyEditor = createPropertyEditor();
            propertiyEditor.setValue(getValue());
            return propertiyEditor.getAsText();
        } catch (PropertyEditorNotAvailableException e) {
            return null;
        }
    }

    /**
     * Set this {@link JavaProperty}'s value by the {@link PropertyEditor}'s text
     * representation.
     *
     * @since 1.2.0;
     */
    public void setValueAsText(String text) throws PropertyEditorNotAvailableException {
        PropertyEditor propertiyEditor = createPropertyEditor();
        propertiyEditor.setAsText(text);
        Object value = propertiyEditor.getValue();
        setValue(value);
    }

    /**
     * Gets the value of this {@link JavaProperty}.
     *
     * @return the value of this property.
     * @throws PropertyReadException if the property could not be accessed for any reason. If the
     *                               thrown {@link PropertyReadException} has no cause this property
     *                               is not readable (has no property getter method).
     * @since 1.2.0;
     */
    @Override
    public <T> T getValue() {
        PropertyDesc propertyDesc = getPropertyDesc();
        return propertyDesc.getPropertyValue(getBean().getBeanObject());
    }

    /**
     * Sets the value of this {@link JavaProperty}.
     *
     * @param propertyValue the value to set.
     * @throws PropertyReadException if this {@link JavaProperty}'s value could not be set. If the thrown
     *                               {@link PropertyWriteException} has no cause this property is not
     *                               writable (has no property setter method).
     * @since 1.2.0;
     */
    @Override
    public void setValue(Object propertyValue) {
        PropertyDesc propertyDesc = getPropertyDesc();
        propertyDesc.setPropertyValue(getBean().getBeanObject(), propertyValue);
    }

    /**
     * Encapsulation of a method invocation for better testing.
     */
    protected Object invoke(Method method, Object target, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        Object beanValue = method.invoke(target, args);
        return beanValue;
    }

    /**
     * Returns true if this property is readable (has a getter method).
     *
     * @return true if this property is readable (has a getter method).
     */
    public boolean isReadable() {
        return getPropertyDesc().isReadable();
    }

    /**
     * Returns if this property is writable (has a setter method).
     *
     * @return true if this property is writable (has a setter method).
     */
    public boolean isWritable() {
        return getPropertyDesc().isWritable();
    }

    /**
     * The name of this property.
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * The declaring class of a {@link JavaProperty} is the class that first mentions
     * the property. So it can be either the read or write method's declaring class.
     * If the read and write methods are defined in different classes the uppermost
     * class in the hierarchy is returned.
     */
    public Class<?> getDeclaringClass() {
        return getPropertyDesc().getDeclaringClass();
    }
}
