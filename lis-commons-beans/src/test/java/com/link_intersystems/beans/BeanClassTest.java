package com.link_intersystems.beans;

import com.link_intersystems.beans.java.JavaBean;
import com.link_intersystems.beans.java.JavaBeanClass;
import com.link_intersystems.beans.java.JavaPropertyDescriptors;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BeanClassTest {

    @Test
    void getPropertyDescriptorWrongClassProperty() throws NoSuchMethodException, SecurityException {
        JavaBeanClass<SomeBean> someBeanClass = JavaBeanClass.get(SomeBean.class);
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(BeanClassTest.class.getDeclaredMethod("getPropertyDescriptorWrongClassProperty"));
        assertNull(propertyDescriptor, "propertyDescriptor");
    }

    @Test
    void getPropertyDescriptorByGetter() throws NoSuchMethodException, SecurityException {
        JavaBeanClass<SomeBean> someBeanClass = JavaBeanClass.get(SomeBean.class);
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("getStringProperty"));
        assertNotNull(propertyDescriptor, "propertyDescriptor");
        assertEquals("stringProperty", propertyDescriptor.getName());
    }

    @Test
    void getPropertyDescriptorBySetter() throws NoSuchMethodException, SecurityException {
        JavaBeanClass<SomeBean> someBeanClass = JavaBeanClass.get(SomeBean.class);
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("setStringProperty", String.class));
        assertNotNull(propertyDescriptor, "propertyDescriptor");
        assertEquals("stringProperty", propertyDescriptor.getName());
    }

    @Test
    void consistency() throws SecurityException {
        JavaBeanClass<SomeBean> someBeanClass = JavaBeanClass.get(SomeBean.class);
        JavaPropertyDescriptors propertyDescriptors = someBeanClass.getJavaPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                PropertyDescriptor propertyDescriptorByMethod = someBeanClass.getPropertyDescriptor(readMethod);
                assertSame(propertyDescriptor, propertyDescriptorByMethod);
            }

            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor propertyDescriptorByMethod = someBeanClass.getPropertyDescriptor(writeMethod);
                assertSame(propertyDescriptor, propertyDescriptorByMethod);
            }
        }
    }

    @Test
    void isPropertyMethod() throws NoSuchMethodException, SecurityException {
        JavaBeanClass<SomeBean> someBeanClass = JavaBeanClass.get(SomeBean.class);
        Method propertyMethod = SomeBean.class.getDeclaredMethod("setStringProperty", String.class);
        assertTrue(someBeanClass.isPropertyAccessor(propertyMethod));
    }

    @Test
    void newInstance() throws SecurityException {
        JavaBeanClass<SomeBean> someBeanClass = JavaBeanClass.get(SomeBean.class);
        JavaBean<SomeBean> someBeanBean = someBeanClass.newInstance();
        assertNotNull(someBeanBean);

        SomeBean object = someBeanBean.getObject();
        assertNotNull(object);
    }

}
