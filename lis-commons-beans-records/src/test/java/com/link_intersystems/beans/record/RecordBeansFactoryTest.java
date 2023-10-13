package com.link_intersystems.beans.record;

import com.link_intersystems.beans.BeansFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordBeansFactoryTest {

    private BeansFactory beansFactory;

    @BeforeEach
    void setUp() {
        beansFactory = BeansFactory.getInstance("record");
    }

    @Test
    void recordBeansFactoryDiscovery() {
        assertNotNull(beansFactory);
    }

    @Test
    void createBeanClass() {
        BeansFactory beansFactory = BeansFactory.getInstance("record");
        assertInstanceOf(RecordBeanClass.class, beansFactory.createBeanClass(PersonRecord.class));
    }



}
