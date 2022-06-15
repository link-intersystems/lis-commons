package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class MergedAdditionalBeanInfoTest {

    private MergedAdditionalBeanInfo additionalBeanInfo;
    private BeanInfo personBeanInfo;

    @BeforeEach
    void setUp() throws IntrospectionException {
        personBeanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        InterfaceBeanInfo interfaceBeanInfo = new InterfaceBeanInfo(Person.class);
        additionalBeanInfo = new MergedAdditionalBeanInfo(interfaceBeanInfo);
        additionalBeanInfo.setIncludeMainBeanInfo(false);
    }


    @Test
    void getBeanDescriptor() {
        BeanDescriptor beanDescriptor = additionalBeanInfo.getBeanDescriptor();
        assertNotNull(beanDescriptor);
        assertEquals(Person.class, beanDescriptor.getBeanClass());
    }

    @Test
    void getEventSetDescriptors() {
        EventSetDescriptor[] eventSetDescriptors = additionalBeanInfo.getEventSetDescriptors();
        assertEquals(0, eventSetDescriptors.length);
    }

    @Test
    void getDefaultEventIndex() {
        assertEquals(personBeanInfo.getDefaultEventIndex(), additionalBeanInfo.getDefaultEventIndex());
    }

    @Test
    void getPropertyDescriptors() {
        PropertyDescriptor[] propertyDescriptors = additionalBeanInfo.getPropertyDescriptors();
        assertEquals(1, propertyDescriptors.length);

        assertEquals("name", propertyDescriptors[0].getName());
    }

    @Test
    void getDefaultPropertyIndex() {
        assertEquals(personBeanInfo.getDefaultPropertyIndex(), additionalBeanInfo.getDefaultPropertyIndex());
    }

    @Test
    void getMethodDescriptors() {
        MethodDescriptor[] methodDescriptors = additionalBeanInfo.getMethodDescriptors();
        assertEquals(2, methodDescriptors.length);

        MethodDescriptor trimMethod = Arrays.stream(methodDescriptors).filter(md -> md.getName().equals("trim")).findFirst().orElse(null);
        assertNotNull(trimMethod);
    }

    @Test
    void getAdditionalBeanInfo() {
        assertEquals(1, additionalBeanInfo.getAdditionalBeanInfo().length);
    }

    @Test
    void getIcon() {
        assertEquals(personBeanInfo.getIcon(1), additionalBeanInfo.getIcon(1));
    }

}