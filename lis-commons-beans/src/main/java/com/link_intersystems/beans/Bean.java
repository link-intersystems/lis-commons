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

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.link_intersystems.lang.Assert;

/**
 * A {@link Bean} is a wrapper for any java object that fulfills the <a href=
 * "http://download.oracle.com/otndocs/jcp/7224-javabeans-1.01-fr-spec-oth-JSpec/"
 * target="_blank">java bean specification</a> to access the bean's properties
 * in a convenience way.
 *
 * @author René Link
 *         <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @param <T>
 *            the type of the bean.
 * @since 1.2.0.0
 */
public class Bean<T> {

	private T bean;

	private BeanClass<T> beanClass;

	private Map<String, Property<?>> properties = new HashMap<String, Property<?>>();

	private Map<String, IndexedProperty<?>> indexedProperties = new HashMap<String, IndexedProperty<?>>();

	private transient Map<String, BeanEvent> beanEvents;

	/**
	 * Constructs a new {@link Bean} based on the beanClass. The bean class must
	 * fulfill the java bean specification. The bean that this {@link Bean}
	 * represents is lazy initialized by instantiating a bean using the default
	 * constructor of the beanClass.
	 *
	 * @param beanClass
	 *            the type of the bean that this {@link Bean} represents.
	 * @since 1.2.0.0
	 */
	public static <T> Bean<T> strictBean(Class<T> beanClass) {
		return new Bean<>(BeanClass.getStrict(beanClass));
	}

	/**
	 * Constructs a new {@link Bean} based on the beanClass. The bean class must
	 * fulfill the java bean specification. The bean that this {@link Bean}
	 * represents is lazy initialized by instantiating a bean using the default
	 * constructor of the beanClass.
	 *
	 * @param beanClass
	 *            the type of the bean that this {@link Bean} represents.
	 * @since 1.2.0.0
	 */
	public static <T> Bean<T> niceBean(Class<T> beanClass) {
		return new Bean<>(BeanClass.get(beanClass));
	}

	/**
	 * Constructs a new {@link Bean} for the given bean object.
	 *
	 * @param bean
	 *            the bean object.
	 * @since 1.2.0.0
	 */
	@SuppressWarnings("unchecked")
	public Bean(T bean) {
		Assert.notNull("bean", bean);
		this.bean = bean;
		beanClass = (BeanClass<T>) BeanClass.get(bean.getClass());
	}

	private Bean(BeanClass<T> beanClass) {
		Assert.notNull("beanClass", beanClass);
		this.beanClass = beanClass;
	}

	/**
	 * Get the {@link IndexedProperty} of this bean with the property name.
	 *
	 * @param propertyName
	 *            the name of the indexed property.
	 * @return the indexed property, if any.
	 * @throws NoSuchPropertyException
	 *             if the property does not exist.
	 * @throws PropertyAccessException
	 *             if the property could not be accessed.
	 * @since 1.2.0.0
	 */
	public <PT> IndexedProperty<PT> getIndexedProperty(String propertyName) {
		IndexedProperty<PT> indexedProperty = getIndexedPropertyInternal(propertyName);
		if (indexedProperty == null) {
			throw new NoSuchPropertyException(bean.getClass(), propertyName);
		}
		return indexedProperty;
	}

	@SuppressWarnings("unchecked")
	private <PT> IndexedProperty<PT> getIndexedPropertyInternal(String propertyName) {
		IndexedProperty<PT> indexedProperty = (IndexedProperty<PT>) indexedProperties.get(propertyName);
		if (indexedProperty == null) {
			PropertyDescriptor propertyDescriptor = beanClass.getPropertyDescriptor(propertyName);
			if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
				IndexedPropertyDescriptor indexedPropertyDescriptor = (IndexedPropertyDescriptor) propertyDescriptor;
				indexedProperty = new IndexedProperty<PT>(this, indexedPropertyDescriptor);
				indexedProperties.put(propertyName, indexedProperty);
				properties.put(propertyName, indexedProperty);
			}
		}
		return indexedProperty;
	}

	private List<IndexedProperty<?>> getIndexedPropertiesInternal() {
		List<IndexedProperty<?>> indexedProperties = new ArrayList<>();

		List<String> indexedPropertyNames = beanClass.getIndexedPropertyNames();

		for (String indexedPropertyName : indexedPropertyNames) {
			indexedProperties.add(getIndexedProperty(indexedPropertyName));
		}

		return indexedProperties;
	}

	private List<Property<?>> getSimplePropertiesInternal() {
		List<Property<?>> properties = new ArrayList<>();

		List<String> propertyNames = beanClass.getSimplePropertyNames();

		for (String propertyName : propertyNames) {
			properties.add(getProperty(propertyName));
		}

		return properties;
	}

	private List<Property<?>> getPropertiesInternal() {
		List<Property<?>> properties = new ArrayList<>();

		List<String> propertyNames = beanClass.getPropertyNames();

		for (String propertyName : propertyNames) {
			properties.add(getProperty(propertyName));
		}

		return properties;
	}

	/**
	 * Get the {@link Property} of this bean with the property name.
	 *
	 * @param propertyName
	 *            the name of the indexed property.
	 * @return the property, if any.
	 * @throws NoSuchPropertyException
	 *             if the property does not exist.
	 * @throws PropertyAccessException
	 *             if the property could not be accessed.
	 * @since 1.2.0.0
	 */
	public <PT> Property<PT> getProperty(String propertyName) {
		Property<PT> property = getPropertyInternal(propertyName);
		if (property == null) {
			throw new NoSuchPropertyException(bean.getClass(), propertyName);
		}
		return property;
	}

	/**
	 *
	 * @param propertyName
	 * @return true if either a simple property or an indexed property with the
	 *         given name exists.
	 */
	public boolean hasAnyProperty(String propertyName) {
		return hasProperty(propertyName) || hasIndexedProperty(propertyName);
	}

	/**
	 *
	 * @param propertyName
	 * @return true if a property with the given name exists.
	 */
	public boolean hasProperty(String propertyName) {
		return getPropertyInternal(propertyName) != null;
	}

	/**
	 *
	 * @param propertyName
	 * @return true if an indexed property with the given name exists.
	 */
	public boolean hasIndexedProperty(String propertyName) {
		return getIndexedPropertyInternal(propertyName) != null;
	}

	@SuppressWarnings("unchecked")
	private <PT> Property<PT> getPropertyInternal(String propertyName) {
		IndexedProperty<PT> indexedProperty = getIndexedPropertyInternal(propertyName);
		if (indexedProperty != null) {
			return (Property<PT>) indexedProperty;
		}

		Property<PT> property = (Property<PT>) properties.get(propertyName);
		if (property == null) {
			PropertyDescriptor propertyDescriptor = beanClass.getPropertyDescriptorInternal(propertyName);
			if (propertyDescriptor != null) {
				property = new Property<PT>(this, propertyDescriptor);
				properties.put(propertyName, property);
			}
		}
		return property;
	}

	private T getTarget() {
		if (bean == null) {
			try {
				Bean<T> newBeanInstance = beanClass.newBeanInstance();
				bean = newBeanInstance.getTarget();
			} catch (Exception e) {
				throw new IllegalStateException("Bean " + getBeanClass()
						+ " can not be instantiated. Is it a nice Bean. See Bean.niceBean(Class<T>)", e);
			}
		}
		return bean;
	}

	public T getBean() {
		return getTarget();
	}

	/**
	 *
	 * @return the {@link BeanClass} of this {@link Bean}.
	 */
	public BeanClass<T> getBeanClass() {
		return beanClass;
	}

	public void removeListener(Object listener) {
		if (listener == null) {
			return;
		}

		BeanEvent applicableBeanEvent = getApplicableBeanEvent(listener);

		if (applicableBeanEvent == null) {
			String msg = MessageFormat.format("{0} can not handle listener {1}", getBeanClass(), listener.getClass());
			throw new UnsupportedOperationException(msg);
		}

		applicableBeanEvent.removeListener(listener);
	}

	public void addListener(Object listener) {
		if (listener == null) {
			return;
		}

		BeanEvent applicableBeanEvent = getApplicableBeanEvent(listener);

		if (applicableBeanEvent == null) {
			String msg = MessageFormat.format("{0} can not handle listener {1}", getBeanClass(), listener.getClass());
			throw new UnsupportedOperationException(msg);
		}

		applicableBeanEvent.addListener(listener);
	}

	private BeanEvent getApplicableBeanEvent(Object listener) {
		Map<String, BeanEvent> beanEvents = getBeanEvents();

		BeanEvent applicableBeanEvent = null;
		for (BeanEvent beanEvent : beanEvents.values()) {
			if (beanEvent.isApplicable(listener)) {
				applicableBeanEvent = beanEvent;
				break;
			}
		}
		return applicableBeanEvent;
	}

	public Map<String, BeanEvent> getBeanEvents() {
		if (beanEvents == null) {
			beanEvents = Collections.unmodifiableMap(getBeanEvents(null));
		}
		return beanEvents;
	}

	public Map<String, BeanEvent> getBeanEvents(Class<?> stopClass) {
		Class<T> beanType = beanClass.getType();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanType, stopClass);
			EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
			Map<String, BeanEvent> beanEventMap = new HashMap<String, BeanEvent>();

			for (EventSetDescriptor eventSetDescriptor : eventSetDescriptors) {
				BeanEvent beanEvent = new BeanEvent(this, eventSetDescriptor);
				String eventName = beanEvent.getName();
				beanEventMap.put(eventName, beanEvent);
			}

			return beanEventMap;
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(
					"Unable to build property map for " + beanType + " with stopClass " + stopClass, e);
		}
	}

	public <L> BeanEventSupport<T, L> newBeanEventSupport() {
		return new BeanEventSupport<T, L>();
	}

	public boolean propertiesEqual(Bean<T> otherBean) {
		List<Property<?>> properties = getPropertiesInternal();
		List<Property<?>> otherProperties = otherBean.getPropertiesInternal();
		return properties.equals(otherProperties);
	}

}
