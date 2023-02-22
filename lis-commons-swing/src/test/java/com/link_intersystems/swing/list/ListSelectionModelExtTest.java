package com.link_intersystems.swing.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ListSelectionModelExtTest {

    private DefaultListSelectionModel listSelectionModel;
    private ListSelectionModelExt listSelectionModelExt;

    @BeforeEach
    void setUp() {
        listSelectionModel = new DefaultListSelectionModel();
        listSelectionModelExt = new ListSelectionModelExt(listSelectionModel);
    }

    @Test
    void getSelectedIndexCount() {
        listSelectionModel.addSelectionInterval(2, 5);
        listSelectionModel.addSelectionInterval(8, 10);

        assertEquals(7, listSelectionModelExt.getSelectedIndexCount());
    }

    @Test
    void getSelectedIndexes() {
        listSelectionModel.setSelectionInterval(2, 10);

        assertArrayEquals(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10}, listSelectionModelExt.getSelectedIndexes());
    }

    @Test
    void getSelectedIndexesIntervals() {
        listSelectionModel.addSelectionInterval(2, 5);
        listSelectionModel.addSelectionInterval(8, 10);

        assertArrayEquals(new int[]{2, 3, 4, 5, 8, 9, 10}, listSelectionModelExt.getSelectedIndexes());
    }
}