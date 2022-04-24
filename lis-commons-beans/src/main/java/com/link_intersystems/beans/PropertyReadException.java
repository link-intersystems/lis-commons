package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyReadException extends PropertyException {


    public PropertyReadException(Class<?> beanType, String propertyName) {
        super(beanType, propertyName);
    }

    public PropertyReadException(Class<?> beanType, String propertyName, Throwable cause) {
        super(beanType, propertyName, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return "Property " + getPropertyName() + " is not readable.";
    }
}
