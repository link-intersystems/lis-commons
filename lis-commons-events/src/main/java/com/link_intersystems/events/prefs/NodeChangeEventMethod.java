package com.link_intersystems.events.prefs;

import com.link_intersystems.events.EventMethod;

import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NodeChangeEventMethod extends EventMethod<NodeChangeListener, NodeChangeEvent> {

    public static final String CHILD_ADDED_NAME = "childAdded";
    public static final String CHILD_REMOVED_NAME = "childRemoved";

    public static final NodeChangeEventMethod CHILD_ADDED = new NodeChangeEventMethod(CHILD_ADDED_NAME);
    public static final NodeChangeEventMethod CHILD_REMOVED = new NodeChangeEventMethod(CHILD_REMOVED_NAME);

    public NodeChangeEventMethod(String... methodNames) {
        super(methodNames);
    }
}
