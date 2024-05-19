package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeanInstantiationException;
import com.link_intersystems.beans.PropertyDescList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.*;

class RecordBeanClassTest {

    private RecordBeanClass<PersonRecord> beanClass;

    @BeforeEach
    void setUp() throws IntrospectionException {
        beanClass = new RecordBeanClass<>(PersonRecord.class);
    }

    @Test
    void getName() {
        assertEquals("PersonRecord", beanClass.getName());
    }

    @Test
    void getType() {
        assertEquals(PersonRecord.class, beanClass.getType());
    }

    @Test
    void getSingleProperties() {
        PropertyDescList allProperties = beanClass.getProperties();

        assertEquals(2, allProperties.size());
    }

    @Test
    void newBeanInstance() {
        assertThrows(BeanInstantiationException.class, () -> beanClass.newBeanInstance());
    }

    @Test
    void fromExistingInstance() {
        PersonRecord personRecord = new PersonRecord("René", "Link");
        Bean<PersonRecord> personBean = beanClass.getBeanFromInstance(personRecord);
        assertInstanceOf(RecordBean.class, personBean);
    }
}