package com.link_intersystems.util.table;

import static java.util.Objects.*;

public class InvertedIntTable implements IntTable {

    private IntTable intTable;

    public InvertedIntTable(IntTable intTable) {
        this.intTable = requireNonNull(intTable);
    }

    @Override
    public int getValue(int row, int column) {
        return intTable.getValue(column, row);
    }

    @Override
    public int getColumnCount() {
        return intTable.getRowCount();
    }

    @Override
    public int getRowCount() {
        return intTable.getColumnCount();
    }
};