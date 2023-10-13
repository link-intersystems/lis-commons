package com.link_intersystems.beans.record;

import com.link_intersystems.beans.PropertyList;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.*;

class RecordBeanTest {

    @Test
    void getAllProperties() throws IntrospectionException {
        RecordBean<PersonRecord> bean = new RecordBean<>(new RecordBeanClass<>(PersonRecord.class), new PersonRecord("René", "Link"));

        PropertyList allProperties = bean.getAllProperties();
        assertEquals(2, allProperties.size());
    }
}