package com.link_intersystems.swing.selection;

import javax.swing.event.ChangeEvent;

import static java.util.Objects.*;

public class SelectionChangeEvent<E> extends ChangeEvent {

    private Selection<E> selection;

    /**
     * Constructs a ChangeEvent object.
     *
     * @param source the Object that is the source of the event
     *               (typically <code>this</code>)
     */
    public SelectionChangeEvent(Object source, Selection<E> selection) {
        super(source);
        this.selection = requireNonNull(selection);
    }

    public Selection<E> getSelection() {
        return selection;
    }
}
