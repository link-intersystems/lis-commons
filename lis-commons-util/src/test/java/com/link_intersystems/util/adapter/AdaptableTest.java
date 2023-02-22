package com.link_intersystems.util.adapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdaptableTest {

    @Test
    void getAdapter() {
        Adaptable adaptable = new Adaptable() {
        };
        TestAdapterClass testAdapterClass = adaptable.getAdapter(TestAdapterClass.class);

        assertNotNull(testAdapterClass);
        assertEquals(adaptable, testAdapterClass.getAdaptableObject());
    }
}