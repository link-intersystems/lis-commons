package com.link_intersystems.events.sql;

import com.link_intersystems.events.EventMethod;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ConnectionEventMethod extends EventMethod<ConnectionEventListener, ConnectionEvent> {

    public static final String CLOSED_NAME = "connectionClosed";
    public static final String ERROR_OCCURED_NAME = "connectionErrorOccurred";

    public static final ConnectionEventMethod CLOSED = new ConnectionEventMethod(CLOSED_NAME);
    public static final ConnectionEventMethod ERROR_OCCURED = new ConnectionEventMethod(ERROR_OCCURED_NAME);

    public ConnectionEventMethod(String... methodNames) {
        super(methodNames);
    }
}
