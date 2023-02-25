package com.link_intersystems.swing.list;

import com.link_intersystems.test.EqualsAndHashCodeTest;

import javax.swing.*;

class ListModelSelectionEqualsAndHashCodeTest extends EqualsAndHashCodeTest {

    private ListModel<String> instance;


    @Override
    protected Object createInstance() throws Exception {
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        if (instance == null) {
            instance = new DefaultListModel<>();
        }
        return new ListModelSelection<>(instance, selectionModel);
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        ListModel<String> listModel = new DefaultListModel<>();
        return new ListModelSelection<>(listModel, selectionModel);
    }
}