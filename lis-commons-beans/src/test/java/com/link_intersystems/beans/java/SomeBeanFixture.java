package com.link_intersystems.beans.java;

import java.beans.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SomeBeanFixture {

    public final String[] stringArrayPropertyValue = new String[]{"Hello", "World"};

    public final SomeBean someBean;

    public final JavaIndexedProperty readOnlyProperty;
    public final JavaIndexedProperty writeOnlyProperty;
    public final JavaIndexedProperty stringProperty;
    public final JavaIndexedPropertyDesc readOnlyPropertyDescriptor;
    public final JavaIndexedPropertyDesc writeOnlyPropertyDescriptor;
    public final JavaIndexedPropertyDesc stringPropertyDescriptor;


    public SomeBeanFixture(TestBeansFactory beansFactory) throws IntrospectionException {
        someBean = new SomeBean() {
            {
                setStringArrayProperty(stringArrayPropertyValue);
            }
        };
        someBean.setStringArrayProperty(stringArrayPropertyValue);

        JavaBean<SomeBean> bean = beansFactory.createBean(someBean);

        BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class);
        JavaPropertyDescriptors propertyDescriptors = new JavaPropertyDescriptors(beanInfo);

        PropertyDescriptor readOnlyIndexedPropertyDescriptor = propertyDescriptors.getByName("readOnlyIndexedProperty");
        readOnlyPropertyDescriptor = new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) readOnlyIndexedPropertyDescriptor);
        readOnlyProperty = new JavaIndexedProperty(bean, readOnlyPropertyDescriptor);

        PropertyDescriptor writeOnlyIndexedPropertyDescriptor = propertyDescriptors.getByName("writeOnlyIndexedProperty");
        this.writeOnlyPropertyDescriptor = new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) writeOnlyIndexedPropertyDescriptor);
        writeOnlyProperty = new JavaIndexedProperty(bean, this.writeOnlyPropertyDescriptor);

        PropertyDescriptor stringArrayProperty = propertyDescriptors.getByName("stringArrayProperty");
        stringPropertyDescriptor = new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) stringArrayProperty);
        stringProperty = new JavaIndexedProperty(bean, stringPropertyDescriptor);
    }
}
