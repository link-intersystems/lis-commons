package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;

import java.beans.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SomeBeanFixture {

    public static final String[] stringArrayPropertyValue = new String[]{"Hello", "World"};

    public final SomeBean someBean;

    public final JavaIndexedProperty readOnlyIndexedProperty;
    public final JavaIndexedProperty writeOnlyIndexedProperty;
    public final JavaIndexedProperty stringArrayProperty;
    public final JavaIndexedPropertyDesc readOnlyIndexedPropertyDescriptor;
    public final JavaIndexedPropertyDesc writeOnlyIndexedPropertyDescriptor;
    public final JavaIndexedPropertyDesc stringArrayPropertyDescriptor;
    public final JavaBean<SomeBean> bean;
    public final JavaPropertyDesc readOnlyPropertyDescriptor;
    public final JavaProperty readOnlyProperty;
    public final JavaPropertyDesc writeOnlyPropertyDescriptor;
    public final JavaProperty writeOnlyProperty;
    public final JavaPropertyDesc stringPropertyDescriptor;
    public final JavaProperty stringProperty;


    public SomeBeanFixture(TestBeansFactory beansFactory) throws IntrospectionException, BeanClassException {
        this(beansFactory, createSomeBean());
    }

    public SomeBeanFixture(TestBeansFactory beansFactory, SomeBean someBean) throws IntrospectionException, BeanClassException {
        this.someBean = someBean;
        bean = beansFactory.createBean(someBean);

        BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class);
        JavaPropertyDescriptors propertyDescriptors = new JavaPropertyDescriptors(beanInfo);

        PropertyDescriptor readOnlyIndexedPropertyDescriptor = propertyDescriptors.getByName("readOnlyIndexedProperty");
        this.readOnlyIndexedPropertyDescriptor = new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) readOnlyIndexedPropertyDescriptor);
        readOnlyIndexedProperty = new JavaIndexedProperty(bean, this.readOnlyIndexedPropertyDescriptor);

        PropertyDescriptor readOnlyPropertyDescriptor = propertyDescriptors.getByName("readOnlyProperty");
        this.readOnlyPropertyDescriptor = new JavaPropertyDesc(readOnlyPropertyDescriptor);
        readOnlyProperty = new JavaProperty(bean, this.readOnlyPropertyDescriptor);

        PropertyDescriptor writeOnlyIndexedPropertyDescriptor = propertyDescriptors.getByName("writeOnlyIndexedProperty");
        this.writeOnlyIndexedPropertyDescriptor = new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) writeOnlyIndexedPropertyDescriptor);
        writeOnlyIndexedProperty = new JavaIndexedProperty(bean, this.writeOnlyIndexedPropertyDescriptor);

        PropertyDescriptor writeOnlyPropertyDescriptor = propertyDescriptors.getByName("writeOnlyProperty");
        this.writeOnlyPropertyDescriptor = new JavaPropertyDesc(writeOnlyPropertyDescriptor);
        writeOnlyProperty = new JavaProperty(bean, this.writeOnlyPropertyDescriptor);

        PropertyDescriptor stringArrayProperty = propertyDescriptors.getByName("stringArrayProperty");
        stringArrayPropertyDescriptor = new JavaIndexedPropertyDesc((IndexedPropertyDescriptor) stringArrayProperty);
        this.stringArrayProperty = new JavaIndexedProperty(bean, stringArrayPropertyDescriptor);

        PropertyDescriptor stringPropertyDescriptor = propertyDescriptors.getByName("stringProperty");
        this.stringPropertyDescriptor = new JavaPropertyDesc(stringPropertyDescriptor);
        stringProperty = new JavaProperty(bean, this.stringPropertyDescriptor);
    }

    private static SomeBean createSomeBean() {
        final SomeBean someBean;
        someBean = new SomeBean() {
            {
                setStringArrayProperty(stringArrayPropertyValue);
            }
        };
        someBean.setStringArrayProperty(stringArrayPropertyValue);
        return someBean;
    }
}
