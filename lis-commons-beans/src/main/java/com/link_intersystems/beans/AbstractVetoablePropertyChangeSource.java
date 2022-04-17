package com.link_intersystems.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

public abstract class AbstractVetoablePropertyChangeSource extends AbstractPropertyChangeSource
        implements VetoablePropertyChangeSource {

    private VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    @Override
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    @Override
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    @Override
    public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(propertyName, listener);
    }

    @Override
    public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(propertyName, listener);
    }

    protected void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
            throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }

    protected void fireVetoableChange(String propertyName, int oldValue, int newValue) throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }

    protected void fireVetoableChange(String propertyName, boolean oldValue, boolean newValue)
            throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }

    protected void fireVetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(event);
    }

}
