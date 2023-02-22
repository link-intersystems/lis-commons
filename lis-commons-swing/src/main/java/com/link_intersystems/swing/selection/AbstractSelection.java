package com.link_intersystems.swing.selection;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import static java.util.Objects.*;

public abstract class AbstractSelection implements Selection {

    private EventListenerList eventListenerList = new EventListenerList();

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        eventListenerList.add(ChangeListener.class, requireNonNull(changeListener));
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        eventListenerList.remove(ChangeListener.class, changeListener);
    }

    protected void fireChanged() {
        ChangeListener[] changeListeners = eventListenerList.getListeners(ChangeListener.class);
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (ChangeListener changeListener : changeListeners) {
            changeListener.stateChanged(changeEvent);
        }
    }

}
