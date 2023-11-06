package com.link_intersystems.util.context;

import static java.util.Objects.requireNonNull;

public class ContextObjectException extends RuntimeException {
    private final ObjectQualifier<?> objectQualifier;

    public ContextObjectException(ObjectQualifier<?> objectQualifier) {
        this.objectQualifier = requireNonNull(objectQualifier);
    }

    public ObjectQualifier<?> getQualifier() {
        return objectQualifier;
    }
}
