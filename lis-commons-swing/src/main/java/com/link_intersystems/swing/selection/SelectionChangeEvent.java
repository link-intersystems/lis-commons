package com.link_intersystems.swing.selection;

import javax.swing.event.ChangeEvent;

import static java.util.Objects.*;

public class SelectionChangeEvent<E> extends ChangeEvent {

    private Selection<E> oldSelection;
    private Selection<E> newSelection;

    public SelectionChangeEvent(Object source, Selection<E> oldSelection, Selection<E> newSelection) {
        super(source);
        this.oldSelection = requireNonNull(oldSelection);
        this.newSelection = requireNonNull(newSelection);
    }

    public Selection<E> getOldSelection() {
        return oldSelection;
    }

    public Selection<E> getNewSelection() {
        return newSelection;
    }
}
