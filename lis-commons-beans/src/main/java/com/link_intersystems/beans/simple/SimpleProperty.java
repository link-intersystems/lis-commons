package com.link_intersystems.beans.simple;

import com.link_intersystems.beans.*;

/**
 * @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SimpleProperty {

    private PropertyDesc<?> propertyDesc;
    private BeanClass<Object> beanClass;

    public SimpleProperty(PropertyDesc<?> propertyDesc, BeanClass<Object> beanClass) {
        this.propertyDesc = propertyDesc;
        this.beanClass = beanClass;
    }

    public String getName() {
        return propertyDesc.getName();
    }

    public Class<?> getType() {
        return propertyDesc.getType();
    }

    public Object get(Object bean) throws PropertyReadException {
        Bean<Object> beanObj = beanClass.getBean(bean);
        try {
            Property property = beanObj.getProperty(propertyDesc);
            return property.getValue();
        } catch (NoSuchPropertyException e) {
            throw new PropertyReadException(beanObj.getBeanClass().getType(), propertyDesc.getName(), e);
        }
    }

    public void set(Object bean, Object value) {
        Bean<Object> beanObj = beanClass.getBean(bean);
        try {
            Property property = beanObj.getProperty(propertyDesc);
            property.setValue(value);
        } catch (NoSuchPropertyException | IllegalArgumentException e) {
            throw new PropertyWriteException(beanObj.getBeanClass().getType(), propertyDesc.getName(), e);
        }
    }
}
