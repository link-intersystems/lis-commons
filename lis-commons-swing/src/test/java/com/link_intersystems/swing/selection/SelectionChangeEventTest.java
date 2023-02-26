package com.link_intersystems.swing.selection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectionChangeEventTest {

    @Test
    void getOldSelection() {
        Selection<String> selection = new DefaultSelection<>();
        assertEquals(selection, new SelectionChangeEvent<>(this, selection, Selection.empty()).getOldSelection());
    }

    @Test
    void getNewSelection() {
        Selection<String> selection = new DefaultSelection<>();
        assertEquals(selection, new SelectionChangeEvent<>(this, Selection.empty(), selection).getNewSelection());
    }
}