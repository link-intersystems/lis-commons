package com.link_intersystems.swing.list;

import javax.swing.*;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * Adapts a {@link ListModel} to a {@link java.util.List}. This allows you to
 * use all methods that a {@link java.util.List} defines like {@link List#stream()},
 * {@link List#indexOf(Object)} or {@link List#contains(Object)}.
 *
 * @param <E>
 */
public class ListModelListAdapter<E> extends AbstractList<E> {

    private ListModel<E> listModel;

    public ListModelListAdapter(ListModel<E> listModel) {
        this.listModel = Objects.requireNonNull(listModel);
    }

    public ListModel<E> getListModel() {
        return listModel;
    }

    @Override
    public E get(int index) {
        return listModel.getElementAt(index);
    }

    @Override
    public int size() {
        return listModel.getSize();
    }
}
