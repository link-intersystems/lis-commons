package com.link_intersystems.beans.record;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeansFactory;
import com.link_intersystems.beans.PropertyDesc;
import com.link_intersystems.beans.PropertyWriteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordPropertyDescTest {

    private PropertyDesc propertyDesc;

    @BeforeEach
    void setUp() {
        BeansFactory beansFactory = BeansFactory.getInstance("record");
        BeanClass<PersonRecord> beanClass = beansFactory.createBeanClass(PersonRecord.class);
        propertyDesc = beanClass.getAllProperties().getByName("firstname");
    }

    @Test
    void getName() {
        assertEquals("firstname", propertyDesc.getName());
    }

    @Test
    void getType() {
        assertEquals(String.class, propertyDesc.getType());
    }

    @Test
    void isReadable() {
        assertTrue(propertyDesc.isReadable());
    }

    @Test
    void isWritable() {
        assertFalse(propertyDesc.isWritable());
    }

    @Test
    void getDeclaringClass() {
        assertEquals(PersonRecord.class, propertyDesc.getDeclaringClass());
    }

    @Test
    void getPropertyValue() {
        PersonRecord personRecord = new PersonRecord("René", "Link");

        assertEquals("René", propertyDesc.getPropertyValue(personRecord));
    }

    @Test
    void setPropertyValue() {
        PersonRecord personRecord = new PersonRecord("René", "Link");

        assertThrows(PropertyWriteException.class, () -> propertyDesc.setPropertyValue(personRecord, "John"));
    }
}