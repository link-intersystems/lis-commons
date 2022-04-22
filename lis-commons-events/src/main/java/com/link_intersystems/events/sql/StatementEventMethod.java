package com.link_intersystems.events.sql;

import com.link_intersystems.events.EventMethod;

import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class StatementEventMethod extends EventMethod<StatementEventListener, StatementEvent> {

    public static final String CLOSED_NAME = "statementClosed";
    public static final String ERROR_OCCURRED_NAME = "statementErrorOccurred";

    public static final StatementEventMethod CLOSED = new StatementEventMethod(CLOSED_NAME);
    public static final StatementEventMethod ERROR_OCCURRED = new StatementEventMethod(ERROR_OCCURRED_NAME);

    public StatementEventMethod(String... methodNames) {
        super(methodNames);
    }
}
