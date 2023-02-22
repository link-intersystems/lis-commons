package com.link_intersystems.swing.selection;

import com.link_intersystems.util.adapter.Adaptable;

import javax.swing.event.ChangeListener;

public interface Selection extends Adaptable {

    public void addChangeListener(ChangeListener changeListener);

    public void removeChangeListener(ChangeListener changeListener);

    public boolean isEmpty();

}
