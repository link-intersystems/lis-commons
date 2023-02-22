package com.link_intersystems.util.adapter;

import java.util.*;

import static java.util.Objects.*;

public class AdapterFactoryRegistry {

    public static AdapterFactoryRegistry getInstance() {
        return instance;
    }

    static void setInstance(AdapterFactoryRegistry instance) {
        AdapterFactoryRegistry.instance = requireNonNull(instance);
    }

    private static AdapterFactoryRegistry instance = new AdapterFactoryRegistry();

    private volatile List<AdapterFactory> adapterFactories;
    private AdapterFactorySelector adapterFactorySelector = DefaultAdapterFactorySelector.INSTANCE;

    public AdapterFactoryRegistry() {
    }

    public Optional<AdapterFactory> getAdapterFactory(Class<?> adapterType) {
        List<AdapterFactory> adapterFactories = getAdapterFactories();
        return adapterFactorySelector.select(adapterFactories, adapterType);
    }

    protected void setAdapterFactorySelector(AdapterFactorySelector adapterFactorySelector) {
        this.adapterFactorySelector = Objects.requireNonNull(adapterFactorySelector);
    }

    protected AdapterFactorySelector getAdapterFactorySelector() {
        return adapterFactorySelector;
    }

    protected List<AdapterFactory> getAdapterFactories() {
        if (adapterFactories == null) {
            synchronized (this) {
                adapterFactories = new ArrayList<>();
                ServiceLoader<AdapterFactory> adapterFactoryLoader = ServiceLoader.load(AdapterFactory.class);
                adapterFactoryLoader.forEach(this.adapterFactories::add);
            }
        }
        return Collections.unmodifiableList(adapterFactories);
    }
}
