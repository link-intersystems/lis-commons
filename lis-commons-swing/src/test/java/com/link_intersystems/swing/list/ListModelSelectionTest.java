package com.link_intersystems.swing.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void changeListenerOnSelectionModelChange() {
        ChangeListener changeListener = mock(ChangeListener.class);
        listModelSelection.addChangeListener(changeListener);

        listSelectionModel.setSelectionInterval(1, 3);

        verify(changeListener).stateChanged(refEq(new ChangeEvent(listModelSelection)));
    }

    @Test
    void changeListenerOnListModelChange() throws InterruptedException {
        listModelSelection.setDebounceDelay(20, TimeUnit.MILLISECONDS);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                countDownLatch.countDown();
                System.out.println("stateChanged");
            }
        };

        listModelSelection.addChangeListener(changeListener);


        listModel.addElement("Test");
        listModel.removeElement("Test");
        listModel.clear();

        countDownLatch.await(100, TimeUnit.MILLISECONDS);

        assertEquals(0, countDownLatch.getCount(), "countDownLatch");
    }
}