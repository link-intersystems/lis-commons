package com.link_intersystems.beans.java.record;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.*;

class IntrospectorTest {

    @Test
    void getBeanInfo() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(PersonRecord.class);

        assertEquals("PersonRecord", beanInfo.getBeanDescriptor().getName());
        assertEquals("firstname", beanInfo.getPropertyDescriptors()[0].getName());
        assertEquals("lastname", beanInfo.getPropertyDescriptors()[1].getName());
    }

    @Test
    void getBeanInfoNotRecord() {
        assertThrows(IntrospectionException.class, () -> Introspector.getBeanInfo(DefaultListModel.class));
    }
}