package com.link_intersystems.util.adapter;

import java.util.List;

public interface AdapterFactory {

    <T> T getAdapter(Object adaptableObject, Class<T> adapterType);

    List<Class<?>> getAdapterList();
}
