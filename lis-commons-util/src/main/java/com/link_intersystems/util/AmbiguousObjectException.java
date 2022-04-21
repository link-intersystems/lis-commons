package com.link_intersystems.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link AmbiguousObjectException} presents an ambiguous exception case in which
 * multiple objects are available when only one should be.
 */
public class AmbiguousObjectException extends RuntimeException {

    private static final long serialVersionUID = 1402395367874649937L;

    private Collection<?> candidates;

    public AmbiguousObjectException(Collection<?> candidates) {
        this(candidates, "Ambiguous objects");
    }

    public AmbiguousObjectException(Collection<?> candidates, String msg) {
        this(candidates, msg, null);
    }

    public AmbiguousObjectException(Collection<?> candidates, String msg, Throwable cause) {
        super(msg, cause);
        this.candidates = Objects.requireNonNull(candidates);
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        StringBuilder sb = new StringBuilder(message);
        sb.append(": ");

        Iterator<?> iterator = candidates.iterator();
        while (iterator.hasNext()) {
            Object candidate = iterator.next();
            sb.append(candidate);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    /**
     * @param <T> the type of the candidates. This method is generic
     *            because an exception can not be generic.
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Collection<T> getCandidates() {
        return (Collection<T>) candidates;
    }

}
