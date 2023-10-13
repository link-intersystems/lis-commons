package com.link_intersystems.beans.java.record;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.RecordComponent;

class RecordBeanInfo extends SimpleBeanInfo {

    private final BeanDescriptor beanDescriptor;
    private PropertyDescriptor[] propertyDescriptors;

    public RecordBeanInfo(Class<?> recordClass) throws IntrospectionException {
        if (!recordClass.isRecord()) {
            throw new IntrospectionException(recordClass + " is not a Java record.");
        }

        beanDescriptor = new BeanDescriptor(recordClass);
        propertyDescriptors = createPropertyDescriptors(recordClass.getRecordComponents());
    }

    private PropertyDescriptor[] createPropertyDescriptors(RecordComponent[] recordComponents) throws IntrospectionException {
        PropertyDescriptor[] descriptors = new PropertyDescriptor[recordComponents.length];

        for (int i = 0; i < recordComponents.length; i++) {
            RecordComponent recordComponent = recordComponents[i];
            descriptors[i] = new PropertyDescriptor(recordComponent.getName(), recordComponent.getAccessor(), null);
        }

        return descriptors;
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return beanDescriptor;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors.clone();
    }
}
