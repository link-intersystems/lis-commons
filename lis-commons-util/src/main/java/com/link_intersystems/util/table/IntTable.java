package com.link_intersystems.util.table;

public interface IntTable {
    int getValue(int row, int column);

    /**
     * Sets the value at the specified cell.
     *
     * @param row
     * @param column
     * @param value
     * @return
     * @throws UnsupportedOperationException if this {@link IntTable} can not be modified;
     */
    default void setValue(int row, int column, int value) {
        throw new UnsupportedOperationException("This " + IntTable.class.getSimpleName() + " is immutable");
    }

    int getColumnCount();

    int getRowCount();

}
