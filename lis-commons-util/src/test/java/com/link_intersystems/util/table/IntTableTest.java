package com.link_intersystems.util.table;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntTableTest {

    @Test
    void setValue() {
        assertThrows(UnsupportedOperationException.class, () -> new IntTable(){
            @Override
            public int getValue(int row, int column) {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public int getRowCount() {
                return 0;
            }
        }.setValue(0,0,0));
    }
}