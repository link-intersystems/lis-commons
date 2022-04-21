package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ContainerEventMethod extends EventMethod<ContainerListener, ContainerEvent> {

    public static final String ADDED_NAME = "componentAdded";
    public static final String REMOVED_NAME = "componentRemoved";

    public static final ContainerEventMethod ADDED = new ContainerEventMethod(ADDED_NAME);
    public static final ContainerEventMethod REMOVED = new ContainerEventMethod(REMOVED_NAME);

    public ContainerEventMethod(String... methodNames) {
        super(methodNames);
    }
}
