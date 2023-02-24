package com.link_intersystems.swing.selection;

import javax.swing.event.EventListenerList;
import java.util.Objects;

import static java.util.Objects.*;

public class SelectionProviderSupport<E> implements SelectionProvider<E> {

    private EventListenerList listenerList = new EventListenerList();
    private Selection<E> selection = Selection.empty();
    private Object changeEventSource;

    public SelectionProviderSupport(Object changeEventSource) {
        this.changeEventSource = requireNonNull(changeEventSource);
    }

    @Override
    public void addSelectionChangedListener(SelectionListener<E> listener) {
        listenerList.add(SelectionListener.class, listener);
    }

    @Override
    public void removeSelectionChangedListener(SelectionListener<E> listener) {
        listenerList.remove(SelectionListener.class, listener);
    }

    @Override
    public Selection<E> getSelection() {
        return selection;
    }

    public void setSelection(Selection<E> selection) {
        Selection oldSelection = this.selection;

        this.selection = selection == null ? Selection.empty() : selection;

        if (!Objects.equals(oldSelection, this.selection)) {
            fireSelectionChanged();
        }
    }

    public void setSubtypeSelection(Selection<? extends E> selection) {
        setSelection(selection == null ? Selection.empty() : Selection.adapted(selection));
    }

    protected void fireSelectionChanged() {
        SelectionListener[] selectionListeners = listenerList.getListeners(SelectionListener.class);
        if (selectionListeners.length != 0) {
            SelectionChangeEvent<E> selectionChangeEvent = new SelectionChangeEvent<>(changeEventSource, getSelection());
            for (int i = 0; i < selectionListeners.length; i++) {
                selectionListeners[i].selectionChanged(selectionChangeEvent);
            }
        }
    }
}
