package com.link_intersystems.lang.ref;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableReferenceAdapter<T> implements ObservableReference<T> {
	
	
	private MutableReference<T> mutableReference;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public ObservableReferenceAdapter(MutableReference<T> mutableReference) {
		this.mutableReference = mutableReference;
	}

	public T get() {
		return mutableReference.get();
	}
	
	
	public void set(T newReferent) {
		T oldValue = mutableReference.get();
		mutableReference.set(newReferent);
		propertyChangeSupport.firePropertyChange(PROPERTY_REFERENT, oldValue, newReferent);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
