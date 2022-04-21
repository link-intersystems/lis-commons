package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TableModelEventMethod extends EventMethod<TableModelListener, TableModelEvent> {

    public static final String TABLE_CHANGED_NAME = "tableChanged";

    public static final TableModelEventMethod TABLE_CHANGED = new TableModelEventMethod(TABLE_CHANGED_NAME);

    public TableModelEventMethod(String... methodNames) {
        super(methodNames);
    }
}
