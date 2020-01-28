package com.link_intersystems.lang;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class AmbigousObjectException extends RuntimeException {

	private static final long serialVersionUID = 1402395367874649937L;

	private Collection<?> candidates;

	public AmbigousObjectException(Collection<?> candidates) {
		this(candidates, "Ambigous objects");
	}

	public AmbigousObjectException(Collection<?> candidates, String msg) {
		this(candidates, msg, null);
	}

	public AmbigousObjectException(Collection<?> candidates, String msg, Throwable cause) {
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
			Object candidate = (Object) iterator.next();
			sb.append(candidate);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> getCandidates() {
		return (Collection<T>) candidates;
	}

}
