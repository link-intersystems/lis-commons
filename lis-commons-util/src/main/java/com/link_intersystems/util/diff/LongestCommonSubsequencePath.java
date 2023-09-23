package com.link_intersystems.util.diff;

import com.link_intersystems.util.table.IntTable;
import com.link_intersystems.util.Sequence;

import java.util.Objects;

/**
 * The path of the longest common subsequence of a {@link LongestCommonSubsequenceTable}.
 *
 * <p>
 * For example the lcs for a seq1 = <code>ACADB</code> and seq2 = <code>CBDA</code> is <code>CA</code>
 * </p>
 * <pre>
 * +-------+-------+-------+-------+-------+-------+
 * |       |       | C     | B     | D     | A     |
 * +-------+-------+-------+-------+-------+-------+
 * |       | 0     | 0     | 0     | 0     | 0     |
 * +-------+-------+-------+-------+-------+-------+
 * | A     | 0     | 0     | 0     | 0     | 1     |
 * +-------+-------\-------+-------+-------+-------+
 * | C     | 0     |[1]   <- 1    <- 1     | 1     |
 * +-------+-------+-------+-------+-------\-------+
 * | A     | 0     | 1     | 1     | 1     |[2]   |
 * +-------+-------+-------+-------+-------+-^-----+
 * | D     | 0     | 1     | 1     | 2     | 2     |
 * +-------+-------+-------+-------+-------+-^-----+
 * | B     | 0     | 1     | 2     | 2     | 2     |
 * +-------+-------+-------+-------+-------+-------+
 * </pre>
 *
 * @param <E>
 */
public class LongestCommonSubsequencePath<E> implements Sequence<E> {


    private final IntTable lcsTable;
    private final Sequence<E> rowSequence;
    private final Sequence<E> columnSequence;

    private int[][] path;

    LongestCommonSubsequencePath(IntTable lcsTable, Sequence<E> rowSequence, Sequence<E> columnSequence) {
        this.lcsTable = lcsTable;
        this.rowSequence = rowSequence;
        this.columnSequence = columnSequence;
    }

    public int[][] getPath() {
        if (path != null) {
            return path;
        }

        int[][] path = new int[Math.max(rowSequence.length(), columnSequence.length())][2];
        int pathIndex = path.length - 1;

        int row = rowSequence.length(), column = columnSequence.length();
        while (row > 0 && column > 0) {
            E elementAtRowAbove = rowSequence.elementAt(row - 1);
            E elementAtColumnToLeft = columnSequence.elementAt(column - 1);

            if (Objects.equals(elementAtRowAbove, elementAtColumnToLeft)) {

                path[pathIndex][0] = row - 1;
                path[pathIndex][1] = column - 1;
                pathIndex--;

                row--;
                column--;
            } else if (lcsTable.getValue(row - 1, column) < lcsTable.getValue(row, column - 1)) {
                column--;
            } else {
                row--;
            }
        }

        this.path = new int[path.length - pathIndex - 1][2];
        System.arraycopy(path, pathIndex + 1, this.path, 0, this.path.length);
        return this.path;
    }

    @Override
    public E elementAt(int index) {
        return rowSequence.elementAt(getPath()[index][0]);
    }

    @Override
    public int length() {
        return getPath().length;
    }
}
