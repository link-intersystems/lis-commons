package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class CellEditorEventMethod extends EventMethod<CellEditorListener, ChangeEvent> {

    public static final String EDITING_STOPPED_NAME = "editingStopped";
    public static final String EDITING_CANCELED_NAME = "editingCanceled";

    public static final CellEditorEventMethod EDITING_STOPPED = new CellEditorEventMethod(EDITING_STOPPED_NAME);
    public static final CellEditorEventMethod EDITING_CANCELED = new CellEditorEventMethod(EDITING_CANCELED_NAME);

    public CellEditorEventMethod(String... methodNames) {
        super(methodNames);
    }
}
