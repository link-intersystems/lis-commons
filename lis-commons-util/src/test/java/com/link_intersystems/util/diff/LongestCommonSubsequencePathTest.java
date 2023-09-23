package com.link_intersystems.util.diff;

import com.link_intersystems.util.CharacterSequence;
import com.link_intersystems.util.table.DefaultIntTable;
import com.link_intersystems.util.table.IntTable;
import com.link_intersystems.util.table.InvertedIntTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class LongestCommonSubsequencePathTest {


    private IntTable lcsTable;
    private CharacterSequence rowSequence;
    private CharacterSequence columnSequence;

    /**
     * <pre>
     * +-------+-------+-------+-------+-------+-------+
     * |       |       | C     | B     | D     | A     |
     * +-------+-------+-------+-------+-------+-------+
     * |       | 0     | 0     | 0     | 0     | 0     |
     * +-------+-------+-------+-------+-------+-------+
     * | A     | 0     | 0     | 0     | 0     | 1     |
     * +-------+-------+-------+-------+-------+-------+
     * | C     | 0     | 1     | 1     | 1     | 1     |
     * +-------+-------+-------+-------+-------+-------+
     * | A     | 0     | 1     | 1     | 1     | 2     |
     * +-------+-------+-------+-------+-------+-------+
     * | D     | 0     | 1     | 1     | 2     | 2     |
     * +-------+-------+-------+-------+-------+-------+
     * | B     | 0     | 1     | 2     | 2     | 2     |
     * +-------+-------+-------+-------+-------+-------+
     * </pre>
     */
    @BeforeEach
    void setUp() {
        int[][] table = new int[][]{ //
                {0, 0, 0, 0, 0}, //
                {0, 0, 0, 0, 1}, //
                {0, 1, 1, 1, 1}, //
                {0, 1, 1, 1, 2}, //
                {0, 1, 1, 2, 2}, //
                {0, 1, 2, 2, 2}, //
        };

        lcsTable = new DefaultIntTable(table);

        rowSequence = new CharacterSequence("ACADB");
        columnSequence = new CharacterSequence("CBDA");
    }

    /**
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
     */
    @Test
    void path() {

        LongestCommonSubsequencePath<Character> longestCommonSubsequencePath = new LongestCommonSubsequencePath<>(lcsTable, rowSequence, columnSequence);

        assertEquals(2, longestCommonSubsequencePath.length());

        assertEquals('C', longestCommonSubsequencePath.elementAt(0));
        assertEquals('A', longestCommonSubsequencePath.elementAt(1));
    }

    /**
     * <pre>
     * +-------+-------+-------+-------+-------+-------+-------+
     * |       |       | A     | C     | A     | D     | B     |
     * +-------+-------+-------+-------+-------+-------+-------+
     * |       | 0     | 0     | 0     | 0     | 0     | 0     |
     * +-------+-------+-------\-------+-------+-------+-------+
     * | C     | 0     | 0     |[1]   <- 1    <- 1     | 1     |
     * +-------+-------+-------+-------+-------+-------\-------+
     * | B     | 0     | 0     | 1     | 1     | 1     |[2]    |
     * +-------+-------+-------+-------+-------+-------+-^-----+
     * | D     | 0     | 0     | 1     | 1     | 2     | 2     |
     * +-------+-------+-------+-------+-------+-------+-^-----+
     * | A     | 0     | 1     | 1     | 2     | 2     | 2     |
     * +-------+-------+-------+-------+-------+-------+-------+
     * </pre>
     */
    @Test
    void pathOfInvertedTable() {
        IntTable invertedTable = new InvertedIntTable(lcsTable);
        LongestCommonSubsequencePath<Character> longestCommonSubsequencePath = new LongestCommonSubsequencePath<>(invertedTable, columnSequence, rowSequence);

        assertEquals(2, longestCommonSubsequencePath.length());

        assertEquals('C', longestCommonSubsequencePath.elementAt(0));
        assertEquals('B', longestCommonSubsequencePath.elementAt(1));
    }

}