package com.link_intersystems.beans.record;

import com.link_intersystems.beans.BeansFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordBeansFactoryTest {

    @Test
    void recordBeansFactoryDiscovery() {
        BeansFactory beansFactory = BeansFactory.getInstance("record");
        assertNotNull(beansFactory);
    }
}