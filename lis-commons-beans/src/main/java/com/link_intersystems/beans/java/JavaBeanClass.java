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
import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.Class2;
import com.link_intersystems.lang.reflect.SignaturePredicate;

import java.beans.*;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * A {@link JavaBeanClass} provides features for handling common bean issues.
 *
 * @param <T> the type of the bean.
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class JavaBeanClass<T> implements Serializable, BeanClass<T> {

    private static final long serialVersionUID = -5446272789930350423L;

    private static final Map<Class<?>, JavaBeanClass<?>> CLASS_TO_BEANCLASS = new HashMap<>();

    private transient Map<Method, PropertyDescriptor> propertyDescriptorsByMethod;
    private transient JavaPropertyDescriptors propertyDescriptors;

    private BeanInfo beanInfo;
    private BeanEventTypes<JavaBeanEventType> beanEventTypes;
    private PropertyDescList<JavaPropertyDesc> properties;

    protected JavaBeanClass(Class<T> beanType) throws IntrospectionException {
        beanInfo = Introspector.getBeanInfo(beanType, null);
    }

    protected JavaBeanClass(Class<T> beanType, Class<?> stopClass) throws IntrospectionException {
        beanInfo = Introspector.getBeanInfo(beanType, stopClass);
    }

    /**
     * Constructs a new {@link JavaBeanClass} for the specified type. The type described
     * by the className string must fulfill the java bean specification and declare
     * a public default constructor.
     *
     * @return a {@link Class2} object that represents the {@link Class} defined by
     * the full qualified class name.
     */
    @SuppressWarnings("unchecked")
    public static <T> JavaBeanClass<T> get(String className) throws ClassNotFoundException {
        Class<T> classForName = (Class<T>) Class.forName(className);
        return get(classForName);
    }

    /**
     * Constructs a new {@link JavaBeanClass} for the specified clazz. The given class
     * must fulfill the java bean specification and declare a public default
     * constructor.
     *
     * @return a {@link Class2} for the given {@link Class}.
     * @throws IllegalArgumentException if the clazz argument does not declare a
     *                                  public default constructor.
     * @since 1.2.0;
     */
    public static <T> JavaBeanClass<T> getStrict(Class<T> clazz) {
        Assert.notNull("clazz", clazz);
        JavaBeanClass<T> class2 = get(clazz);
        if (!hasBeanConstructor(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getCanonicalName() + " does not declare a public default constructor " + "and therefore does not fulfill the bean specification");
        }
        return class2;
    }

    /**
     * Constructs a new {@link JavaBeanClass} for the specified clazz. The given class
     * must not declare a public default constructor.
     *
     * @return a {@link Class2} for the given {@link Class}.
     * @throws IllegalArgumentException if the clazz argument does not declare a
     *                                  public default constructor.
     * @see #getStrict(Class)
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> JavaBeanClass<T> get(Class<T> clazz) {
        Assert.notNull("clazz", clazz);
        JavaBeanClass<T> class2 = (JavaBeanClass<T>) CLASS_TO_BEANCLASS.get(clazz);
        if (class2 == null) {
            try {
                class2 = new JavaBeanClass<>(clazz);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
            CLASS_TO_BEANCLASS.put(clazz, class2);
        }
        return class2;
    }

    /**
     * Has the clazz a public default constructor?
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

    @Override
    public String getName() {
        return beanInfo.getBeanDescriptor().getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getType() {
        return (Class<T>) beanInfo.getBeanDescriptor().getBeanClass();
    }

    /**
     * @return a map whose keys are the property names of the properties that this
     * class defines according to the java bean specification. The values
     * are the corresponding {@link PropertyDescriptor}s. The returned Map
     * is unmodifiable.
     * @since 1.2.0;
     */
    public JavaPropertyDescriptors getJavaPropertyDescriptors() {
        if (propertyDescriptors == null) {
            propertyDescriptors = getJavaPropertyDescriptors(null);
        }
        return propertyDescriptors;
    }

    /**
     * @param stopClass the class to stop {@link PropertyDescriptor} resolution.
     *                  Must be a superclass of this class. If the stop class is not
     *                  null then all {@link PropertyDescriptor}s are contained in
     *                  the result map that this class and every class along the
     *                  hierarchy until the stop class has. The
     *                  {@link PropertyDescriptor} of the stop class are not
     *                  included.
     * @return a map whose keys are the property names of the properties that this
     * class defines according to the java bean specification. The values
     * are the corresponding {@link PropertyDescriptor}s. The returned
     * {@link Map} can be modified by clients without interfering this
     * object's state.
     * @throws IllegalArgumentException if the stop class is not a super class of
     *                                  this class or another Exception occurs while
     *                                  resolving the {@link PropertyDescriptor}s.
     *                                  The cause might be an
     *                                  {@link IntrospectionException}.
     * @since 1.2.0;
     */
    public JavaPropertyDescriptors getJavaPropertyDescriptors(Class<?> stopClass) throws IllegalStateException {
        Class<T> beanType = getType();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanType, stopClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            return new JavaPropertyDescriptors(propertyDescriptors);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Unable to build property map for " + beanType + " with stopClass " + stopClass, e);
        }
    }

    /**
     * Returns true if the method is a method to access a property (either get, is,
     * set). The equality is based on the method's signature. This means that even
     * property accessor methods in super classes will be recognized as property
     * accessor methods of this {@link JavaBeanClass}.
     *
     * @param method the method to test if it is a property accessor method of this
     *               class.
     * @return true if the given method is a property accessor method of this class.
     * @since 1.2.0;
     */
    public boolean isPropertyAccessor(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        Class<T> beanType = getType();
        boolean isInHierarchy = declaringClass.isAssignableFrom(beanType);
        if (!isInHierarchy) {
            return false;
        }
        JavaPropertyDescriptors propertyDescriptors = getJavaPropertyDescriptors();
        SignaturePredicate predicate = new SignaturePredicate(method);

        return propertyDescriptors.stream()
                .anyMatch(pd -> PropertyDescriptor2AccessorsTransformer.INSTANCE.apply(pd).stream().anyMatch(predicate));
    }

    /**
     * Returns the {@link PropertyDescriptor} for the property name.
     *
     * @param propertyName the name of the property.
     * @return the {@link PropertyDescriptor} for the property name or
     * <code>null</code> if none exists.
     * @since 1.2.0;
     */
    public PropertyDescriptor getPropertyDescriptor(String propertyName) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptorInternal(propertyName);
        if (propertyDescriptor == null) {
            Class<T> beanType = getType();
            throw new NoSuchPropertyException(beanType, propertyName);
        }
        return propertyDescriptor;
    }

    /**
     * @return a {@link PropertyDescriptor} for the given method if the method is a
     * getter or setter of a property of this {@link JavaBeanClass}.
     * @since 1.2.0;
     */
    public PropertyDescriptor getPropertyDescriptor(Method method) {
        Map<Method, PropertyDescriptor> propertyDescriptorsByMethod = getPropertyDescriptorsByMethod();

        return propertyDescriptorsByMethod.getOrDefault(method, null);
    }

    private Map<Method, PropertyDescriptor> getPropertyDescriptorsByMethod() {
        if (propertyDescriptorsByMethod == null) {
            Map<Method, PropertyDescriptor> mapToBuild = new HashMap<>();

            for (PropertyDescriptor propertyDescriptor : getJavaPropertyDescriptors()) {
                Method readMethod = propertyDescriptor.getReadMethod();
                mapToBuild.put(readMethod, propertyDescriptor);

                Method writeMethod = propertyDescriptor.getWriteMethod();
                mapToBuild.put(writeMethod, propertyDescriptor);
            }

            this.propertyDescriptorsByMethod = mapToBuild;
        }
        return propertyDescriptorsByMethod;
    }

    /**
     * TODO make package scope again after refactoring
     */
    public PropertyDescriptor getPropertyDescriptorInternal(String propertyName) {
        return getJavaPropertyDescriptors().getByName(propertyName);
    }

    /**
     * A {@link JavaBean} instance factory of this {@link JavaBeanClass}.
     *
     * @return creates a new {@link JavaBean} instance of this {@link JavaBeanClass}.
     */
    @Override
    public JavaBean<T> newBeanInstance() throws BeanInstantiationException {
        Class<T> beanClass = getType();
        try {
            Constructor<T> defaultConstructor = beanClass.getDeclaredConstructor();
            T newBeanObj = defaultConstructor.newInstance();
            return getBean(newBeanObj);
        } catch (Exception e) {
            throw new BeanInstantiationException("Bean " + getType().getCanonicalName() + " throws an exception in default constructor. Does it have a default constructor (a strict BeanClass). See BeanClass.getStrict(Class<T>)", e);
        }
    }

    @Override
    public JavaBean<T> getBean(T bean) {
        return new JavaBean<>(bean);
    }

    @Override
    public BeanEventTypes<JavaBeanEventType> getBeanEventTypes() {
        if (beanEventTypes == null) {
            beanEventTypes = getBeanEventsInternal(null);
        }
        return beanEventTypes;
    }

    private BeanEventTypes<JavaBeanEventType> getBeanEventsInternal(Class<?> stopClass) {
        Class<T> beanType = getType();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanType, stopClass);
            EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
            List<JavaBeanEventType> beanEventTypes = stream(eventSetDescriptors)
                    .map(JavaBeanEventType::new)
                    .collect(Collectors.toList());
            return new BeanEventTypes<>(beanEventTypes);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Unable to build property map for " + beanType + " with stopClass " + stopClass, e);
        }
    }

    @Override
    public BeanEventTypes<JavaBeanEventType> getBeanEvents(Class<?> stopClass) {
        return getBeanEventsInternal(stopClass);
    }

    @Override
    public boolean isListenerSupported(Class<?> listenerClass) {
        BeanEventTypes<JavaBeanEventType> beanEvents = getBeanEventTypes();
        for (JavaBeanEventType beanEvent : beanEvents) {
            if (beanEvent.isApplicable(listenerClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PropertyDescList<JavaPropertyDesc> getProperties() {
        if (this.properties == null) {
            List<JavaPropertyDesc> desciptors = getJavaPropertyDescriptors().stream()
                    .map(this::toPropertyDesc)
                    .collect(Collectors.toList());
            this.properties = new PropertyDescList<>(desciptors);
        }
        return properties;
    }

    private JavaPropertyDesc toPropertyDesc(PropertyDescriptor pd) {
        if (pd instanceof IndexedPropertyDescriptor) {
            return new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) pd);
        } else {
            return new JavaPropertyDesc(pd);
        }
    }
}
