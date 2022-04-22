package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class WindowEventMethod extends EventMethod<WindowListener, WindowEvent> {

    public static final String OPENED_NAME = "windowOpened";
    public static final String CLOSING_NAME = "windowClosing";
    public static final String CLOSED_NAME = "windowClosed";
    public static final String ICONIFIED_NAME = "windowIconified";
    public static final String DEICONIFIED_NAME = "windowDeiconified";
    public static final String ACTIVATED_NAME = "windowActivated";
    public static final String DEACTIVATED_NAME = "windowDeactivated";

    public static final WindowEventMethod OPENED = new WindowEventMethod(OPENED_NAME);
    public static final WindowEventMethod CLOSING = new WindowEventMethod(CLOSING_NAME);
    public static final WindowEventMethod CLOSED = new WindowEventMethod(CLOSED_NAME);
    public static final WindowEventMethod ICONIFIED = new WindowEventMethod(ICONIFIED_NAME);
    public static final WindowEventMethod DEICONIFIED = new WindowEventMethod(DEICONIFIED_NAME);
    public static final WindowEventMethod ACTIVATED = new WindowEventMethod(ACTIVATED_NAME);
    public static final WindowEventMethod DEACTIVATED = new WindowEventMethod(DEACTIVATED_NAME);

    public WindowEventMethod(String... methodNames) {
        super(methodNames);
    }
}
