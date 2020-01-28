package com.link_intersystems.beans;

import java.beans.PropertyChangeListener;
import java.util.Objects;

import com.link_intersystems.lang.ref.Reference;

public class PropertyChangeSourceSupport<T extends PropertyChangeSource> implements Reference<T> {

	private T referent;
	private PropertyChangeListener propertyChangeListener;

	private boolean enabled = true;

	public PropertyChangeSourceSupport(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeListener = Objects.requireNonNull(propertyChangeListener);
	}

	public void setReferent(T referent) {
		if (this.referent != null) {
			this.referent.removePropertyChangeListener(propertyChangeListener);
		}

		this.referent = referent;

		if (this.referent != null && enabled) {
			this.referent.addPropertyChangeListener(propertyChangeListener);
		}
	}

	public void setPropertyChangeEventsEnabled(boolean enabled) {
		this.enabled = enabled;
		setReferent(referent);
	}

	public T get() {
		return referent;
	}

}
