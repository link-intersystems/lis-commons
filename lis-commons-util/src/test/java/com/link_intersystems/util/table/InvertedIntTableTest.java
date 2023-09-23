package com.link_intersystems.util.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvertedIntTableTest {

    private InvertedIntTable invertedIntTable;
    private DefaultIntTable intTable;

    @BeforeEach
    void setUp() {
        intTable = new DefaultIntTable(new int[][]{ //
                {1, 2}, //
                {3, 4}, //
                {5, 6}, //
        });
        invertedIntTable = new InvertedIntTable(intTable);
    }

    @Test
    void getValue() {

        assertEquals(1, invertedIntTable.getValue(0, 0));
        assertEquals(3, invertedIntTable.getValue(0, 1));
        assertEquals(5, invertedIntTable.getValue(0, 2));

        assertEquals(2, invertedIntTable.getValue(1, 0));
        assertEquals(4, invertedIntTable.getValue(1, 1));
        assertEquals(6, invertedIntTable.getValue(1, 2));
    }

    @Test
    void getColumnCount() {
        assertEquals(3, invertedIntTable.getColumnCount());
    }

    @Test
    void getRowCount() {
        assertEquals(2, invertedIntTable.getRowCount());
    }
}