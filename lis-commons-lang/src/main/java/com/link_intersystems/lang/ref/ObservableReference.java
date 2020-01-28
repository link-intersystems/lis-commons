package com.link_intersystems.lang.ref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observer;

/**
 * A {@link Reference} that notifes {@link Observer}s when it's referent
 * changes.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @param <T>
 * @since 1.2.0.0
 */
public interface ObservableReference<T> extends MutableReference<T> {

	/**
	 * The property identifier of this {@link MutableReference}'s referent that
	 * is used by {@link PropertyChangeEvent}s.
	 * 
	 * @since 1.2.0.0
	 */
	public static final String PROPERTY_REFERENT = "referent";

	/**
	 * Add the {@link PropertyChangeListener} to this
	 * {@link ObservableReference}. Every time this reference gets changes all
	 * registered {@link PropertyChangeListener}s will be notified.
	 * 
	 * @param propertyChangeListener
	 * 
	 * @since 1.2.0.0
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

	/**
	 * Removes the {@link PropertyChangeListener} so that it will not be
	 * notified anymore when this {@link ObservableReference}'s referent
	 * changes.
	 * 
	 * @param propertyChangeListener
	 * 
	 * @since 1.2.0.0
	 */
	void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

}
