package com.link_intersystems.swing.list;

import com.link_intersystems.test.EqualsAndHashCodeTest;

import javax.swing.*;

class ListModelSelectionEqualsAndHashCodeBySelectionModelDiffTest extends EqualsAndHashCodeTest {

    private DefaultListModel<String> instance;


    @Override
    protected Object createInstance() throws Exception {
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionInterval(1, 2);
        return new ListModelSelection<>(getListModel(), selectionModel);
    }

    private ListModel<String> getListModel() {
        if (instance == null) {
            instance = new DefaultListModel<>();
            instance.addElement("A");
            instance.addElement("B");
            instance.addElement("C");
            instance.addElement("D");
        }
        return instance;
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionInterval(2, 3);
        return new ListModelSelection<>(getListModel(), selectionModel);
    }
}