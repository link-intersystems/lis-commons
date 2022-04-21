package com.link_intersystems.beans;

import com.link_intersystems.events.beans.PropertyChangeMethod;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

public interface PropertyChangeSource {

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

    default public PropertyObservation onPropertyChange(String propertyName, Runnable runnable) {
        return onPropertyChange(propertyName, v -> runnable.run());
    }

    @SuppressWarnings("unchecked")
    default public <T> PropertyObservation onPropertyChange(String propertyName, Consumer<T> newValueConsumer) {
        PropertyChangeListener listener = PropertyChangeMethod.CHANGE
                .listener(pce -> newValueConsumer.accept((T) pce.getNewValue()));
        PropertyObservation propertyObservation = new PropertyObservation(this, listener, propertyName);
        propertyObservation.activate();
        return propertyObservation;
    }

    default public <T> PropertyObservation onPropertyChangeEvent(String propertyName,
                                                                 Consumer<PropertyChangeEvent> eventConsumer) {
        PropertyChangeListener listener = PropertyChangeMethod.CHANGE.listener(eventConsumer);
        PropertyObservation propertyObservation = new PropertyObservation(this, listener, propertyName);
        propertyObservation.activate();
        return propertyObservation;
    }

    public static class PropertyObservation {

        private boolean activated;

        private PropertyChangeSource propertyChangeSource;
        private PropertyChangeListener listener;
        private String propertyName;

        public PropertyObservation(PropertyChangeSource propertyChangeSource, PropertyChangeListener listener) {
            this(propertyChangeSource, listener, null);
        }

        public PropertyObservation(PropertyChangeSource propertyChangeSource, PropertyChangeListener listener,
                                   String propertyName) {
            this.propertyChangeSource = propertyChangeSource;
            this.propertyName = propertyName;
            this.listener = listener;
        }

        public void activate() {
            if (!activated) {
                if (propertyName == null) {
                    propertyChangeSource.addPropertyChangeListener(listener);
                } else {
                    propertyChangeSource.addPropertyChangeListener(propertyName, listener);
                }
                activated = true;
            }
        }

        public void deactivate() {
            if (activated) {
                if (propertyName == null) {
                    propertyChangeSource.removePropertyChangeListener(listener);
                } else {
                    propertyChangeSource.removePropertyChangeListener(propertyName, listener);
                }
                activated = false;
            }
        }

    }

}
