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

import java.beans.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * A {@link JavaBeanClass} provides features for handling common bean issues.
 *
 * @param <T> the type of the bean.
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class JavaBeanClass<T> extends BeanClass<T> implements Serializable {

    private static final long serialVersionUID = -5446272789930350423L;

    private BeanInfo beanInfo;

    private transient JavaPropertyDescriptors propertyDescriptors;

    private transient List<JavaPropertyDesc> javaPropertyDescs;

    private transient PropertyDescList allProperties;

    private BeanEventTypeList beanEventTypes;

    JavaBeanClass(Class<T> beanType) throws BeanClassException {
        this(beanType, null);
    }

    JavaBeanClass(Class<T> beanType, Class<?> stopClass) throws BeanClassException {
        this(getBeanInfo(beanType, stopClass));
    }

    private static BeanInfo getBeanInfo(Class<?> beanType, Class<?> stopClass) {
        try {
            return Introspector.getBeanInfo(beanType, stopClass);
        } catch (IntrospectionException e) {
            String msg = format("Unable to create BeanClass for ''{0}''. Stop at ''{1}''", beanType, stopClass);
            throw new BeanClassException(msg, e);
        }
    }

    protected JavaBeanClass(BeanInfo beanInfo) throws BeanClassException {
        this.beanInfo = requireNonNull(beanInfo);
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

        return propertyDescriptors.stream()
                .anyMatch(pd -> PropertyDescriptor2AccessorsTransformer.INSTANCE.apply(pd).stream().anyMatch(method::equals));
    }

    @Override
    public BeanInstanceFactory<T> getBeanInstanceFactory() {
        return new JavaBeanInstanceFactory<>(this);
    }

    @Override
    public BeanEventTypeList getBeanEventTypes() {
        if (beanEventTypes == null) {
            beanEventTypes = createBeanEventTypes();
        }
        return beanEventTypes;
    }

    private BeanEventTypeList createBeanEventTypes() {
        EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
        List<JavaBeanEventType> beanEventTypes = stream(eventSetDescriptors)
                .map(this::newJavaBeanEventType)
                .collect(toList());
        return new BeanEventTypeList(beanEventTypes);
    }

    private JavaBeanEventType newJavaBeanEventType(EventSetDescriptor eventSetDescriptor) {
        return new JavaBeanEventType(this, eventSetDescriptor);
    }

    @Override
    public boolean isListenerSupported(Class<?> listenerClass) {
        BeanEventTypeList beanEvents = getBeanEventTypes();
        return beanEvents.stream().anyMatch(be -> be.isApplicable(listenerClass));
    }

    @Override
    public PropertyDescList getAllProperties() {
        if (this.allProperties == null) {
            this.allProperties = new PropertyDescList(getJavaPropertyDescs());
        }
        return allProperties;
    }


    List<JavaPropertyDesc> getJavaPropertyDescs() {
        if (javaPropertyDescs == null) {
            javaPropertyDescs = getJavaPropertyDescriptors().stream()
                    .map(this::toPropertyDesc)
                    .collect(toList());
        }
        return javaPropertyDescs;
    }

    JavaPropertyDesc getPropertyDescByMethod(Method method) {
        return getAllProperties().stream()
                .map(JavaPropertyDesc.class::cast)
                .filter(jpd -> jpd.hasMethod(method))
                .findFirst()
                .orElse(null);
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
