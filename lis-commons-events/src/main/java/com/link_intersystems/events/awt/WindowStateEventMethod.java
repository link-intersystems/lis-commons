package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class WindowStateEventMethod extends EventMethod<WindowStateListener, WindowEvent> {

    public static final String STATE_CHANGED_NAME = "windowStateChanged";

    public static final WindowStateEventMethod STATE_CHANGED = new WindowStateEventMethod(STATE_CHANGED_NAME);

    public WindowStateEventMethod(String... methodNames) {
        super(methodNames);
    }
}
