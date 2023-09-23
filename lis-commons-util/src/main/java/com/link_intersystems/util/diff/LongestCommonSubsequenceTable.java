package com.link_intersystems.util.diff;

import com.link_intersystems.util.table.IntTable;
import com.link_intersystems.util.Sequence;

import java.util.Objects;

import static java.util.Objects.*;

public class LongestCommonSubsequenceTable<E> implements IntTable {

    private final Sequence<E> rowSequence;
    private final Sequence<E> columnSequence;

    private int[][] table;
    private LongestCommonSubsequencePath LongestCommonSubsequencePath;

    public LongestCommonSubsequenceTable(Sequence<E> rowSequence, Sequence<E> columnSequence) {
        this.rowSequence = requireNonNull(rowSequence);
        this.columnSequence = requireNonNull(columnSequence);
    }

    public int[][] getTable() {
        if (table != null) {
            return table;
        }

        table = new int[rowSequence.length() + 1][columnSequence.length() + 1];

        for (int row = 1; row <= rowSequence.length(); row++) {
            for (int column = 1; column <= columnSequence.length(); column++) {
                if (Objects.equals(rowSequence.elementAt(row - 1), columnSequence.elementAt(column - 1))) {
                    table[row][column] = table[row - 1][column - 1] + 1;
                } else {
                    table[row][column] = Math.max(table[row - 1][column], table[row][column - 1]);
                }
            }
        }

        return table;
    }

    public int getValue(int row, int column) {
        return getTable()[row][column];
    }

    public Sequence<E> getLcs() {
        if (LongestCommonSubsequencePath != null) {
            return LongestCommonSubsequencePath;
        }

        return LongestCommonSubsequencePath = new LongestCommonSubsequencePath<>(this, getRowSequence(), getColumnSequence());
    }

    public Sequence<E> getRowSequence() {
        return rowSequence;
    }

    public Sequence<E> getColumnSequence() {
        return columnSequence;
    }

    @Override
    public int getColumnCount() {
        return columnSequence.length() + 1;
    }

    @Override
    public int getRowCount() {
        return rowSequence.length() + 1;
    }
}
