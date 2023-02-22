package com.link_intersystems.util.adapter;

import java.util.Arrays;
import java.util.List;

public class TestAdapterFactory implements AdapterFactory {
    @Override
    public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
        return (T) new TestAdapterClass(adaptableObject);
    }

    @Override
    public List<Class<?>> getAdapterList() {
        return Arrays.asList(TestAdapterClass.class);
    }
}
