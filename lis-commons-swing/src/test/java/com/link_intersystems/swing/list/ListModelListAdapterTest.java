package com.link_intersystems.swing.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ListModelListAdapterTest {

    private DefaultListModel<String> listModel;
    private ListModelListAdapter<String> listAdapter;

    @BeforeEach
    void setUp() {
        listModel = new DefaultListModel<>();
        listAdapter = new ListModelListAdapter<>(listModel);

        listModel.addElement("one");
        listModel.addElement("two");
        listModel.addElement("three");
    }

    @Test
    void getListModel() {
        assertEquals(listModel, listAdapter.getListModel());
    }

    @Test
    void get() {
        assertEquals("two", listAdapter.get(1));
    }

    @Test
    void getSize() {
        assertEquals(3, listAdapter.size());
    }
}