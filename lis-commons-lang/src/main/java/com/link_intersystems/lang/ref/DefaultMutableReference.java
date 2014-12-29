package com.link_intersystems.lang.ref;

/**
 * A {@link MutableReference} implementation that just uses a field to store the
 * reference to another object.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @param <T>
 * @since 1.2.0.0
 */
public class DefaultMutableReference<T> implements MutableReference<T> {

	private T referent;

	/**
	 * Creates a {@link DefaultMutableReference} that references the given
	 * referent object.
	 * 
	 * @since 1.2.0.0
	 * @param referent
	 */
	public DefaultMutableReference(T referent) {
		this.referent = referent;
	}

	/**
	 * Creates a {@link DefaultMutableReference} that has no referent object (
	 * <code>null</code>).
	 * 
	 * @since 1.2.0.0
	 */
	public DefaultMutableReference() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void set(T newReferent) {
		this.referent = newReferent;
	}

	/**
	 * {@inheritDoc}
	 */
	public T get() {
		return referent;
	}

}
