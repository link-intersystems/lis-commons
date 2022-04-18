package com.link_intersystems.beans;


/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyWriteException extends PropertyException {

    public PropertyWriteException(Class<?> beanType, String propertyName) {
        super(beanType, propertyName);
    }

    public PropertyWriteException(Class<?> beanType, String propertyName, Throwable cause) {
        super(beanType, propertyName, cause);
    }
}
