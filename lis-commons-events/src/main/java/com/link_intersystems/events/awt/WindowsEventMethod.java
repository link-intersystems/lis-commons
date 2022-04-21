package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class WindowsEventMethod extends EventMethod<WindowListener, WindowEvent> {

    public static final String OPENED_NAME = "windowOpened";
    public static final String CLOSING_NAME = "windowClosing";
    public static final String CLOSED_NAME = "windowClosed";
    public static final String ICONIFIED_NAME = "windowIconified";
    public static final String DEICONIFIED_NAME = "windowDeiconified";
    public static final String ACTIVATED_NAME = "windowActivated";
    public static final String DEACTIVATED_NAME = "windowDeactivated";

    public static final WindowsEventMethod OPENED = new WindowsEventMethod(OPENED_NAME);
    public static final WindowsEventMethod CLOSING = new WindowsEventMethod(CLOSING_NAME);
    public static final WindowsEventMethod CLOSED = new WindowsEventMethod(CLOSED_NAME);
    public static final WindowsEventMethod ICONIFIED = new WindowsEventMethod(ICONIFIED_NAME);
    public static final WindowsEventMethod DEICONIFIED = new WindowsEventMethod(DEICONIFIED_NAME);
    public static final WindowsEventMethod ACTIVATED = new WindowsEventMethod(ACTIVATED_NAME);
    public static final WindowsEventMethod DEACTIVATED = new WindowsEventMethod(DEACTIVATED_NAME);

    public WindowsEventMethod(String... methodNames) {
        super(methodNames);
    }
}
