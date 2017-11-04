/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Objects;

import com.link_intersystems.beans.PropertyAccessException.PropertyAccessType;
import com.link_intersystems.lang.Assert;

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
 *
 * @author René Link
 *         <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <TYPE>
 *            the {@link Property}'s type.
 * @since 1.2.0.0
 */
public class Property<TYPE> implements Serializable, Formattable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6759158627808430975L;
	private Bean<?> bean;
	private PropertyDescriptor propertyDescriptor;

	private Class<TYPE> type;

	Property(Bean<?> bean, PropertyDescriptor propertyDescriptor) {
		Assert.notNull("bean", bean);
		Assert.notNull("propertyDescriptor", propertyDescriptor);
		this.bean = bean;
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * @return the bean object of this {@link Property}.
	 * @since 1.2.0.0
	 */
	protected final Object getBean() {
		return bean.getObject();
	}

	/**
	 * @return this {@link Property}'s getter method, if any.
	 * @since 1.2.0.0
	 */
	protected final Method getReadMethod() {
		return getPropertyDescriptor().getReadMethod();
	}

	/**
	 * @return this {@link Property}'s setter method, if any.
	 * @since 1.2.0.0
	 */
	protected final Method getWriteMethod() {
		return getPropertyDescriptor().getWriteMethod();
	}

	/**
	 * The {@link Property}'s type.
	 *
	 * @return the {@link Property}'s type.
	 * @since 1.2.0.0
	 */
	@SuppressWarnings("unchecked")
	public Class<TYPE> getType() {
		if (this.type == null) {
			Class<TYPE> type = null;
			Method readMethod = getReadMethod();
			if (readMethod != null) {
				type = (Class<TYPE>) readMethod.getReturnType();
			} else {
				Method writeMethod = getWriteMethod();
				Class<?>[] parameterTypes = writeMethod.getParameterTypes();
				type = (Class<TYPE>) parameterTypes[0];
			}
			this.type = type;
		}
		return type;
	}

	/**
	 * The name of this {@link Property}.
	 *
	 * @return name of this {@link Property}.
	 * @since 1.2.0.0
	 */
	public String getName() {
		return getPropertyDescriptor().getName();
	}

	/**
	 * The name of this {@link Property}.
	 *
	 * @return name of this {@link Property}.
	 * @since 1.2.0.0
	 */
	public String getDisplayName() {
		return getPropertyDescriptor().getDisplayName();
	}

	/**
	 * Creates a {@link PropertyEditor} for this {@link Property}.
	 *
	 * @return the property editor for this property.
	 * @throws PropertyEditorNotAvailableException
	 * @since 1.2.0.4
	 */
	public PropertyEditor createPropertiyEditor() throws PropertyEditorNotAvailableException {
		Object bean = getBean();

		PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(bean);

		if (propertyEditor == null) {
			Class<TYPE> propertyType = getType();
			propertyEditor = PropertyEditorManager.findEditor(propertyType);
		}

		if (propertyEditor == null) {
			String msg = MessageFormat.format("No property editor available for {0}", this);
			throw new PropertyEditorNotAvailableException(msg);
		}

		return propertyEditor;
	}

	/**
	 * Set this {@link Property}'s value by the {@link PropertyEditor}'s text
	 * representation.
	 *
	 * @param text
	 * @throws PropertyEditorNotAvailableException
	 * @since 1.2.0.5
	 */
	@SuppressWarnings("unchecked")
	public void setValueAsText(String text) throws PropertyEditorNotAvailableException {
		PropertyEditor propertiyEditor = createPropertiyEditor();
		propertiyEditor.setAsText(text);
		Object value = propertiyEditor.getValue();
		setValue((TYPE) value);
	}

	/**
	 * Get this {@link Property}'s value as text, according to the associated
	 * {@link PropertyEditor}'s return value.
	 *
	 * @param text
	 * @throws PropertyEditorNotAvailableException
	 * @since 1.2.0.5
	 */
	public String getValueAsText() throws PropertyEditorNotAvailableException {
		try {
			PropertyEditor propertiyEditor = createPropertiyEditor();
			return propertiyEditor.getAsText();
		} catch (PropertyEditorNotAvailableException e) {
			return null;
		}
	}

	/**
	 * Gets the value of this {@link Property}.
	 *
	 * @return the value of this property.
	 * @throws PropertyAccessException
	 *             if the property could not be accessed for any reason. If the
	 *             thrown {@link PropertyAccessException} has no cause this property
	 *             is not readable (has no property getter method).
	 * @since 1.2.0.0
	 */
	@SuppressWarnings("unchecked")
	public TYPE getValue() {
		Object target = getBean();
		Method readMethod = getReadMethod();
		if (readMethod == null) {
			throw new PropertyAccessException(this, PropertyAccessType.READ);
		}
		try {
			Object beanValue = invoke(readMethod, target);
			return (TYPE) beanValue;
		} catch (InvocationTargetException e) {
			throw new PropertyAccessException(this, PropertyAccessType.READ, e);
		} catch (IllegalAccessException e) {
			throw new PropertyAccessException(this, PropertyAccessType.READ, e);
		}
	}

	/**
	 * Encapsulation of a method invocation for better testing.
	 *
	 * @param method
	 * @param target
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected Object invoke(Method method, Object target, Object... args)
			throws IllegalAccessException, InvocationTargetException {
		Object beanValue = method.invoke(target, args);
		return beanValue;
	}

	/**
	 * Sets the value of this {@link Property}.
	 *
	 * @param propertyValue
	 *            the value to set.
	 *
	 * @throws PropertyAccessException
	 *             if this {@link Property}'s value could not be set. If the thrown
	 *             {@link PropertyAccessException} has no cause this property is not
	 *             writable (has no property setter method).
	 * @since 1.2.0.0
	 */
	public void setValue(TYPE propertyValue) {
		Object target = getBean();
		Method writeMethod = getWriteMethod();
		if (writeMethod == null) {
			throw new PropertyAccessException(this, PropertyAccessType.WRITE);
		}
		try {
			invoke(writeMethod, target, propertyValue);
		} catch (InvocationTargetException e) {
			throw new PropertyAccessException(this, PropertyAccessType.WRITE, e);
		} catch (IllegalAccessException e) {
			throw new PropertyAccessException(this, PropertyAccessType.WRITE, e);
		}
	}

	/**
	 * @inherited
	 * @since 1.2.0.0
	 */
	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		formatter.format("%s.%s", getBean().getClass().getCanonicalName(), getName());
	}

	/**
	 * Returns the {@link PropertyDescriptor} of this {@link Property}.
	 *
	 * @return
	 * @since 1.2.0.0
	 */
	public final PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	/**
	 * Returns true if this property is readable (has a getter method).
	 *
	 * @return true if this property is readable (has a getter method).
	 */
	public boolean isReadable() {
		return getReadMethod() != null;
	}

	/**
	 * Returns if this property is writable (has a setter method).
	 *
	 * @return true if this property is writable (has a setter method).
	 */
	public boolean isWritable() {
		return getWriteMethod() != null;
	}

	/**
	 * The name of this property.
	 */
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + propertyDescriptor.hashCode();
		Object value = getValue();

		if (value != null) {
			Class<? extends Object> valueClass = value.getClass();
			if (valueClass.isArray()) {
				result = prime * result + Arrays.deepHashCode((Object[]) value);
			} else {
				result = prime * result + value.hashCode();
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (!propertyDescriptor.equals(other.propertyDescriptor))
			return false;

		Object value = getValue();
		if (value == null) {
			return other.getValue() == null;
		} else {
			Object otherValue = other.getValue();

			if (otherValue == null) {
				return false;
			}

			Class<? extends Object> valueClass = value.getClass();
			Class<? extends Object> otherValueClass = otherValue.getClass();
			if (valueClass.isArray() && otherValueClass.isArray()) {
				return Objects.deepEquals(value, otherValue);
			} else if (!valueClass.isArray() && !otherValueClass.isArray()) {
				return value.equals(otherValue);
			} else {
				return false;
			}
		}
	}

	/**
	 * The declaring class of a {@link Property} is the class that first mentions
	 * the property. So it can be either the read or write method's declaring class.
	 * If the read and write methods are defined in different classes the uppermost
	 * class in the hierarchy is returned.
	 *
	 * @return
	 */
	public Class<?> getDeclaringClass() {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor();

		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();

		if (readMethod != null && writeMethod == null) {
			return readMethod.getDeclaringClass();
		} else if (readMethod == null && writeMethod != null) {
			return writeMethod.getDeclaringClass();
		} else if (readMethod != null && writeMethod != null) {
			Class<?> readDeclaringClass = readMethod.getDeclaringClass();
			Class<?> writeDeclaringClass = writeMethod.getDeclaringClass();

			if (readDeclaringClass.equals(writeDeclaringClass)
					|| readDeclaringClass.isAssignableFrom(writeDeclaringClass)) {
				return readDeclaringClass;
			} else {
				return writeDeclaringClass;
			}
		} else {
			String msg = MessageFormat.format("Can not determine declaring class of property {0}. " + //
					"Read and write method is null.", this);
			throw new IllegalStateException(msg);
		}

	}

}
