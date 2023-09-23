package com.link_intersystems.util.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultIntTableTest {

    private DefaultIntTable intTable;

    @BeforeEach
    void setUp() {
        intTable = new DefaultIntTable(new int[][]{ //
                {0, 1, 2}, //
                {3, 4, 5}, //
        });
    }

    @Test
    void emptyTable() {
        intTable = new DefaultIntTable(0, 5);

        assertEquals(0, intTable.getRowCount());
        assertEquals(0, intTable.getColumnCount());
    }

    @Test
    void unequalColumnLength() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultIntTable(new int[][]{ //
                {0, 1, 2}, //
                {3, 4, 5, 6}, //
        }));
    }

    @Test
    void getValue() {
        assertEquals(0, intTable.getValue(0, 0));
        assertEquals(1, intTable.getValue(0, 1));
        assertEquals(2, intTable.getValue(0, 2));

        assertEquals(3, intTable.getValue(1, 0));
        assertEquals(4, intTable.getValue(1, 1));
        assertEquals(5, intTable.getValue(1, 2));
    }


    @Test
    void setValue() {
        intTable.setValue(1, 2, 42);

        assertEquals(0, intTable.getValue(0, 0));
        assertEquals(1, intTable.getValue(0, 1));
        assertEquals(2, intTable.getValue(0, 2));

        assertEquals(3, intTable.getValue(1, 0));
        assertEquals(4, intTable.getValue(1, 1));
        assertEquals(42, intTable.getValue(1, 2));
    }

    @Test
    void getColumnCount() {
        assertEquals(3, intTable.getColumnCount());
    }

    @Test
    void getRowCount() {
        assertEquals(2, intTable.getRowCount());
    }
}