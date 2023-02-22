package com.link_intersystems.util.adapter;

import java.util.List;
import java.util.Optional;

/**
 * An interface for an adaptable object. Adaptable objects can be dynamically
 * extended to provide different interfaces (or "adapters"). Adapters are
 * created by adapter factories, which are in turn managed by type by adapter
 * managers.
 * <p>
 * For example,
 *
 * <pre>
 * IAdaptable a = [some adaptable];
 * IFoo x = (IFoo)a.getAdapter(IFoo.class);
 * if (x != null)
 * 	[do IFoo things with x]
 * </pre>
 * <p>
 * Clients may implement this interface, or obtain a default implementation of
 * this interface by subclassing PlatformObject.
 */
public interface Adaptable {

    /**
     * Returns an object which is an instance of the given class associated with
     * this object. Returns null if no such object can be found.
     *
     * @param adapterType adapter class to look up.
     * @param <T>
     * @return a object castable to the given class, or null if this object does
     * not have an adapter for the given class.
     */
    default public <T> T getAdapter(Class<T> adapterType) {
        if (adapterType.isInstance(this)) {
            return adapterType.cast(this);
        }

        AdapterFactoryRegistry adapterFactoryRegistry = AdapterFactoryRegistry.getInstance();
        Optional<AdapterFactory> adapterFactory = adapterFactoryRegistry.getAdapterFactory(adapterType);
        return adapterFactory.map(af -> af.getAdapter(Adaptable.this, adapterType)).orElse(null);
    }
}