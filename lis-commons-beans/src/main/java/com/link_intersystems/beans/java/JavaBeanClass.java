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

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanEventTypes;
import com.link_intersystems.beans.BeanInstantiationException;
import com.link_intersystems.beans.PropertyDescList;
import com.link_intersystems.lang.reflect.SignaturePredicate;

import java.beans.*;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    private transient Map<Method, PropertyDescriptor> propertyDescriptorsByMethod;
    private transient JavaPropertyDescriptors propertyDescriptors;

    private BeanInfo beanInfo;
    private BeanEventTypes beanEventTypes;
    private PropertyDescList properties;
    private List<JavaPropertyDesc> javaPropertyDescs;

    public JavaBeanClass(Class<T> beanType) throws IntrospectionException {
        this(beanType, null);
    }

    public JavaBeanClass(Class<T> beanType, Class<?> stopClass) throws IntrospectionException {
        beanInfo = Introspector.getBeanInfo(beanType, stopClass);
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
            propertyDescriptors = createJavaPropertyDescriptors();
        }
        return propertyDescriptors;
    }

    private JavaPropertyDescriptors createJavaPropertyDescriptors() throws IllegalStateException {
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        return new JavaPropertyDescriptors(propertyDescriptors);
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
            String msg = "Bean " +
                    getType().getCanonicalName() +
                    " throws an exception while invoking the default constructor." +
                    " Does it have a public default constructor?" +
                    " See BeanClass.getStrict(Class<T>)";
            throw new BeanInstantiationException(msg, e);
        }
    }

    @Override
    public JavaBean<T> getBean(T bean) {
        return new JavaBean<>(this, bean);
    }

    @Override
    public BeanEventTypes getBeanEventTypes() {
        if (beanEventTypes == null) {
            beanEventTypes = createBeanEventTypes();
        }
        return beanEventTypes;
    }

    private BeanEventTypes createBeanEventTypes() {
        EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
        List<JavaBeanEventType> beanEventTypes = stream(eventSetDescriptors)
                .map(JavaBeanEventType::new)
                .collect(Collectors.toList());
        return new BeanEventTypes(beanEventTypes);
    }

    @Override
    public boolean isListenerSupported(Class<?> listenerClass) {
        BeanEventTypes beanEvents = getBeanEventTypes();
        return beanEvents.stream().anyMatch(be -> be.isApplicable(listenerClass));
    }

    @Override
    public PropertyDescList getProperties() {
        if (this.properties == null) {
            this.properties = new PropertyDescList(getJavaPropertyDescs());
        }
        return properties;
    }

    List<JavaPropertyDesc> getJavaPropertyDescs() {
        if (javaPropertyDescs == null) {
            javaPropertyDescs = getJavaPropertyDescriptors().stream()
                    .map(this::toPropertyDesc)
                    .collect(Collectors.toList());
        }
        return javaPropertyDescs;
    }

    private JavaPropertyDesc toPropertyDesc(PropertyDescriptor pd) {
        if (pd instanceof IndexedPropertyDescriptor) {
            return new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) pd);
        } else {
            return new JavaPropertyDesc(pd);
        }
    }

    @Override
    public String toString() {
        return "JavaBeanClass{" +
                "name=" + getName() +
                ", type=" + getType().getName() +
                '}';
    }
}
