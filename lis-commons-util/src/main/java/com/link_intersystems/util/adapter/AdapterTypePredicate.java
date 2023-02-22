package com.link_intersystems.util.adapter;

import java.util.List;
import java.util.function.Predicate;

class AdapterTypePredicate implements Predicate<AdapterFactory> {

    private Class<?> adapterType;

    public AdapterTypePredicate(Class<?> adapterType) {
        this.adapterType = adapterType;
    }

    @Override
    public boolean test(AdapterFactory adapterFactory) {
        List<Class<?>> adapterList = adapterFactory.getAdapterList();
        return adapterList.contains(adapterType);
    }
}
