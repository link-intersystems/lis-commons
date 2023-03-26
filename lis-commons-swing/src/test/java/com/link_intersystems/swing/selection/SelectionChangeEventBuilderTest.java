package com.link_intersystems.swing.selection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectionChangeEventBuilderTest {

    private SelectionChangeEventBuilder<Object> eventBuilder;

    @BeforeEach
    void setUp() {
        eventBuilder = new SelectionChangeEventBuilder<>(this);
    }

    @Test
    void newElement() {
        SelectionChangeEvent<Object> event = eventBuilder.withNewElements("new", "new1").build();

        assertEquals(this, event.getSource());
        assertEquals("new", event.getNewSelection().getFirstElement());
        assertTrue(event.getOldSelection().isEmpty());
    }

    @Test
    void oldAndNewElement() {
        SelectionChangeEvent<Object> event = eventBuilder.withNewElements("new", "new1").withOldElements("old").build();

        assertEquals(this, event.getSource());
        assertEquals("new", event.getNewSelection().getFirstElement());
        assertEquals("old", event.getOldSelection().getFirstElement());
    }

    @Test
    void useMultipleTimes() {
       eventBuilder.withNewElements("new", "new1").withOldElements("old").build();
        SelectionChangeEvent<Object> event = eventBuilder.withNewElements("new2", "new3").build();

        assertEquals(this, event.getSource());
        assertEquals("new2", event.getNewSelection().getFirstElement());
        assertTrue(event.getOldSelection().isEmpty());
    }

}