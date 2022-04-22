package com.link_intersystems.events.sql;

import com.link_intersystems.events.EventMethod;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class RowSetEventMethod extends EventMethod<RowSetListener, RowSetEvent> {

    public static final String ROW_SET_CHANGED_NAME = "rowSetChanged";
    public static final String ROW_CHANGED_NAME = "rowChanged";
    public static final String CURSOR_MOVED_NAME = "cursorMoved";

    public static final RowSetEventMethod ROW_SET_CHANGED = new RowSetEventMethod(ROW_SET_CHANGED_NAME);
    public static final RowSetEventMethod ROW_CHANGED = new RowSetEventMethod(ROW_CHANGED_NAME);
    public static final RowSetEventMethod CURSOR_MOVED = new RowSetEventMethod(CURSOR_MOVED_NAME);

    public RowSetEventMethod(String... methodNames) {
        super(methodNames);
    }
}
