package com.link_intersystems.beans;

import java.beans.PropertyChangeListener;

public class PropertyChangeSourcePropertyObservation implements PropertyObservation {

	private boolean activated;

	private PropertyChangeSource propertyChangeSource;
	private PropertyChangeListener listener;
	private String propertyName;

	public PropertyChangeSourcePropertyObservation(PropertyChangeSource propertyChangeSource,
			PropertyChangeListener listener) {
		this(propertyChangeSource, listener, null);
	}

	public PropertyChangeSourcePropertyObservation(PropertyChangeSource propertyChangeSource,
			PropertyChangeListener listener, String propertyName) {
		this.propertyChangeSource = propertyChangeSource;
		this.propertyName = propertyName;
		this.listener = listener;
	}

	@Override
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

	@Override
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
