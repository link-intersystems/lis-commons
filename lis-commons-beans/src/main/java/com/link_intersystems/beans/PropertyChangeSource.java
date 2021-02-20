package com.link_intersystems.beans;

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
		PropertyChangeListener listener = PropertyChangeMethods.CHANGED
				.listener(pce -> newValueConsumer.accept((T) pce.getNewValue()));
		PropertyChangeSourcePropertyObservation propertyObservation = new PropertyChangeSourcePropertyObservation(this,
				listener, propertyName);
		propertyObservation.activate();
		return propertyObservation;
	}

	default public <T> PropertyObservation onPropertyChangeEvent(String propertyName,
			Consumer<PropertyChangeEvent> eventConsumer) {
		PropertyChangeListener listener = PropertyChangeMethods.CHANGED.listener(eventConsumer);
		PropertyChangeSourcePropertyObservation propertyObservation = new PropertyChangeSourcePropertyObservation(this,
				listener, propertyName);
		propertyObservation.activate();
		return propertyObservation;
	}

}
