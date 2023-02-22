package com.link_intersystems.util.adapter;

import java.util.List;
import java.util.Optional;

public interface AdapterFactorySelector {

    public Optional<AdapterFactory> select(List<AdapterFactory> adapterFactoryList, Class<?> adapterType);
}
