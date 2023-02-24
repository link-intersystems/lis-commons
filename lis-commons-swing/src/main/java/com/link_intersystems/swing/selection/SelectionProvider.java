package com.link_intersystems.swing.selection;

public interface SelectionProvider<E> {

    void addSelectionChangedListener(SelectionListener<E> listener);

    void removeSelectionChangedListener(SelectionListener<E> listener);

    Selection<E> getSelection();

}
