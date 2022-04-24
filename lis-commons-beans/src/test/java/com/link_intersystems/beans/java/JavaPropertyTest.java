package com.link_intersystems.beans.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.*;
import java.util.Arrays;
import java.util.Formatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class JavaPropertyTest {

    public static class PropertyTestBean {

        private int intValue;

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
    }

    private JavaProperty intValueProperty;
    private PropertyTestBean testBean;

    @BeforeEach
    void setUp() throws IntrospectionException {
        testBean = new PropertyTestBean();

        BeanInfo beanInfo = Introspector.getBeanInfo(PropertyTestBean.class);
        PropertyDescriptor propertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(pd -> pd.getName().equals("intValue"))
                .findFirst().orElseThrow(() -> new IllegalStateException());
        JavaBeanClass<PropertyTestBean> javaBeanClass = new JavaBeanClass<>(PropertyTestBean.class);
        JavaBean<PropertyTestBean> javaBean = new JavaBean<>(javaBeanClass, testBean);

        intValueProperty = new JavaProperty(javaBean, new JavaPropertyDesc(propertyDescriptor));
    }

    @Test
    void getName() {
        assertEquals("intValue", intValueProperty.getName());
    }

    @Test
    void createPropertiyEditor() {
        PropertyEditor propertiyEditor = intValueProperty.createPropertiyEditor();

        assertNotNull(propertiyEditor);
    }

    @Test
    void getValueAsText() {
        testBean.setIntValue(123);

        assertEquals("123", intValueProperty.getValueAsText());
    }

    @Test
    void setValueAsText() {
        intValueProperty.setValueAsText("1234");

        assertEquals(1234, testBean.getIntValue());
    }

    @Test
    void getValue() {
        testBean.setIntValue(1234);

        assertEquals(1234, (Integer) intValueProperty.getValue());
    }

    @Test
    void setValue() {
        intValueProperty.setValue(12345);

        assertEquals(12345, testBean.getIntValue());
    }

    @Test
    void isReadable() {
        assertTrue(intValueProperty.isReadable());
    }

    @Test
    void isWritable() {
        assertTrue(intValueProperty.isWritable());
    }

    @Test
    void getDeclaringClass() {
        Class<?> declaringClass = intValueProperty.getDeclaringClass();

        assertEquals(PropertyTestBean.class, declaringClass);
    }
}