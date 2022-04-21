package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ListSelectionEventMethod extends EventMethod<ListSelectionListener, ListSelectionEvent> {

    public static final String VALUE_CHANGED_NAME = "valueChanged";

    public static final ListSelectionEventMethod VALUE_CHANGED = new ListSelectionEventMethod(VALUE_CHANGED_NAME);

    public ListSelectionEventMethod(String... methodNames) {
        super(methodNames);
    }
}
