package com.link_intersystems.util.diff;

import com.link_intersystems.util.CharacterSequence;
import com.link_intersystems.util.Sequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestCommonSubsequenceTableTest {

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
    @Test
    void lcsTableContent() {
        CharacterSequence rowSequence = new CharacterSequence("ACADB");
        CharacterSequence columnSequence = new CharacterSequence("CBDA");
        LongestCommonSubsequenceTable<Character> table = new LongestCommonSubsequenceTable<>(rowSequence, columnSequence);

        assertFirstRowHasOnlyZeros(table);
        assertFirstColumnHasOnlyZeros(table);

        assertEquals(0, table.getValue(1, 1)); // compare A to C
        assertEquals(0, table.getValue(1, 2)); // compare A to B
        assertEquals(0, table.getValue(1, 3)); // compare A to D
        assertEquals(1, table.getValue(1, 4)); // compare A to A

        assertEquals(1, table.getValue(2, 1)); // compare C to C
        assertEquals(1, table.getValue(2, 2)); // compare C to B
        assertEquals(1, table.getValue(2, 3)); // compare C to D
        assertEquals(1, table.getValue(2, 4)); // compare C to A

        assertEquals(1, table.getValue(3, 1)); // compare A to C
        assertEquals(1, table.getValue(3, 2)); // compare A to B
        assertEquals(1, table.getValue(3, 3)); // compare A to D
        assertEquals(2, table.getValue(3, 4)); // compare A to A

        assertEquals(1, table.getValue(4, 1)); // compare D to C
        assertEquals(1, table.getValue(4, 2)); // compare D to B
        assertEquals(2, table.getValue(4, 3)); // compare D to D
        assertEquals(2, table.getValue(4, 4)); // compare D to A

        assertEquals(1, table.getValue(5, 1)); // compare B to C
        assertEquals(2, table.getValue(5, 2)); // compare B to B
        assertEquals(2, table.getValue(5, 3)); // compare B to D
        assertEquals(2, table.getValue(5, 4)); // compare B to A
    }

    private void assertFirstColumnHasOnlyZeros(LongestCommonSubsequenceTable<Character> table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            assertEquals(0, table.getValue(row, 0));
        }
    }

    private void assertFirstRowHasOnlyZeros(LongestCommonSubsequenceTable<Character> table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            assertEquals(0, table.getValue(0, column));
        }
    }

    @Test
    void lcsCached() {
        CharacterSequence rowSequence = new CharacterSequence("ACADB");
        CharacterSequence columnSequence = new CharacterSequence("CBDA");
        LongestCommonSubsequenceTable<Character> table = new LongestCommonSubsequenceTable<>(rowSequence, columnSequence);
        Sequence<Character> lcs = table.getLcs();


        assertSame(lcs, table.getLcs());
    }

}