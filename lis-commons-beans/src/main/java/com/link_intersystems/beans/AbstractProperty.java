package com.link_intersystems.beans;


import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.*;

public abstract class AbstractProperty implements Property {

    private final PropertyDesc propertyDescriptor;

    public AbstractProperty(PropertyDesc propertyDescriptor) {
        this.propertyDescriptor = requireNonNull(propertyDescriptor);
    }


    @Override
    public PropertyDesc getPropertyDesc() {
        return propertyDescriptor;
    }

    /**
     * Gets the value of this {@link AbstractProperty}.
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
        Object beanObject = getBeanObject();
        return propertyDesc.getPropertyValue(beanObject);
    }

    protected abstract Object getBeanObject();

    /**
     * Sets the value of this {@link AbstractProperty}.
     *
     * @param propertyValue the value to set.
     * @throws PropertyReadException if this {@link AbstractProperty}'s value could not be set. If the thrown
     *                               {@link PropertyWriteException} has no cause this property is not
     *                               writable (has no property setter method).
     * @since 1.2.0;
     */
    @Override
    public void setValue(Object propertyValue) {
        PropertyDesc propertyDesc = getPropertyDesc();
        Object beanObject = getBeanObject();
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
        AbstractProperty other = (AbstractProperty) obj;
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

    @Override
    public String toString() {
        return "Property{" +
                "propertyDescriptor=" + propertyDescriptor +
                '}';
    }
}
