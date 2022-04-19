package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JavaBeansExtension.class)
class BeanClassTest {

    private JavaBeanClass<SomeBean> someBeanClass;

    @BeforeEach
    void setUp(TestBeansFactory beansFactory) throws BeanClassException {
        someBeanClass = beansFactory.createBeanClass(SomeBean.class);
    }

    @Test
    void getPropertyDescriptorWrongClassProperty() throws NoSuchMethodException, SecurityException {
        Method method = BeanClassTest.class.getDeclaredMethod("getPropertyDescriptorWrongClassPropertyMethod");
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(method);
        assertNull(propertyDescriptor, "propertyDescriptor");
    }

    // used by test above
    void getPropertyDescriptorWrongClassPropertyMethod() {
    }

    @Test
    void getPropertyDescriptorByGetter() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("getStringProperty"));
        assertNotNull(propertyDescriptor, "propertyDescriptor");
        assertEquals("stringProperty", propertyDescriptor.getName());
    }

    @Test
    void getPropertyDescriptorBySetter() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("setStringProperty", String.class));
        assertNotNull(propertyDescriptor, "propertyDescriptor");
        assertEquals("stringProperty", propertyDescriptor.getName());
    }

    @Test
    void consistency() throws SecurityException {
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
        Method propertyMethod = SomeBean.class.getDeclaredMethod("setStringProperty", String.class);
        assertTrue(someBeanClass.isPropertyAccessor(propertyMethod));
    }

    @Test
    void newInstance() throws SecurityException {
        JavaBean<SomeBean> someBeanBean = someBeanClass.newBeanInstance();
        assertNotNull(someBeanBean);

        SomeBean object = someBeanBean.getObject();
        assertNotNull(object);
    }

    @Test
    void isListenerSupported() {
        assertTrue(someBeanClass.isListenerSupported(PropertyChangeListener.class));

        assertFalse(someBeanClass.isListenerSupported(VetoableChangeListener.class));
    }

}
