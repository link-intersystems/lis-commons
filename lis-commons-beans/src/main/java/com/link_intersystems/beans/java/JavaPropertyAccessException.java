package com.link_intersystems.beans.java;

import com.link_intersystems.beans.Messages;
import com.link_intersystems.beans.PropertyAccessException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyAccessException extends PropertyAccessException {

    private JavaProperty<?> property;

    protected JavaPropertyAccessException(JavaProperty<?> property, PropertyAccessType propertyAccessType) {
        this(property, propertyAccessType, null);
    }

    public JavaPropertyAccessException(JavaProperty<?> property, PropertyAccessType propertyAccessType, Throwable cause) {
        super(property.getBean().getClass(), property.getName(), propertyAccessType, cause);
        this.property = property;
    }

    @Override
    public String getLocalizedMessage() {
        if (PropertyAccessType.READ.equals(getPropertyAccessType())) {
            return Messages.formatPropertyNotReadable(property);
        } else {
            return Messages.formatPropertyNotWritable(property);
        }
    }
}
