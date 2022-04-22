package com.link_intersystems.events.naming;

import com.link_intersystems.events.EventMethod;

import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NamespaceChangeEventMethod extends EventMethod<NamespaceChangeListener, NamingEvent> {

    public static final String OBJECT_ADDED_NAME = "objectAdded";
    public static final String OBJECT_REMOVED_NAME = "objectRemoved";
    public static final String OBJECT_RENAMED_NAME = "objectRenamed";

    public static final NamespaceChangeEventMethod OBJECT_ADDED = new NamespaceChangeEventMethod(OBJECT_ADDED_NAME);
    public static final NamespaceChangeEventMethod OBJECT_REMOVED = new NamespaceChangeEventMethod(OBJECT_REMOVED_NAME);
    public static final NamespaceChangeEventMethod OBJECT_RENAMED = new NamespaceChangeEventMethod(OBJECT_RENAMED_NAME);

    public NamespaceChangeEventMethod(String... methodNames) {
        super(methodNames);
    }
}
