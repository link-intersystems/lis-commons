package com.link_intersystems.beans;

/**
 * A {@link BeanEvent} provides access to the event mechanism of a {@link Bean} instance.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 *
 * @see Bean#getApplicableBeanEvent(Object)
 */
public interface BeanEvent {

    BeanEventType getType();

    /**
     * Adds the given listener to this {@link BeanEvent} if the {@link Bean}, which provided this {@link BeanEvent},
     * has an add method for the given listener's type. Otherwise it throws an {@link UnsupportedOperationException}.
     *
     * @param listener
     * @throws UnsupportedOperationException the {@link Bean}, which provided this {@link BeanEvent},
     *                                       dosen't have an add method for the given listener's type.
     */
    void addListener(Object listener) throws UnsupportedOperationException;

    /**
     * Removes the given listener from this {@link BeanEvent} if the {@link Bean}, which provided this {@link BeanEvent},
     * has a remove method for the given listener's type. Otherwise it throws an {@link UnsupportedOperationException}.
     *
     * @param listener
     * @throws UnsupportedOperationException the {@link Bean}, which provided this {@link BeanEvent},
     *                                       dosen't have a remove method for the given listener's type.
     */
    void removeListener(Object listener) throws UnsupportedOperationException;
}
