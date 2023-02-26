package com.link_intersystems.swing.selection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractSelectionTest {

    private List<String> stringSelection = new ArrayList<>();
    private Selection<String> selection;

    @BeforeEach
    void setUp() {
        selection = new AbstractSelection<String>() {
            @Override
            public int size() {
                return stringSelection.size();
            }

            @Override
            public List<String> toList() {
                return stringSelection;
            }
        };
    }

    @Test
    void isEmpty() {
        assertTrue(selection.isEmpty());

        stringSelection.add("A");

        assertFalse(selection.isEmpty());
    }

    @Test
    void getFirstElement() {
        assertNull(selection.getFirstElement());

        stringSelection.addAll(Arrays.asList("A", "B"));

        assertEquals("A", selection.getFirstElement());
    }

    @Test
    void iterator() {
        stringSelection.addAll(Arrays.asList("A", "B"));

        Iterator<String> iterator = selection.iterator();

        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void empty() {
        assertTrue(Selection.empty().isEmpty());
        assertNull(Selection.empty().getFirstElement());
    }

}