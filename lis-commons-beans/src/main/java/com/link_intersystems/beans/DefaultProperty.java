package com.link_intersystems.beans;


import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.*;

public class DefaultProperty implements Property {

    private final Supplier<Object> beanSupplier;
    private final PropertyDesc propertyDescriptor;

    public DefaultProperty(Supplier<Object> beanSupplier, PropertyDesc propertyDescriptor) {
        this.beanSupplier = requireNonNull(beanSupplier);
        this.propertyDescriptor = requireNonNull(propertyDescriptor);
    }


    @Override
    public PropertyDesc getPropertyDesc() {
        return propertyDescriptor;
    }

    /**
     * Gets the value of this {@link DefaultProperty}.
     *
     * @return the value of this property.
     * @throws PropertyReadException if the property could not be accessed for any reason. If the
     *                               thrown {@link PropertyReadException} has no cause this property
     *                               is not readable (has no property getter method).
     * @since 1.2.0;
     */
    @Override
    public <T> T getValue() {
        PropertyDesc propertyDesc = getPropertyDesc();
        Object beanObject = beanSupplier.get();
        return propertyDesc.getPropertyValue(beanObject);
    }

    /**
     * Sets the value of this {@link DefaultProperty}.
     *
     * @param propertyValue the value to set.
     * @throws PropertyReadException if this {@link DefaultProperty}'s value could not be set. If the thrown
     *                               {@link PropertyWriteException} has no cause this property is not
     *                               writable (has no property setter method).
     * @since 1.2.0;
     */
    @Override
    public void setValue(Object propertyValue) {
        PropertyDesc propertyDesc = getPropertyDesc();
        Object beanObject = beanSupplier.get();
        propertyDesc.setPropertyValue(beanObject, propertyValue);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getPropertyDesc().hashCode();
        Object value = getValue();

        if (value != null) {
            Class<?> valueClass = value.getClass();
            if (valueClass.isArray()) {
                result = prime * result + Arrays.deepHashCode((Object[]) value);
            } else {
                result = prime * result + value.hashCode();
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultProperty other = (DefaultProperty) obj;
        if (!getPropertyDesc().equals(other.getPropertyDesc()))
            return false;

        Object value = getValue();
        if (value == null) {
            return other.getValue() == null;
        } else {
            Object otherValue = other.getValue();

            if (otherValue == null) {
                return false;
            }

            Class<?> valueClass = value.getClass();
            Class<?> otherValueClass = otherValue.getClass();
            if (valueClass.isArray() && otherValueClass.isArray()) {
                return Objects.deepEquals(value, otherValue);
            } else if (!valueClass.isArray() && !otherValueClass.isArray()) {
                return value.equals(otherValue);
            } else {
                return false;
            }
        }
    }
}
