package com.link_intersystems.beans;

import java.beans.PropertyChangeListener;

public interface PropertyChangeSource {

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

}
