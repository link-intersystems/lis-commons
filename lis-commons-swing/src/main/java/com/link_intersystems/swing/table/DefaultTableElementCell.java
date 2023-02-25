package com.link_intersystems.swing.table;

public class DefaultTableElementCell<E> implements TableElementCell<E> {
    @Override
    public Object getValue(E element, int columnIndex) {
        return String.valueOf(element);
    }
}
