package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BeanInfoBeanMapTest {

    private BeanInfoBeanMap beanMap;

    @BeforeEach
    void setUp() throws IntrospectionException {
        Person person = new Person("Penelope", "Guiness");
        beanMap = new BeanInfoBeanMap(person);
    }

    @Test
    void property() {
        assertEquals(3, beanMap.size(), "property count");
        assertEquals("Penelope", beanMap.get("firstName"));
        assertEquals("Guiness", beanMap.get("lastName"));
        assertEquals("Penelope Guiness", beanMap.get("name"));
    }
}