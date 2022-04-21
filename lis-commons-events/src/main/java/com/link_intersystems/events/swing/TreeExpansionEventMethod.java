package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TreeExpansionEventMethod extends EventMethod<TreeExpansionListener, TreeExpansionEvent> {

    public static final String EXPANDED_NAME = "treeExpanded";
    public static final String COLLAPSED_NAME = "treeCollapsed";

    public static final TreeExpansionEventMethod EXPANDED = new TreeExpansionEventMethod(EXPANDED_NAME);
    public static final TreeExpansionEventMethod COLLAPSED = new TreeExpansionEventMethod(COLLAPSED_NAME);

    public TreeExpansionEventMethod(String... methodNames) {
        super(methodNames);
    }
}
