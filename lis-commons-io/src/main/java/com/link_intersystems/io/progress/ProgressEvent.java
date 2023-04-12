package com.link_intersystems.io.progress;

import java.util.EventObject;
import java.util.Objects;

public class ProgressEvent extends EventObject {

    private final int min;
    private final int max;
    private final int value;


    public ProgressEvent(Object source, int min, int max, int value) {
        super(source);
        if (max < min) {
            throw new IllegalArgumentException("max must be equal to or greater than min");
        }
        this.min = min;
        this.max = max;
        if (value < min) {
            throw new IllegalArgumentException("value must be equal to or greater than min");
        }
        this.value = value;
    }

    public boolean isDone() {
        return getValue() >= getMax();
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgressEvent that = (ProgressEvent) o;
        return getMin() == that.getMin() && getMax() == that.getMax() && getValue() == that.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMin(), getMax(), getValue());
    }

    @Override
    public String toString() {
        return "ProgressEvent{" +
                "min=" + min +
                ", max=" + max +
                ", value=" + value +
                "} " + super.toString();
    }
}
