package com.link_intersystems.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.link_intersystems.util.EventMethod;

public interface PropertyChangeMethods {

	public static final EventMethod<PropertyChangeListener, PropertyChangeEvent> CHANGED = new EventMethod<>(
			PropertyChangeListener.class, PropertyChangeEvent.class, "propertyChange");


}
