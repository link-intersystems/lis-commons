package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ComponentEventMethod extends EventMethod<ComponentListener, ComponentEvent> {

    public static final String RESIZED_NAME = "componentResized";
    public static final String MOVED_NAME = "componentMoved";
    public static final String SHOWN_NAME = "componentShown";
    public static final String HIDDEN_NAME = "componentHidden";

    public static final ComponentEventMethod RESIZED = new ComponentEventMethod(RESIZED_NAME);
    public static final ComponentEventMethod MOVED = new ComponentEventMethod(MOVED_NAME);
    public static final ComponentEventMethod SHOWN = new ComponentEventMethod(SHOWN_NAME);
    public static final ComponentEventMethod HIDDEN = new ComponentEventMethod(HIDDEN_NAME);

    public ComponentEventMethod(String... methodNames) {
        super(methodNames);
    }
}
