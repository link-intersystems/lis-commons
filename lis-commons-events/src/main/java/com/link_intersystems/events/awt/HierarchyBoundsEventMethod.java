package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HierarchyBoundsEventMethod extends EventMethod<HierarchyBoundsListener, HierarchyEvent> {

    public static final String ANCESTOR_MOVED_NAME = "ancestorMoved";
    public static final String ANCESTOR_RESIZED_NAME = "ancestorResized";

    public static final HierarchyBoundsEventMethod ANCESTOR_MOVED = new HierarchyBoundsEventMethod(ANCESTOR_MOVED_NAME);
    public static final HierarchyBoundsEventMethod ANCESTOR_RESIZED = new HierarchyBoundsEventMethod(ANCESTOR_RESIZED_NAME);

    public HierarchyBoundsEventMethod(String... methodNames) {
        super(methodNames);
    }
}
