package com.link_intersystems.swing.list;

import com.link_intersystems.swing.selection.AbstractSelection;
import com.link_intersystems.swing.selection.DefaultSelection;
import com.link_intersystems.swing.selection.Selection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Captures the selected elements of {@link ListModel}, as selected by a {@link ListSelectionModel}, at the time of construction.
 * {@link ListModelSelection} instances are immutable.
 *
 * @param <E> the selected element type.
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
public class ListModelSelection<E> extends AbstractSelection<E> {

    private Selection<E> selection;

    public ListModelSelection(ListModel<E> listModel, ListSelectionModel listSelectionModel) {
        int[] selectedIndexes = new ListSelectionModelExt(listSelectionModel).getSelectedIndexes();

        List<E> selectedElements = getSelectedElements(listModel, selectedIndexes);

        selection = new DefaultSelection<>(selectedElements);
    }

    private List<E> getSelectedElements(ListModel<E> listModel, int[] selectedIndexes) {
        List<E> selectedElements = new ArrayList<>();

        for (int i = 0; i < selectedIndexes.length; i++) {
            int selectedIndex = selectedIndexes[i];
            if (selectedIndex < listModel.getSize()) {
                E selectedElement = listModel.getElementAt(selectedIndex);
                selectedElements.add(selectedElement);
            }
        }

        return selectedElements;
    }

    @Override
    public int size() {
        return selection.size();
    }

    @Override
    public List<E> toList() {
        return selection.toList();
    }
}