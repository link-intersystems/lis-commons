package com.link_intersystems.swing.list;

import com.link_intersystems.swing.selection.Selection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;

/**
 * A {@link ListModelSelection} represents the selected element of an
 * {@link ListModel}. It brings a {@link ListModel} and
 * {@link ListSelectionModel} together.
 *
 * @param <E>
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
public class ListModelSelection<E> implements Selection<E> {


    private ListModel<E> listModel;

    private ListSelectionModelExt listSelectionModelExt = new ListSelectionModelExt(new DefaultListSelectionModel());

    public ListModelSelection() {
        this(new DefaultListModel<>());
    }

    public ListModelSelection(ListModel<E> listModel) {
        this(listModel, new DefaultListSelectionModel());
    }

    public ListModelSelection(ListModel<E> listModel, ListSelectionModel listSelectionModel) {
        setListModel(listModel);
        setSelectionModel(listSelectionModel);
    }

    public void setListModel(ListModel<E> listModel) {
        this.listModel = requireNonNull(listModel);
    }

    public ListModel<E> getListModel() {
        return listModel;
    }

    public ListSelectionModel getListSelectionModel() {
        return listSelectionModelExt.getListSelectionModel();
    }

    public void setSelectionModel(ListSelectionModel listSelectionModel) {
        listSelectionModelExt = new ListSelectionModelExt(requireNonNull(listSelectionModel));
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return listSelectionModelExt.getSelectedIndexCount();
    }

    @Override
    public List<E> toList() {
        List<E> elementSelection = new ArrayList<>();

        int[] selectedIndexes = listSelectionModelExt.getSelectedIndexes();

        for (int i = 0; i < selectedIndexes.length; i++) {
            int selectedIndex = selectedIndexes[i];
            if (selectedIndex < listModel.getSize()) {
                E elementAt = listModel.getElementAt(selectedIndex);
                elementSelection.add(elementAt);
            }
        }

        return elementSelection;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListModelSelection<?> that = (ListModelSelection<?>) o;

        return Objects.equals(listModel, that.listModel) && Objects.equals(listSelectionModelExt, that.listSelectionModelExt);
    }

    @Override
    public int hashCode() {
        return hash(listModel, listSelectionModelExt);
    }
}