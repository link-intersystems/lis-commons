package com.link_intersystems.swing.selection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSelectionTest {

    @Test
    void size() {
        DefaultSelection<String> listSelection = new DefaultSelection<>("A", "B");

        assertEquals(2, listSelection.size());
    }

    @Test
    void toList() {
        DefaultSelection<String> listSelection = new DefaultSelection<>("A", "B");

        assertEquals(Arrays.asList("A", "B"), listSelection.toList());
    }

}