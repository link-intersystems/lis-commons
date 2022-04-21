package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TableColumnModelEventMethod extends EventMethod<TableColumnModelListener, TableColumnModelEvent> {

    public static final String COLUMN_ADDED_NAME = "columnAdded";
    public static final String COLUMN_REMOVED_NAME = "columnRemoved";
    public static final String COLUMN_MOVED_NAME = "columnMoved";
    public static final String COLUMN_MARGIN_CHANGED_NAME = "columnMarginChanged";
    public static final String COLUMN_SELECTION_CHANGED_NAME = "columnSelectionChanged";

    public static final TableColumnModelEventMethod COLUMN_ADDED = new TableColumnModelEventMethod(COLUMN_ADDED_NAME);
    public static final TableColumnModelEventMethod COLUMN_REMOVED = new TableColumnModelEventMethod(COLUMN_REMOVED_NAME);
    public static final TableColumnModelEventMethod COLUMN_MOVED = new TableColumnModelEventMethod(COLUMN_MOVED_NAME);

    public static final ChangeEventMethod COLUMN_MARGIN_CHANGED = new ChangeEventMethod(COLUMN_MARGIN_CHANGED_NAME);
    public static final ListSelectionEventMethod COLUMN_SELECTION_CHANGED = new ListSelectionEventMethod(COLUMN_SELECTION_CHANGED_NAME);


    public TableColumnModelEventMethod(String... methodNames) {
        super(methodNames);
    }
}
