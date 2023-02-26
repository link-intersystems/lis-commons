package com.link_intersystems.swing.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultSelection<E> extends AbstractSelection<E> {

    private List<E> selectedElements;

    public DefaultSelection(Selection<? extends E> selection) {
        this(selection.toList());
    }

    public <T extends E> DefaultSelection(T... selectedElements) {
        this(Arrays.asList(selectedElements));
    }

    public DefaultSelection(List<? extends E> selectedElements) {
        this.selectedElements = Collections.unmodifiableList(new ArrayList<>(selectedElements));
    }

    @Override
    public int size() {
        return selectedElements.size();
    }

    @Override
    public List<E> toList() {
        return selectedElements;
    }
}
