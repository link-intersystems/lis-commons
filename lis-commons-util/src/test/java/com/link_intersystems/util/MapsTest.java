package com.link_intersystems.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class MapsTest {

    private Named named1;
    private Named named2;
    private ArrayList<Named> namedObjects;

    private static class Named {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @BeforeEach
    void setUp(){
        named1 = new Named();
        named2 = new Named();

        namedObjects = new ArrayList<>(Arrays.asList(named1, named2));
    }

    @Test
    void keyMap() {
        named1.setName("A");
        named2.setName("B");

        Map<String, Named> byName = Maps.keyMap(namedObjects, Named::getName);

        assertEquals(named1, byName.get("A"));
        assertEquals(named2, byName.get("B"));
    }

    @Test
    void keyMapCollistion() {
        named1.setName("A");
        named2.setName("A");

        assertThrows(KeyCollisionException.class, () -> Maps.keyMap(namedObjects, Named::getName));
    }
}