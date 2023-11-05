package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.PropertyDescList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class RecordBeanClassTest {

    private BeanClass<PersonRecord> bean;

    @BeforeEach
    void setUp() throws IntrospectionException {
        bean = new RecordBeanClass<>(PersonRecord.class);
    }

    @Test
    void getName() {
        assertEquals("PersonRecord", bean.getName());
    }

    @Test
    void getType() {
        assertEquals(PersonRecord.class, bean.getType());
    }

    @Test
    void getBeanInstanceFactory() {

        assertInstanceOf(RecordBeanInstanceFactory.class, bean.getBeanInstanceFactory());
    }

    @Test
    void getAllProperties() {
        PropertyDescList allProperties = bean.getAllProperties();

        assertEquals(2, allProperties.size());
    }
}