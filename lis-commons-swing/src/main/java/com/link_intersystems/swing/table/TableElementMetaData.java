package com.link_intersystems.swing.table;

public interface TableElementMetaData {

    public int getColumnCount();

    default public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public String getColumnName(int column);

}
