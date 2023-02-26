package com.link_intersystems.swing.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ListModelSelectionTest {

    private DefaultListSelectionModel listSelectionModel;
    private DefaultListModel<String> listModel;

    @BeforeEach
    void setUp() {
        listModel = new DefaultListModel<>();
        listSelectionModel = new DefaultListSelectionModel();

        listModel.addElement("one");
        listModel.addElement("two");
        listModel.addElement("three");
        listModel.addElement("four");
    }

    @Test
    void getFirstElement() {
        listSelectionModel.setSelectionInterval(2, 2);

        assertEquals("three", new ListModelSelection<>(listModel, listSelectionModel).getFirstElement());
    }

    @Test
    void toList() {
        listSelectionModel.setSelectionInterval(0, 0);
        listSelectionModel.addSelectionInterval(2, 3);

        assertEquals(Arrays.asList("one", "three", "four"), new ListModelSelection<>(listModel, listSelectionModel).toList());
    }

    @Test
    void size() {
        listSelectionModel.setSelectionInterval(0, 0);
        listSelectionModel.addSelectionInterval(2, 3);

        assertEquals(3, new ListModelSelection<>(listModel, listSelectionModel).size());
    }

    @Test
    void isEmpty() {
        listSelectionModel.setSelectionInterval(1, 3);

        assertFalse(new ListModelSelection<>(listModel, listSelectionModel).isEmpty());
    }
}