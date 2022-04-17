package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException(Throwable cause) {
        super(cause);
    }

    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
