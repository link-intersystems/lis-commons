package com.link_intersystems.util.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultAdapterFactorySelector implements AdapterFactorySelector {

    public static final AdapterFactorySelector INSTANCE = new DefaultAdapterFactorySelector();

    @Override
    public Optional<AdapterFactory> select(List<AdapterFactory> adapterFactoryList, Class<?> adapterType) {
        AdapterTypePredicate adapterTypePredicate = new AdapterTypePredicate(adapterType);
        List<AdapterFactory> matchingAdapterFactories = adapterFactoryList.stream().filter(adapterTypePredicate).collect(Collectors.toList());
        return matchingAdapterFactories.stream().findFirst();
    }
}
