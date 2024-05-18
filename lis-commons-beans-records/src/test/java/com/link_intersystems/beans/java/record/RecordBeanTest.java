package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.PropertyList;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.*;

class RecordBeanTest {

    @Test
    void getProperties() throws IntrospectionException {
        RecordBean<PersonRecord> bean = new RecordBean<>(new RecordBeanClass<>(PersonRecord.class), new PersonRecord("René", "Link"));

        PropertyList allProperties = bean.getProperties();
        assertEquals(2, allProperties.size());
    }
}