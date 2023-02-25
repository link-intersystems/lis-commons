package com.link_intersystems.swing.table;

import static java.util.Objects.*;

public class DefaultListTableModel<E> extends AbstractListTableModel<E> {

    private TableElementMetaData tableElementMetaData = new DefaultTableElementMetaData();
    private TableElementCell tableElementCell = new DefaultTableElementCell<>();

    public void setTableElementMetaData(TableElementMetaData tableElementMetaData) {
        this.tableElementMetaData = requireNonNull(tableElementMetaData);
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    public void setTableElementCell(TableElementCell tableElementCell) {
        this.tableElementCell = requireNonNull(tableElementCell);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return tableElementMetaData.getColumnCount();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return tableElementMetaData.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return tableElementMetaData.getColumnName(column);
    }

    @Override
    protected Object getValue(E element, int columnIndex) {
        return tableElementCell.getValue(element, columnIndex);
    }
}
