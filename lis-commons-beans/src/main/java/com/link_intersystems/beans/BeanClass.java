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
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Predicate;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.Class2;
import com.link_intersystems.lang.reflect.SignaturePredicate;

/**
 * A {@link BeanClass} provides features for handling common bean issues.
 *
 * @author René Link
 *         <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @param <T>
 *            the type of the bean.
 * @since 1.2.0.0
 */
public class BeanClass<T> extends Class2<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5446272789930350423L;

	private static final Map<Class<?>, BeanClass<?>> CLASS_TO_BEANCLASS = new HashMap<Class<?>, BeanClass<?>>();

	private transient Map<String, PropertyDescriptor> propertyDescriptors;

	private transient PropertyNames propertyNames;

	/**
	 * Constructs a new {@link BeanClass} for the specified type. The type
	 * described by the className string must fulfill the java bean
	 * specification and declare a public default constructor.
	 *
	 * @param className
	 * @return a {@link Class2} object that represents the {@link Class} defined
	 *         by the full qualified class name.
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> BeanClass<T> get(String className) throws ClassNotFoundException {
		Class<T> classForName = (Class<T>) Class.forName(className);
		return get(classForName);
	}

	/**
	 * Constructs a new {@link BeanClass} for the specified clazz. The given
	 * class must fulfill the java bean specification and declare a public
	 * default constructor.
	 *
	 * @param clazz
	 * @return a {@link Class2} for the given {@link Class}.
	 * @throws IllegalArgumentException
	 *             if the clazz argument does not declare a public default
	 *             constructor.
	 * @since 1.2.0.0
	 */
	public static <T> BeanClass<T> getStrict(Class<T> clazz) {
		Assert.notNull("clazz", clazz);
		BeanClass<T> class2 = get(clazz);
		if (class2 == null) {
			if (!hasBeanConstructor(clazz)) {
				throw new IllegalArgumentException(
						"Class " + clazz.getCanonicalName() + " does not declare a public default constructor "
								+ "and therefore does not fulfill the bean specification");
			}
		}
		return class2;
	}

	/**
	 * Constructs a new {@link BeanClass} for the specified clazz. The given
	 * class must not declare a public default constructor.
	 *
	 * @param clazz
	 * @return a {@link Class2} for the given {@link Class}.
	 * @throws IllegalArgumentException
	 *             if the clazz argument does not declare a public default
	 *             constructor.
	 * @since 1.2.0.0
	 *
	 * @see #getStrict(Class)
	 */
	@SuppressWarnings("unchecked")
	public static <T> BeanClass<T> get(Class<T> clazz) {
		Assert.notNull("clazz", clazz);
		BeanClass<T> class2 = (BeanClass<T>) CLASS_TO_BEANCLASS.get(clazz);
		if (class2 == null) {
			class2 = new BeanClass<T>(clazz);
			CLASS_TO_BEANCLASS.put(clazz, class2);
		}
		return class2;
	}

	/**
	 * Has the clazz a public default constructor?
	 *
	 * @param clazz
	 * @return
	 */
	private static boolean hasBeanConstructor(Class<?> clazz) {
		try {
			Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
			int modifiers = defaultConstructor.getModifiers();
			return Modifier.isPublic(modifiers);
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	protected BeanClass(Class<T> beanType) {
		super(beanType);
	}

	/**
	 *
	 * @return a map whose keys are the property names of the properties that
	 *         this class defines according to the java bean specification. The
	 *         values are the corresponding {@link PropertyDescriptor}s. The
	 *         returned Map is unmodifiable.
	 * @since 1.2.0.0
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			propertyDescriptors = Collections.unmodifiableMap(getPropertyDescriptors(null));
		}
		return propertyDescriptors;
	}

	/**
	 * @param stopClass
	 *            the class to stop {@link PropertyDescriptor} resolution. Must
	 *            be a superclass of this class. If the stop class is not null
	 *            then all {@link PropertyDescriptor}s are contained in the
	 *            result map that this class and every class along the hierarchy
	 *            until the stop class has. The {@link PropertyDescriptor} of
	 *            the stop class are not included.
	 * @return a map whose keys are the property names of the properties that
	 *         this class defines according to the java bean specification. The
	 *         values are the corresponding {@link PropertyDescriptor}s. The
	 *         returned {@link Map} can be modified by clients without
	 *         interfering this object's state.
	 * @throws IllegalArgumentException
	 *             if the stop class is not a super class of this class or
	 *             another Exception occurs while resolving the
	 *             {@link PropertyDescriptor}s. The cause might be an
	 *             {@link IntrospectionException}.
	 * @since 1.2.0.0
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptors(Class<?> stopClass) throws IllegalStateException {
		Class<T> beanType = getType();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanType, stopClass);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			Map<String, PropertyDescriptor> propertyDescriptorsMap = new LinkedHashMap<>();

			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				propertyDescriptorsMap.put(propertyDescriptor.getName(), propertyDescriptor);
			}
			return propertyDescriptorsMap;
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(
					"Unable to build property map for " + beanType + " with stopClass " + stopClass, e);
		}
	}

	/**
	 * Returns true if the method is a method to access a property (either get,
	 * is, set). The equality is based on the method's signature. This means
	 * that even property accessor methods in super classes will be recognized
	 * as property accessor methods of this {@link BeanClass}.
	 *
	 * @param method
	 *            the method to test if it is a property accessor method of this
	 *            class.
	 * @return true if the given method is a property accessor method of this
	 *         class.
	 * @since 1.2.0.0
	 */
	@SuppressWarnings("unchecked")
	public boolean isPropertyAccessor(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();
		Class<T> beanType = getType();
		boolean isInHierarchy = declaringClass.isAssignableFrom(beanType);
		if (!isInHierarchy) {
			return false;
		}
		Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors();
		Collection<PropertyDescriptor> propertyDescriptorCollection = propertyDescriptors.values();
		Predicate<Object> predicate = new SignaturePredicate(method);
		Iterator<Method> propertyMethodsIterator = IteratorUtils.objectGraphIterator(
				propertyDescriptorCollection.iterator(), PropertyDescriptor2AccessorsTransformer.INSTANCE);
		Iterator<Method> filteredIterator = IteratorUtils.filteredIterator(propertyMethodsIterator, predicate);
		return filteredIterator.hasNext();
	}

	/**
	 * Returns the {@link PropertyDescriptor} for the property name.
	 *
	 * @param propertyName
	 *            the name of the property.
	 * @return the {@link PropertyDescriptor} for the property name or
	 *         <code>null</code> if none exists.
	 * @since 1.2.0.0
	 */
	public PropertyDescriptor getPropertyDescriptor(String propertyName) {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptorInternal(propertyName);
		if (propertyDescriptor == null) {
			Class<T> beanType = getType();
			throw new NoSuchPropertyException(beanType, propertyName);
		}
		return propertyDescriptor;
	}

	PropertyDescriptor getPropertyDescriptorInternal(String propertyName) {
		Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors();
		PropertyDescriptor propertyDescriptor = propertyDescriptors.get(propertyName);
		return propertyDescriptor;
	}

	/**
	 * A {@link Bean} instance factory of this {@link BeanClass}.
	 *
	 * @return creates a new {@link Bean} instance of this {@link BeanClass}.
	 */
	public Bean<T> newBeanInstance() {
		try {
			T newBeanObj = newInstance();
			Bean<T> newBean = new Bean<T>(newBeanObj);
			return newBean;
		} catch (Exception e) {
			throw new IllegalStateException(
					"Bean " + getType().getCanonicalName()
							+ " throws an exception in default constructor. Does it have a default constructor (a strict BeanClass). See BeanClass.getStrict(Class<T>)",
					e);
		}
	}

	/**
	 *
	 * @return this {@link BeanClass}'s property names, simple and indexed
	 *         properties.
	 */
	public List<String> getPropertyNames() {
		if (this.propertyNames == null) {
			this.propertyNames = new PropertyNames(getPropertyDescriptors());
		}
		return propertyNames.getPropertyNames();
	}

	/**
	 *
	 * @return this {@link BeanClass}'s property names, excluding indexed
	 *         properties.
	 */
	public List<String> getSimplePropertyNames() {
		if (this.propertyNames == null) {
			this.propertyNames = new PropertyNames(getPropertyDescriptors());
		}
		return propertyNames.getSimplePropertyNames();
	}

	/**
	 *
	 * @return this {@link BeanClass}'s property names of indexed properties
	 *         only.
	 */
	public List<String> getIndexedPropertyNames() {
		if (this.propertyNames == null) {
			this.propertyNames = new PropertyNames(getPropertyDescriptors());
		}
		return propertyNames.getIndexedPropertyNames();
	}

	private static class PropertyNames {
		private List<String> propertyNames;

		private List<String> indexedPropertyNames;

		private List<String> simplePropertyNames;

		public PropertyNames(Map<String, PropertyDescriptor> propertyDescriptorMap) {
			List<String> propertyNames = new ArrayList<String>();
			List<String> simplePropertyNames = new ArrayList<String>();
			List<String> indexedPropertyNames = new ArrayList<String>();

			Set<Entry<String, PropertyDescriptor>> propertyDescriptorEntries = propertyDescriptorMap.entrySet();
			for (Entry<String, PropertyDescriptor> propertyDescriptorEntry : propertyDescriptorEntries) {
				PropertyDescriptor propertyDescriptor = propertyDescriptorEntry.getValue();
				String name = propertyDescriptor.getName();
				propertyNames.add(name);
				if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
					indexedPropertyNames.add(name);
				} else {
					simplePropertyNames.add(name);
				}
			}
			this.propertyNames = Collections.unmodifiableList(propertyNames);
			this.simplePropertyNames = Collections.unmodifiableList(simplePropertyNames);
			this.indexedPropertyNames = Collections.unmodifiableList(indexedPropertyNames);
		}

		public List<String> getPropertyNames() {
			return propertyNames;
		}

		public List<String> getIndexedPropertyNames() {
			return indexedPropertyNames;
		}

		public List<String> getSimplePropertyNames() {
			return simplePropertyNames;
		}

	}
}
