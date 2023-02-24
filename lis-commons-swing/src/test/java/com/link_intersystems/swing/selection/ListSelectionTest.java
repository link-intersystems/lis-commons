package com.link_intersystems.swing.selection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ListSelectionTest {

    private ListSelection<String> listSelection;

    @BeforeEach
    void setUP() {
        listSelection = new ListSelection<>();
    }

    @Test
    void add() {
        listSelection.add("A");

        assertEquals("A", listSelection.getFirstElement());
    }

    @Test
    void remove() {
        listSelection.addAll(Arrays.asList("A", "B"));
        listSelection.remove("A");

        assertEquals("B", listSelection.getFirstElement());
    }

    @Test
    void set() {
        listSelection.addAll(Arrays.asList("A", "B"));
        listSelection.set(0, "C");

        assertEquals("C", listSelection.getFirstElement());
    }

    @Test
    void size() {
        listSelection.addAll(Arrays.asList("A", "B"));

        assertEquals(2, listSelection.size());
    }

    @Test
    void toList() {
        listSelection.addAll(Arrays.asList("A", "B"));

        assertEquals(Arrays.asList("A", "B"), listSelection.toList());
    }

}