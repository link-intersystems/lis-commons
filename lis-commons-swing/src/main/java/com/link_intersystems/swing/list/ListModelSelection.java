package com.link_intersystems.swing.list;

import com.link_intersystems.swing.function.EventQueueExecutor;
import com.link_intersystems.swing.selection.AbstractSelection;
import com.link_intersystems.swing.selection.StructuredSelection;
import com.link_intersystems.util.concurrent.DebouncedExecutor;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.*;

/**
 * A {@link ListModelSelection} represents the selected element of an
 * {@link ListModel}. It brings a {@link ListModel} and
 * {@link ListSelectionModel} together.
 *
 * @param <E>
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
public class ListModelSelection<E> extends AbstractSelection implements StructuredSelection<E> {


    private class ListSelectionAdapter implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            fireChanged();
        }
    }

    private class ListModelAdapter implements ListDataListener {

        @Override
        public void intervalAdded(ListDataEvent e) {
            fireChanged();
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            fireChanged();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            fireChanged();
        }

    }

    @Override
    protected void fireChanged() {
        Runnable r = () -> super.fireChanged();
        debouncedExecutor.execute(r);
    }

    private DebouncedExecutor debouncedExecutor = new DebouncedExecutor(EventQueueExecutor.INSTANCE);

    private ListModel<E> listModel = new DefaultListModel<>();

    private ListSelectionModelExt listSelectionModelExt = new ListSelectionModelExt(new DefaultListSelectionModel());

    private ListSelectionAdapter listSelectionAdapter = new ListSelectionAdapter();
    private ListModelAdapter listModelAdapter = new ListModelAdapter();

    public ListModelSelection() {
        this(new DefaultListModel<>());
    }

    public ListModelSelection(ListModel<E> listModel) {
        this(listModel, new DefaultListSelectionModel());
    }

    public ListModelSelection(ListModel<E> listModel, ListSelectionModel listSelectionModel) {
        setDebounceDelay(0, TimeUnit.MILLISECONDS);

        setListModel(listModel);
        setSelectionModel(listSelectionModel);
    }

    /**
     * The debounce delay time to wait before propagating change events that are triggered by {@link ListModel} changes.
     * Setting the delay to 0 will cause {@link ListModel} changes to be immediately reflected by change events of this {@link ListModelSelection}, which is the default.
     *
     * @param time
     * @param timeUnit
     */
    public void setDebounceDelay(long time, TimeUnit timeUnit) {
        debouncedExecutor.setDelay(time, timeUnit);
    }

    public void setListModel(ListModel<E> listModel) {
        if (Objects.equals(this.listModel, requireNonNull(listModel))) {
            return;
        }

        this.listModel.removeListDataListener(listModelAdapter);
        this.listModel = listModel;
        this.listModel.addListDataListener(listModelAdapter);

        fireChanged();
    }

    public ListModel<E> getListModel() {
        return listModel;
    }

    public ListSelectionModel getListSelectionModel() {
        return listSelectionModelExt.getListSelectionModel();
    }

    public void setSelectionModel(ListSelectionModel listSelectionModel) {
        if (Objects.equals(listSelectionModelExt.getListSelectionModel(), requireNonNull(listSelectionModel))) {
            return;
        }

        listSelectionModelExt.getListSelectionModel().removeListSelectionListener(listSelectionAdapter);

        listSelectionModelExt = new ListSelectionModelExt(listSelectionModel);

        listSelectionModelExt.getListSelectionModel().addListSelectionListener(listSelectionAdapter);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return listSelectionModelExt.getSelectedIndexCount();
    }

    @Override
    public List<E> toList() {
        List<E> elementSelection = new ArrayList<>();

        int[] selectedIndexes = listSelectionModelExt.getSelectedIndexes();

        for (int i = 0; i < selectedIndexes.length; i++) {
            int selectedIndex = selectedIndexes[i];
            if (selectedIndex < listModel.getSize()) {
                E elementAt = listModel.getElementAt(selectedIndex);
                elementSelection.add(elementAt);
            }
        }

        return elementSelection;
    }


}