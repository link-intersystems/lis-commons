package com.link_intersystems.swing.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static com.link_intersystems.beans.mockito.BeanMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListTableModelTest {

    private DefaultListModel<String> listModel;
    private DefaultListTableModel<String> listTableModel;
    private DefaultListTableDescriptorModel<String> listTableDescriptorModel;

    @BeforeEach
    void setUp() {
        listModel = new DefaultListModel<>();
        listTableModel = new DefaultListTableModel<>();

        listTableDescriptorModel = new DefaultListTableDescriptorModel<>();
        listTableDescriptorModel.addColumnDescriptor("A");
        listTableModel.setListTableDescriptorModel(listTableDescriptorModel);

        listModel.addElement("one");
        listModel.addElement("two");
        listModel.addElement("three");

        listTableModel.setListModel(listModel);
    }

    @Test
    void getRowCount() {
        assertEquals(listModel.getSize(), listTableModel.getRowCount());
    }

    @Test
    void getColumnCount() {
        assertEquals(1, listTableModel.getColumnCount());
    }

    @Test
    void getColumnClass() {
        assertEquals(Object.class, listTableModel.getColumnClass(0));
    }

    @Test
    void getColumnName() {
        assertEquals("A", listTableModel.getColumnName(0));
    }

    @Test
    void getValueAt() {
        assertEquals("one", listTableModel.getValueAt(0, 0));
        assertEquals("two", listTableModel.getValueAt(1, 0));
        assertEquals("three", listTableModel.getValueAt(2, 0));
    }

    @Test
    void rowsInserted() {
        TableModelListener tableModelListener = mock(TableModelListener.class);
        listTableModel.addTableModelListener(tableModelListener);

        listModel.addElement("four");

        TableModelEvent expectedEvent = new TableModelEvent(listTableModel, 3, 3, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        verify(tableModelListener).tableChanged(propertiesEqual(expectedEvent));
    }

    @Test
    void rowsRemoved() {
        TableModelListener tableModelListener = mock(TableModelListener.class);
        listTableModel.addTableModelListener(tableModelListener);

        listModel.removeRange(0, 1);

        TableModelEvent expectedEvent = new TableModelEvent(listTableModel, 0, 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        verify(tableModelListener).tableChanged(propertiesEqual(expectedEvent));
    }

    @Test
    void dataChanged() {
        TableModelListener tableModelListener = mock(TableModelListener.class);
        listTableModel.addTableModelListener(tableModelListener);

        listModel.set(0, "ONE");

        TableModelEvent expectedEvent = new TableModelEvent(listTableModel);
        verify(tableModelListener).tableChanged(propertiesEqual(expectedEvent));
    }


    @Test
    void tableElementMetaDataAndCell() {
        listTableDescriptorModel.removeColumnDescriptor("A");
        listTableDescriptorModel.addColumnDescriptor("first Letter", s -> Character.toString(s.charAt(0)));
        listTableDescriptorModel.addColumnDescriptor("value");

        assertEquals("o", listTableModel.getValueAt(0, 0));
        assertEquals("t", listTableModel.getValueAt(1, 0));
        assertEquals("t", listTableModel.getValueAt(2, 0));

        assertEquals("one", listTableModel.getValueAt(0, 1));
        assertEquals("two", listTableModel.getValueAt(1, 1));
        assertEquals("three", listTableModel.getValueAt(2, 1));

        assertEquals("first Letter", listTableModel.getColumnName(0));
        assertEquals("value", listTableModel.getColumnName(1));
    }
}