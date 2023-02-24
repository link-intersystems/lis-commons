package com.link_intersystems.swing.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ListModelSelectionTest {

    private ListModelSelection<String> listModelSelection;
    private DefaultListSelectionModel listSelectionModel;
    private DefaultListModel<String> listModel;

    @BeforeEach
    void setUp() {
        listModel = new DefaultListModel<>();
        listSelectionModel = new DefaultListSelectionModel();
        listModelSelection = new ListModelSelection<>(listModel, listSelectionModel);

        listModel.addElement("one");
        listModel.addElement("two");
        listModel.addElement("three");
        listModel.addElement("four");
    }


    @Test
    void defaultModels() {
        ListModelSelection<Object> modelSelection = new ListModelSelection<>();
        assertNotNull(modelSelection.getListModel());
        assertNotNull(modelSelection.getListSelectionModel());
    }


    @Test
    void getListModel() {
        assertEquals(listModel, listModelSelection.getListModel());
    }

    @Test
    void getListSelectionModel() {
        assertEquals(listSelectionModel, listModelSelection.getListSelectionModel());
    }

    @Test
    void getFirstElement() {
        listSelectionModel.setSelectionInterval(2, 2);

        assertEquals("three", listModelSelection.getFirstElement());
    }

    @Test
    void toList() {
        listSelectionModel.setSelectionInterval(0, 0);
        listSelectionModel.addSelectionInterval(2, 3);

        assertEquals(Arrays.asList("one", "three", "four"), listModelSelection.toList());
    }

    @Test
    void size() {
        listSelectionModel.setSelectionInterval(0, 0);
        listSelectionModel.addSelectionInterval(2, 3);

        assertEquals(3, listModelSelection.size());
    }

    @Test
    void isEmpty() {
        listSelectionModel.setSelectionInterval(1, 3);

        assertFalse(listModelSelection.isEmpty());
    }

}