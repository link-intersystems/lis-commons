package com.link_intersystems.beans;

import com.link_intersystems.events.EventMethod;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface PropertyChangeMethods {

    public static final EventMethod<PropertyChangeListener, PropertyChangeEvent> CHANGED = new EventMethod<>(
            PropertyChangeListener.class, PropertyChangeEvent.class, "propertyChange");


}
