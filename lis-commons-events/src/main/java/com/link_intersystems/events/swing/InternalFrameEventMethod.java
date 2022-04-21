package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class InternalFrameEventMethod extends EventMethod<InternalFrameListener, InternalFrameEvent> {

    public static final String OPENED_NAME = "internalFrameOpened";
    public static final String CLOSING_NAME = "internalFrameClosing";
    public static final String CLOSED_NAME = "internalFrameClosed";
    public static final String ICONIFIED_NAME = "internalFrameIconified";
    public static final String DEICONIFIED_NAME = "internalFrameDeiconified";
    public static final String ACTIVATED_NAME = "internalFrameActivated";
    public static final String DEACTIVATED_NAME = "internalFrameDeactivated";

    public static final InternalFrameEventMethod OPENED = new InternalFrameEventMethod(OPENED_NAME);
    public static final InternalFrameEventMethod CLOSING = new InternalFrameEventMethod(CLOSING_NAME);
    public static final InternalFrameEventMethod CLOSED = new InternalFrameEventMethod(CLOSED_NAME);
    public static final InternalFrameEventMethod ICONIFIED = new InternalFrameEventMethod(ICONIFIED_NAME);
    public static final InternalFrameEventMethod DEICONIFIED = new InternalFrameEventMethod(DEICONIFIED_NAME);
    public static final InternalFrameEventMethod ACTIVATED = new InternalFrameEventMethod(ACTIVATED_NAME);
    public static final InternalFrameEventMethod DEACTIVATED = new InternalFrameEventMethod(DEACTIVATED_NAME);

    public InternalFrameEventMethod(String... methodNames) {
        super(methodNames);
    }
}
