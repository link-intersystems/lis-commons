package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class WindowFocusEventMethod extends EventMethod<WindowFocusListener, WindowEvent> {

    public static final String GAINED_FOCUS_NAME = "windowGainedFocus";
    public static final String LOST_FOCUS_NAME = "windowLostFocus";

    public static final WindowFocusEventMethod GAINED_FOCUS = new WindowFocusEventMethod(GAINED_FOCUS_NAME);
    public static final WindowFocusEventMethod LOST_FOCUS = new WindowFocusEventMethod(LOST_FOCUS_NAME);

    public WindowFocusEventMethod(String... methodNames) {
        super(methodNames);
    }
}
