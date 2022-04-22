package com.link_intersystems.events.beans;

import com.link_intersystems.events.EventMethod;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class VetoableChangeMethod extends EventMethod<VetoableChangeListener, PropertyChangeEvent> {

    public static final String VETOABLE_CHANGE_NAME = "vetoableChange";

    public static final VetoableChangeMethod VETOABLE_CHANGE = new VetoableChangeMethod(VETOABLE_CHANGE_NAME);

    public VetoableChangeMethod(String... methodNames) {
        super(methodNames);
    }
}
