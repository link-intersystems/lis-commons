package com.link_intersystems.util.adapter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdapterTypePredicateTest {

    @Test
    void typeMatch() {
        AdapterTypePredicate adapterTypePredicate = new AdapterTypePredicate(CharSequence.class);
        assertTrue(adapterTypePredicate.test(adapterFactory(CharSequence.class)));
    }

    @Test
    void noTypeMatch() {
        AdapterTypePredicate adapterTypePredicate = new AdapterTypePredicate(CharSequence.class);
        assertFalse(adapterTypePredicate.test(adapterFactory(String.class)));
    }

    private static AdapterFactory adapterFactory(Class<?>... adaptableTypes) {
        AdapterFactory adapterFactory = mock(AdapterFactory.class);
        when(adapterFactory.getAdapterList()).thenReturn(Arrays.asList(adaptableTypes));
        return adapterFactory;
    }
}