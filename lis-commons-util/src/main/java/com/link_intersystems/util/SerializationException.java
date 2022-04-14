package com.link_intersystems.util;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SerializationException extends RuntimeException {
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
