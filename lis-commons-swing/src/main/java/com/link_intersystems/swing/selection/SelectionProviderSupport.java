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

    public void setSelection(Selection<E> newSelection) {
        newSelection = newSelection == null ? Selection.empty() : newSelection;

        Selection oldSelection = selection;

        selection = newSelection;

        if (!isEqual(oldSelection, newSelection)) {
            selectionChanged(oldSelection, newSelection);
        }
    }

    private boolean isEqual(Selection<E> oldSelection, Selection<E> currentSelection) {
        return Objects.equals(oldSelection, currentSelection);
    }

    private void selectionChanged(Selection<E> oldSelection, Selection<E> newSelection) {
        SelectionListener<E>[] selectionListeners = listenerList.getListeners(SelectionListener.class);

        if (selectionListeners.length != 0) {
            SelectionChangeEvent<E> selectionChangeEvent = new SelectionChangeEvent<>(changeEventSource, oldSelection, newSelection);
            fireSelectionChanged(selectionListeners, selectionChangeEvent);
        }

    }

    private void fireSelectionChanged(SelectionListener<E>[] selectionListeners, SelectionChangeEvent<E> selectionChangeEvent) {
        for (int i = 0; i < selectionListeners.length; i++) {
            selectionListeners[i].selectionChanged(selectionChangeEvent);
        }
    }
}
