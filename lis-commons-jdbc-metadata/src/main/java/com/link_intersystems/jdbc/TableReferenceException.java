package com.link_intersystems.jdbc;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TableReferenceException extends RuntimeException {
    public TableReferenceException(String msg) {
        super(msg);
    }

    public TableReferenceException(Throwable cause) {
        super(cause);
    }

    public TableReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
