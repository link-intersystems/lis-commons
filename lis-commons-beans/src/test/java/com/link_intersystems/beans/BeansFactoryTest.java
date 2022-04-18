package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BeansFactoryTest {

    @Test
    void getDefault() {
        BeansFactory beansFactory = BeansFactory.getDefault();
        assertNotNull(beansFactory);
    }
}