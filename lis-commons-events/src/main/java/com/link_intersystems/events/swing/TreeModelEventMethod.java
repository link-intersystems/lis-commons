package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TreeModelEventMethod extends EventMethod<TreeModelListener, TreeModelEvent> {

    public static final String NODES_CHANGED_NAME = "treeNodesChanged";
    public static final String NODES_INSERTED_NAME = "treeNodesInserted";
    public static final String NODES_REMOVED_NAME = "treeNodesRemoved";
    public static final String STRUCTURE_CHANGED_NAME = "treeStructureChanged";

    public static final TreeModelEventMethod NODES_CHANGED = new TreeModelEventMethod(NODES_CHANGED_NAME);
    public static final TreeModelEventMethod NODES_INSERTED = new TreeModelEventMethod(NODES_INSERTED_NAME);
    public static final TreeModelEventMethod NODES_REMOVED = new TreeModelEventMethod(NODES_REMOVED_NAME);
    public static final TreeModelEventMethod STRUCTURE_CHANGED = new TreeModelEventMethod(STRUCTURE_CHANGED_NAME);

    public TreeModelEventMethod(String... methodNames) {
        super(methodNames);
    }
}
