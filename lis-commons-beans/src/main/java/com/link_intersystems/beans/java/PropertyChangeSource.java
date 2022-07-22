package com.link_intersystems.beans.java;

import java.beans.PropertyChangeListener;

public interface PropertyChangeSource {

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

}
