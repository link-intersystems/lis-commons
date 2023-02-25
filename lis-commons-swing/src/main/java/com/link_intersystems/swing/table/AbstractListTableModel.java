package com.link_intersystems.swing.table;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

import static java.util.Objects.*;

public abstract class AbstractListTableModel<E> extends AbstractTableModel {

    private ListDataListener listDataListener = new ListDataListener() {
        @Override
        public void intervalAdded(ListDataEvent e) {
            fireTableRowsInserted(e.getIndex0(), e.getIndex1());
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            fireTableRowsDeleted(e.getIndex0(), e.getIndex1());
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            fireTableDataChanged();
        }
    };

    private ListModel<E> listModel = new DefaultListModel<>();

    public void setListModel(ListModel<E> listModel) {
        requireNonNull(listModel);

        this.listModel.removeListDataListener(listDataListener);
        this.listModel = listModel;
        this.listModel.addListDataListener(listDataListener);

        fireTableStructureChanged();
    }

    public ListModel<E> getListModel() {
        return listModel;
    }

    @Override
    public int getRowCount() {
        return listModel.getSize();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        E elementAt = getListModel().getElementAt(rowIndex);
        return getValue(elementAt, columnIndex);
    }

    protected abstract Object getValue(E element, int columnIndex);

}
