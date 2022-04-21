package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TreeSelectionEventMethod extends EventMethod<TreeSelectionListener, TreeSelectionEvent> {

    public static final String VALUE_CHANGED_NAME = "valueChanged";

    public static final TreeSelectionEventMethod VALUE_CHANGED = new TreeSelectionEventMethod(VALUE_CHANGED_NAME);

    public TreeSelectionEventMethod(String... methodNames) {
        super(methodNames);
    }
}
