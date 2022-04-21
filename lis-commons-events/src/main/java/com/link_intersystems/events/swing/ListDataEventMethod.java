package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ListDataEventMethod extends EventMethod<ListDataListener, ListDataEvent> {

    public static final String INTERVAL_ADDED_NAME = "intervalAdded";
    public static final String INTERVAL_REMOVED_NAME = "intervalRemoved";
    public static final String CONTENTS_CHANGED_NAME = "contentsChanged";

    public static final ListDataEventMethod INTERVAL_ADDED = new ListDataEventMethod(INTERVAL_ADDED_NAME);
    public static final ListDataEventMethod INTERVAL_REMOVED = new ListDataEventMethod(INTERVAL_REMOVED_NAME);
    public static final ListDataEventMethod CONTENTS_CHANGED = new ListDataEventMethod(CONTENTS_CHANGED_NAME);

    public ListDataEventMethod(String... methodNames) {
        super(methodNames);
    }
}
