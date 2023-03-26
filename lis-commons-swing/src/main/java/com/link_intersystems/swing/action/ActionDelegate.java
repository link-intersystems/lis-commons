package com.link_intersystems.swing.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public abstract class ActionDelegate implements Action {

    private Action delegate;

    public ActionDelegate(Action delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object getValue(String key) {
        return delegate.getValue(key);
    }

    @Override
    public void putValue(String key, Object value) {
        delegate.putValue(key, value);
    }

    @Override
    public void setEnabled(boolean b) {
        delegate.setEnabled(b);
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        delegate.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        delegate.removePropertyChangeListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        delegate.actionPerformed(e);
    }
}
