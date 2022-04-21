package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FocusEventMethod extends EventMethod<FocusListener, FocusEvent> {

    public static final String GAINED_NAME = "focusGained";
    public static final String LOST_NAME = "focusLost";

    public static final FocusEventMethod GAINED = new FocusEventMethod(GAINED_NAME);
    public static final FocusEventMethod LOST = new FocusEventMethod(LOST_NAME);

    public FocusEventMethod(String... methodNames) {
        super(methodNames);
    }
}
