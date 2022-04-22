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

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * A {@link JavaBean} is a wrapper for any java object that fulfills the <a href=
 * "http://download.oracle.com/otndocs/jcp/7224-javabeans-1.01-fr-spec-oth-JSpec/"
 * target="_blank">java bean specification</a> to access the bean's properties
 * in a convenience way.
 *
 * @param <T> the type of the bean.
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class JavaBean<T> extends Bean<T> {

    private PropertyList propertyList;
    private List<JavaProperty> javaProperties;

    public JavaBean(JavaBeanClass<T> beanClass, T bean) {
        super(beanClass, bean);
    }

    /**
     * @return the {@link JavaBeanClass} of this {@link JavaBean}.
     */
    @Override
    public JavaBeanClass<T> getBeanClass() {
        return (JavaBeanClass<T>) super.getBeanClass();
    }

    /**
     * Retuns all properties of this bean .
     *
     * @return all properties or an empty list.
     * @since 1.2.0;
     */
    @Override
    public PropertyList getAllProperties() {
        if (propertyList == null) {
            propertyList = new PropertyList(getJavaProperties());
        }
        return propertyList;
    }

    List<JavaProperty> getJavaProperties() {
        if (javaProperties == null) {
            javaProperties = getBeanClass().getJavaPropertyDescs().stream()
                    .map(this::toProperty).collect(toList());
        }
        return javaProperties;
    }

    private JavaProperty toProperty(JavaPropertyDesc propertyDesc) {
        if (propertyDesc instanceof JavaIndexedPropertyDesc) {
            return new JavaIndexedProperty(this, (JavaIndexedPropertyDesc) propertyDesc);
        } else {
            return new JavaProperty(this, propertyDesc);
        }
    }

    protected BeanEvent getApplicableBeanEvent(Object listener) {
        BeanClass<T> beanClass = getBeanClass();

        BeanEventTypeList beanEventTypes = beanClass.getBeanEventTypes();

        BeanEvent applicableBeanEvent = null;
        for (BeanEventType beanEvent : beanEventTypes) {
            if (beanEvent.isApplicable(listener)) {
                T beanObject = getBeanObject();
                applicableBeanEvent = new JavaBeanEvent(beanObject, (JavaBeanEventType) beanEvent);
                break;
            }
        }
        return applicableBeanEvent;
    }

    public boolean propertiesEqual(JavaBean<T> otherBean) {
        List<Property> properties = getProperties();
        List<Property> otherProperties = otherBean.getProperties();
        return properties.equals(otherProperties);
    }


}
