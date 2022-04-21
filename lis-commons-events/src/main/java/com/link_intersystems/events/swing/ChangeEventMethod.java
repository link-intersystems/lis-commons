package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ChangeEventMethod extends EventMethod<ChangeListener, ChangeEvent> {

    public static final String STATE_CHANGED_NAME = "stateChanged";

    public static final ChangeEventMethod STATE_CHANGED = new ChangeEventMethod(STATE_CHANGED_NAME);

    public ChangeEventMethod(String... methodNames) {
        super(methodNames);
    }
}
