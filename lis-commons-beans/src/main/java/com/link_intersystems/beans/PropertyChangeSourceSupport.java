package com.link_intersystems.beans;

import com.link_intersystems.lang.ref.Reference;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.function.Consumer;

public class PropertyChangeSourceSupport<T extends PropertyChangeSource> implements Reference<T> {

    private PropertyChangeListenerAdapter adapter = new PropertyChangeListenerAdapter();
    private T referent;
    private Consumer<PropertyChangeEvent> propertyChangeListener;
    private boolean enabled = true;

    public PropertyChangeSourceSupport(Consumer<PropertyChangeEvent> propertyChangeListener) {
        this.propertyChangeListener = Objects.requireNonNull(propertyChangeListener);
    }

    public PropertyChangeSourceSupport(PropertyChangeListener propertyChangeListener) {
        this((Consumer<PropertyChangeEvent>) propertyChangeListener::propertyChange);
    }

    public void setReferent(T referent) {
        if (this.referent != null) {
            this.referent.removePropertyChangeListener(adapter);
        }

        this.referent = referent;

        if (this.referent != null && enabled) {
            this.referent.addPropertyChangeListener(adapter);
        }
    }

    public void setPropertyChangeEventsEnabled(boolean enabled) {
        this.enabled = enabled;
        setReferent(referent);
    }

    public T get() {
        return referent;
    }

    private class PropertyChangeListenerAdapter implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            propertyChangeListener.accept(evt);
        }

    }

}
