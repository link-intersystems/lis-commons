package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class MouseEventMethod extends EventMethod<MouseListener, MouseEvent> {

    public static final String CLICKED_NAME = "mouseClicked";
    public static final String PRESSED_NAME = "mousePressed";
    public static final String RELEASED_NAME = "mouseReleased";
    public static final String ENTERED_NAME = "mouseEntered";
    public static final String EXITED_NAME = "mouseExited";

    public static final MouseEventMethod CLICKED = new MouseEventMethod(CLICKED_NAME);
    public static final MouseEventMethod PRESSED = new MouseEventMethod(PRESSED_NAME);
    public static final MouseEventMethod RELEASED = new MouseEventMethod(RELEASED_NAME);
    public static final MouseEventMethod ENTERED = new MouseEventMethod(ENTERED_NAME);
    public static final MouseEventMethod EXITED = new MouseEventMethod(EXITED_NAME);

    public MouseEventMethod(String... methodNames) {
        super(methodNames);
    }
}
