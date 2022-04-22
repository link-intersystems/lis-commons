package com.link_intersystems.events;

import com.link_intersystems.events.swing.ListSelectionEventMethod;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class EventMethodUsageExample {

    public static void main(String[] args) {
        EventMethodUsageExample eventMethodUsageExample = new EventMethodUsageExample();
        eventMethodUsageExample.setSelection(3);
        eventMethodUsageExample.setSelection(5);
    }

    private ListSelectionListener l1 = ListSelectionEventMethod.VALUE_CHANGED.listener(this::printEventFired);

    private ListSelectionListener l2 = ListSelectionEventMethod.VALUE_CHANGED.listener(
            this::printSelectionChanged,
            ListSelectionEvent::getLastIndex,
            ">>> ", e -> !e.getValueIsAdjusting());

    private ListSelectionModel selectionModel = new DefaultListSelectionModel();

    EventMethodUsageExample() {
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(l1);
        selectionModel.addListSelectionListener(l2);
    }

    public void setSelection(int index) {
        selectionModel.setValueIsAdjusting(true);
        selectionModel.setSelectionInterval(index, index);
        selectionModel.setValueIsAdjusting(false);
    }

    private void printEventFired(ListSelectionEvent e) {
        int firstIndex = e.getFirstIndex();
        int lastIndex = e.getLastIndex();
        boolean isAdjusting = e.getValueIsAdjusting();
        System.out.println("Selection model event fired:" +
                " firstIndex= " + firstIndex +
                " lastIndex= " + lastIndex +
                " isAdjusting= " + isAdjusting);
    }

    private void printSelectionChanged(int selectedIndex, String indent) {
        System.out.println(indent + "Index " + selectedIndex + " selected");
    }
}
