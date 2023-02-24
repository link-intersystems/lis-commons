package com.link_intersystems.swing.selection;

import java.util.EventListener;

public interface SelectionListener<E> extends EventListener {

    public void selectionChanged(SelectionChangeEvent<E> event);
}
