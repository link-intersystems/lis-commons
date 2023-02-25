package com.link_intersystems.swing.list;

import javax.swing.*;
import java.util.Arrays;

import static java.util.Objects.*;

public class ListSelectionModelExt {

    private ListSelectionModel listSelectionModel;

    public ListSelectionModelExt(ListSelectionModel listSelectionModel) {
        this.listSelectionModel = requireNonNull(listSelectionModel);
    }

    public ListSelectionModel getListSelectionModel() {
        return listSelectionModel;
    }

    public int getSelectedIndexCount() {
        int minSelectionIndex = listSelectionModel.getMinSelectionIndex();
        int maxSelectionIndex = listSelectionModel.getMaxSelectionIndex();

        int count = 0;

        for (int i = minSelectionIndex; i <= maxSelectionIndex; i++) {
            if (listSelectionModel.isSelectedIndex(i)) {
                count++;
            }
        }

        return count;
    }

    public int[] getSelectedIndexes() {
        int minSelectionIndex = listSelectionModel.getMinSelectionIndex();
        int maxSelectionIndex = listSelectionModel.getMaxSelectionIndex();


        int[] selectedIndexes = new int[maxSelectionIndex - minSelectionIndex + 1];
        int nextIndex = 0;

        for (int i = minSelectionIndex; i <= maxSelectionIndex; i++) {
            if (listSelectionModel.isSelectedIndex(i)) {
                selectedIndexes[nextIndex++] = i;
            }
        }


        int[] result = selectedIndexes;

        if (selectedIndexes.length != nextIndex) {
            result = new int[nextIndex];
            System.arraycopy(selectedIndexes, 0, result, 0, result.length);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSelectionModelExt that = (ListSelectionModelExt) o;

        return Arrays.equals(getSelectedIndexes(), that.getSelectedIndexes());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getSelectedIndexes());
    }
}
