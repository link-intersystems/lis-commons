package com.link_intersystems.beans;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSharedPropertyEditor implements SharedPropertyEditor {

	class PropertyChangeEventAdapter implements PropertyChangeListener {

		private List<PropertyChangeListener> propetyChangeListeners = new ArrayList<>();

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(AbstractSharedPropertyEditor.this,
					evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			propertyChangeEvent.setPropagationId(evt.getPropagationId());

			propetyChangeListeners.forEach(l -> l.propertyChange(propertyChangeEvent));

		}

		public void addPropertyChangeListener(PropertyChangeListener listener) {
			propetyChangeListeners.add(listener);
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
			propetyChangeListeners.remove(listener);
		}

		public void release() {
			propertyEditor.removePropertyChangeListener(this);
		}

		public void releaseListeners() {
			propetyChangeListeners.clear();
		}

	}

	protected PropertyChangeEventAdapter propertyChangeEventAdapter = new PropertyChangeEventAdapter();
	protected PropertyEditor propertyEditor;

	public AbstractSharedPropertyEditor(PropertyEditor propertyEditor) {
		this.propertyEditor = propertyEditor;
		propertyEditor.addPropertyChangeListener(propertyChangeEventAdapter);
	}

	public void setValue(Object value) {
		getPropertyEditor().setValue(value);
	}

	public Object getValue() {
		return getPropertyEditor().getValue();
	}

	public boolean isPaintable() {
		return getPropertyEditor().isPaintable();
	}

	public void paintValue(Graphics gfx, Rectangle box) {
		getPropertyEditor().paintValue(gfx, box);
	}

	public String getJavaInitializationString() {
		return getPropertyEditor().getJavaInitializationString();
	}

	public String getAsText() {
		return getPropertyEditor().getAsText();
	}

	public void setAsText(String text) throws IllegalArgumentException {
		getPropertyEditor().setAsText(text);
	}

	public String[] getTags() {
		return getPropertyEditor().getTags();
	}

	public Component getCustomEditor() {
		return getPropertyEditor().getCustomEditor();
	}

	public boolean supportsCustomEditor() {
		return getPropertyEditor().supportsCustomEditor();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeEventAdapter.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeEventAdapter.removePropertyChangeListener(listener);
	}

	private PropertyEditor getPropertyEditor() {
		if (propertyEditor == null) {
			throw new IllegalStateException("SharedPropertyEditor has been released.");
		}

		return propertyEditor;
	}

	@Override
	public void release() {
		propertyChangeEventAdapter.release();
		this.propertyEditor = null;
	}

	protected void releaseListeners() {
		propertyChangeEventAdapter.releaseListeners();
	}

}