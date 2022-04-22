package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class MouseWheelEventMethod extends EventMethod<MouseWheelListener, MouseWheelEvent> {

    public static final String WHEEL_MOVED_NAME = "mouseWheelMoved";

    public static final MouseWheelEventMethod WHEEL_MOVED = new MouseWheelEventMethod(WHEEL_MOVED_NAME);

    public MouseWheelEventMethod(String... methodNames) {
        super(methodNames);
    }
}
