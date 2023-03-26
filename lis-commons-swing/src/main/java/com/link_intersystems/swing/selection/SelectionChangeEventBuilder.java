package com.link_intersystems.swing.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.*;

public class SelectionChangeEventBuilder<E> {

    private final Object eventSource;
    private List<E> newElements;
    private List<E> oldElements;

    public SelectionChangeEventBuilder(Object eventSource) {
        this.eventSource = requireNonNull(eventSource);
    }

    public SelectionChangeEventBuilder<E> withNewElements(E... newElements) {
        return withNewElements(Arrays.asList(newElements));
    }

    public SelectionChangeEventBuilder<E> withNewElements(List<E> newElements) {
        this.newElements = new ArrayList<>(newElements);
        return this;
    }

    public SelectionChangeEventBuilder<E> withOldElements(E... oldElements) {
        return withOldElements(Arrays.asList(oldElements));
    }

    public SelectionChangeEventBuilder<E> withOldElements(List<E> oldElements) {
        this.oldElements = new ArrayList<>(oldElements);
        return this;
    }

    public SelectionChangeEvent<E> build() {
        Selection<E> oldSelection = Selection.empty();
        if (oldElements != null) {
            oldSelection = new DefaultSelection<E>(oldElements);
        }

        Selection<E> newSelection = Selection.empty();
        if (newSelection != null) {
            newSelection = new DefaultSelection<E>(newElements);
        }

        SelectionChangeEvent<E> eSelectionChangeEvent = new SelectionChangeEvent<>(eventSource, oldSelection, newSelection);

        this.newElements = null;
        this.oldElements = null;

        return eSelectionChangeEvent;
    }
}
