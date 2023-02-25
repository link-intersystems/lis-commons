package com.link_intersystems.swing.table;

public class DefaultTableElementMetaData<E> implements TableElementMetaData {

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return "A";
    }

}
