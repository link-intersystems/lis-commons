package com.link_intersystems.beans.java;

import com.link_intersystems.beans.AbstractBean;
import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeanEventTypeList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JavaBeansExtension.class)
class BeanClassTest {

    private JavaBeanClass<SomeBean> someBeanClass;

    @BeforeEach
    void setUp(TestBeansFactory beansFactory) throws BeanClassException {
        someBeanClass = beansFactory.createBeanClass(SomeBean.class, Object.class);
    }

    @Test
    void getBeanEventTypes() throws SecurityException {
        BeanEventTypeList beanEventTypes = someBeanClass.getBeanEventTypes();
        assertEquals(1, beanEventTypes.size());
    }

    @Test
    void getPropertyDescriptorWrongClassProperty() throws NoSuchMethodException, SecurityException {
        Method method = BeanClassTest.class.getDeclaredMethod("getPropertyDescriptorWrongClassPropertyMethod");
        JavaPropertyDesc javaPropertyDesc = someBeanClass.getPropertyDescByMethod(method);
        assertNull(javaPropertyDesc, "javaPropertyDesc");
    }

    // used by test above
    void getPropertyDescriptorWrongClassPropertyMethod() {
    }

    @Test
    void getPropertyDescriptorByGetter() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescByMethod(SomeBean.class.getDeclaredMethod("getStringProperty")).getJavaPropertyDescriptor();
        assertNotNull(propertyDescriptor, "propertyDescriptor");
        assertEquals("stringProperty", propertyDescriptor.getName());
    }

    @Test
    void getPropertyDescriptorBySetter() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescByMethod(SomeBean.class.getDeclaredMethod("setStringProperty", String.class)).getJavaPropertyDescriptor();
        assertNotNull(propertyDescriptor, "propertyDescriptor");
        assertEquals("stringProperty", propertyDescriptor.getName());
    }

    @Test
    void consistency() throws SecurityException {
        JavaPropertyDescriptors propertyDescriptors = someBeanClass.getJavaPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                PropertyDescriptor propertyDescriptorByMethod = someBeanClass.getPropertyDescByMethod(readMethod).getJavaPropertyDescriptor();
                assertSame(propertyDescriptor, propertyDescriptorByMethod);
            }

            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor propertyDescriptorByMethod = someBeanClass.getPropertyDescByMethod(writeMethod).getJavaPropertyDescriptor();
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
        Bean<SomeBean> someBeanBean = someBeanClass.newBeanInstance();
        assertNotNull(someBeanBean);

        SomeBean object = someBeanBean.getBeanObject();
        assertNotNull(object);
    }



}
