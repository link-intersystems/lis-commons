package com.link_intersystems.util.adapter;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdapterFactoryRegistryTest {

    @Test
    void getInstance() {
        AdapterFactoryRegistry adapterFactoryRegistry = AdapterFactoryRegistry.getInstance();
        Optional<AdapterFactory> adapterFactory = adapterFactoryRegistry.getAdapterFactory(TestAdapterClass.class);
        assertTrue(adapterFactory.isPresent());
    }
}