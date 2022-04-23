package com.link_intersystems.beans;

import com.link_intersystems.lang.ref.Reference;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.function.Consumer;

public class PropertyChangeSourceSupport<T> implements Reference<T> {

    private PropertyChangeListenerAdapter adapter = new PropertyChangeListenerAdapter();
    private Bean<T> referent;
    private Consumer<PropertyChangeEvent> propertyChangeListener;
    private boolean enabled = true;

    public PropertyChangeSourceSupport(Consumer<PropertyChangeEvent> propertyChangeListener) {
        this.propertyChangeListener = Objects.requireNonNull(propertyChangeListener);
    }

    public PropertyChangeSourceSupport(PropertyChangeListener propertyChangeListener) {
        this((Consumer<PropertyChangeEvent>) propertyChangeListener::propertyChange);
    }

    public void setReferent(T bean) {

        if (this.referent != null) {
            this.referent.removeListener(adapter);
        }

        if (bean == null) {
            this.referent = null;
        } else {
            BeansFactory beansFactory = BeansFactory.getDefault();
            try {
                this.referent = beansFactory.createBean(bean);
            } catch (BeanClassException e) {
                throw new IllegalStateException(e);
            }
        }

        if (this.referent != null && enabled) {
            this.referent.addListener(adapter);
        }
    }

    public void setPropertyChangeEventsEnabled(boolean enabled) {
        this.enabled = enabled;
        setReferent(referent.getBeanObject());
    }

    public T get() {
        return referent == null ? null : referent.getBeanObject();
    }

    private class PropertyChangeListenerAdapter implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            propertyChangeListener.accept(evt);
        }

    }

}
