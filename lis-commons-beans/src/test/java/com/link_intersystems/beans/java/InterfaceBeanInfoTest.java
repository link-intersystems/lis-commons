package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class InterfaceBeanInfoTest {

    private InterfaceBeanInfo interfaceBeanInfo;
    private BeanInfo personBeanInfo;

    @BeforeEach
    void setUp() throws IntrospectionException {
        personBeanInfo = Introspector.getBeanInfo(Person.class);
        interfaceBeanInfo = new InterfaceBeanInfo(Person.class);
    }


    @Test
    void getBeanDescriptor() {
        assertEquals(personBeanInfo.getBeanDescriptor(), interfaceBeanInfo.getBeanDescriptor());
    }

    @Test
    void getEventSetDescriptors() {
        assertArrayEquals(personBeanInfo.getEventSetDescriptors(), interfaceBeanInfo.getEventSetDescriptors());
    }

    @Test
    void getDefaultEventIndex() {
        assertEquals(personBeanInfo.getDefaultEventIndex(), interfaceBeanInfo.getDefaultEventIndex());
    }

    @Test
    void getPropertyDescriptors() {
        assertArrayEquals(personBeanInfo.getPropertyDescriptors(), interfaceBeanInfo.getPropertyDescriptors());
    }

    @Test
    void getDefaultPropertyIndex() {
        assertEquals(personBeanInfo.getDefaultPropertyIndex(), interfaceBeanInfo.getDefaultPropertyIndex());
    }

    @Test
    void getMethodDescriptors() {
        assertArrayEquals(personBeanInfo.getMethodDescriptors(), interfaceBeanInfo.getMethodDescriptors());
    }

    @Test
    void getAdditionalBeanInfo() throws IntrospectionException {
        assertNotEquals(personBeanInfo.getAdditionalBeanInfo(), interfaceBeanInfo.getAdditionalBeanInfo());

        BeanInfo[] additionalBeanInfo = interfaceBeanInfo.getAdditionalBeanInfo();
        assertEquals(1, additionalBeanInfo.length);

        BeanInfo namedBeanInfo = Introspector.getBeanInfo(Named.class);
        assertEquals(namedBeanInfo, additionalBeanInfo[0]);
    }

    @Test
    void getIcon() {
        assertEquals(personBeanInfo.getIcon(1), interfaceBeanInfo.getIcon(1));
    }
}