package com.link_intersystems.lang.ref;

/**
 * A {@link MutableReference}'s referent can be set by clients.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @param <T>
 *            the reference object's referent type.
 * 
 * @since 1.2.0.0
 * @version 1.2.0.0
 */
public interface MutableReference<T> extends Reference<T> {

	/**
	 * Set this {@link Reference}'s referent.
	 * 
	 * @param newReferent
	 *            the object that this reference should refer to.
	 * 
	 * @since 1.2.0.0
	 */
	public void set(T newReferent);
}
