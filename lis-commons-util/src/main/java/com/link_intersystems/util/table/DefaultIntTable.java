package com.link_intersystems.util.table;

import static java.util.Objects.*;

public class DefaultIntTable implements IntTable {

    private final int[][] table;

    public DefaultIntTable(int rowSize, int columnSize) {
        this(new int[rowSize][columnSize]);
    }

    /**
     * @param table the 2-dimensional int array that represents this {@link IntTable}.
     * @throws IllegalArgumentException if rows do not have the same column length. E.g. <code>new DefaultIntTable(new int[][]{ {0, 1}, {2, 3, 4} });</code>
     */
    public DefaultIntTable(int[][] table) {
        this.table = requireNonNull(table);
        for (int row = 1; row < getRowCount(); row++) {
            if (table[row].length != table[row - 1].length) {
                throw new IllegalArgumentException("All rows must have the same column length, but row " + row + " differs from row " + (row - 1) + ": " + table[row].length + " != " + table[row - 1].length);
            }
        }
    }

    @Override
    public int getValue(int row, int column) {
        return table[row][column];
    }

    @Override
    public void setValue(int row, int column, int value) {
        table[row][column] = value;
    }

    @Override
    public int getColumnCount() {
        return getColumnCount(0);
    }

    public int getColumnCount(int row) {
        if (row < table.length) {
            return table[row].length;
        }

        return 0;
    }

    @Override
    public int getRowCount() {
        return table.length;
    }
}
