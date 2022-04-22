package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AncestorEventMethod extends EventMethod<AncestorListener, AncestorEvent> {

    public static final String ADDED_NAME = "ancestorAdded";
    public static final String REMOVED_NAME = "ancestorRemoved";
    public static final String MOVED_NAME = "ancestorMoved";

    public static final AncestorEventMethod ADDED = new AncestorEventMethod(ADDED_NAME);
    public static final AncestorEventMethod REMOVED = new AncestorEventMethod(REMOVED_NAME);
    public static final AncestorEventMethod MOVED = new AncestorEventMethod(MOVED_NAME);

    public AncestorEventMethod(String... methodNames) {
        super(methodNames);
    }
}
