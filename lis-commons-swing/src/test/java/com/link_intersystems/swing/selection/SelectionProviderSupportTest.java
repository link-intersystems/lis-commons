package com.link_intersystems.swing.selection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SelectionProviderSupportTest {

    private SelectionProviderSupport<CharSequence> selectionProviderSupport;
    private SelectionListener selectionListener;
    private Selection<CharSequence> selection;

    @BeforeEach
    void setUp() {
        selectionProviderSupport = new SelectionProviderSupport(this);
        selectionListener = mock(SelectionListener.class);
        selection = mock(Selection.class);
    }

    @Test
    void setSelection() {
        selectionProviderSupport.setSelection(selection);

        assertEquals(selection, selectionProviderSupport.getSelection());
    }

    @Test
    void addSelectionChangedListener() {
        selectionProviderSupport.addSelectionChangedListener(selectionListener);

        selectionProviderSupport.setSelection(selection);

        ArgumentCaptor<SelectionChangeEvent> eventArgumentCaptor = ArgumentCaptor.forClass(SelectionChangeEvent.class);
        verify(selectionListener).selectionChanged(eventArgumentCaptor.capture());

        SelectionChangeEvent selectionChangeEvent = eventArgumentCaptor.getValue();

        assertEquals(this, selectionChangeEvent.getSource());
        assertEquals(selection, selectionChangeEvent.getNewSelection());
    }

    @Test
    void removeSelectionChangedListener() {
        selectionProviderSupport.addSelectionChangedListener(selectionListener);
        selectionProviderSupport.removeSelectionChangedListener(selectionListener);

        selectionProviderSupport.setSelection(selection);

        verify(selectionListener, never()).selectionChanged(refEq(new SelectionChangeEvent(this, Selection.empty(), selection)));
    }
}