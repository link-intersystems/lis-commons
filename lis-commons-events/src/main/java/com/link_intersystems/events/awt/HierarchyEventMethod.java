package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HierarchyEventMethod extends EventMethod<HierarchyListener, HierarchyEvent> {

    public static final String CHANGED_NAME = "hierarchyChanged";

    public static final HierarchyEventMethod CHANGED = new HierarchyEventMethod(CHANGED_NAME);

    public HierarchyEventMethod(String... methodNames) {
        super(methodNames);
    }
}
