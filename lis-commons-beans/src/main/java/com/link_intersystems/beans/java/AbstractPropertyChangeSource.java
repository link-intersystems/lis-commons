package com.link_intersystems.beans.java;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractPropertyChangeSource implements PropertyChangeSource {

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	@Override
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
		propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
		propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
		propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	protected void removePropertyChangeListeners() {
		PropertyChangeListener[] propertyChangeListeners = propertyChangeSupport.getPropertyChangeListeners();
		for (PropertyChangeListener propertyChangeListener : propertyChangeListeners) {
			propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
		}
	}
}
