package com.link_intersystems.events.beans;

import com.link_intersystems.events.EventMethod;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyChangeMethod extends EventMethod<PropertyChangeListener, PropertyChangeEvent> {

    public static final String CHANGE_NAME = "propertyChange";

    public static final PropertyChangeMethod CHANGE = new PropertyChangeMethod(CHANGE_NAME);

    public PropertyChangeMethod(String... methodNames) {
        super(methodNames);
    }
}
