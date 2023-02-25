package com.link_intersystems.swing.table;

@FunctionalInterface
public interface TableElementCell<E> {
    Object getValue(E element, int columnIndex);
}
