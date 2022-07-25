package com.link_intersystems.beans.java;

import com.link_intersystems.beans.IndexedProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AbstractPropertyChangeSourceTest extends AbstractPropertyChangeSource {

    private PropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setup() {
        propertyChangeListener = mock(PropertyChangeListener.class);
        addPropertyChangeListener(propertyChangeListener);
    }

    @AfterEach
    void tearDown() {
        removePropertyChangeListener(propertyChangeListener);
    }

    @Test
    void removeChangeListeners() {
        removePropertyChangeListeners();
        firePropertyChange("intProperty", 0, 1);

        verify(propertyChangeListener, never()).propertyChange(Mockito.any());
    }

    @Test
    void fireIntPropertyChange() {
        firePropertyChange("intProperty", 0, 1);

        verifyPropertyChanged("intProperty", 0, 1);
    }

    private void verifyPropertyChanged(String propertyName, Object oldValue, Object newValue) {
        ArgumentCaptor<PropertyChangeEvent> changeEventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(propertyChangeListener, times(1)).propertyChange(changeEventCaptor.capture());

        PropertyChangeEvent event = changeEventCaptor.getValue();
        assertEquals(this, event.getSource());
        assertEquals(propertyName, event.getPropertyName());
        assertEquals(oldValue, event.getOldValue());
        assertEquals(newValue, event.getNewValue());
    }

    @Test
    void fireBooleanPropertyChange() {
        firePropertyChange("booleanProperty", false, true);

        verifyPropertyChanged("booleanProperty", false, true);
    }

    @Test
    void fireObjectPropertyChange() {
        firePropertyChange("stringProperty", "old", "new");

        verifyPropertyChanged("stringProperty", "old", "new");
    }

    @Test
    void fireIndexedObjectPropertyChange() {
        fireIndexedPropertyChange("indexedObjectProperty", 1, "old", "new");

        verifyIndexedPropertyChanged("indexedObjectProperty", 1, "old", "new");
    }

    private void verifyIndexedPropertyChanged(String propertyName, int index, Object oldValue, Object newValue) {
        ArgumentCaptor<IndexedPropertyChangeEvent> changeEventCaptor = ArgumentCaptor.forClass(IndexedPropertyChangeEvent.class);
        verify(propertyChangeListener, times(1)).propertyChange(changeEventCaptor.capture());

        IndexedPropertyChangeEvent event = changeEventCaptor.getValue();
        assertEquals(this, event.getSource());
        assertEquals(propertyName, event.getPropertyName());
        assertEquals(index, event.getIndex());
        assertEquals(oldValue, event.getOldValue());
        assertEquals(newValue, event.getNewValue());
    }

    @Test
    void fireIndexedBooleanPropertyChange() {
        fireIndexedPropertyChange("indexedObjectProperty", 1, false, true);

        verifyIndexedPropertyChanged("indexedObjectProperty", 1, false, true);
    }

    @Test
    void fireIndexedIntegerPropertyChange() {
        fireIndexedPropertyChange("indexedObjectProperty", 1, 2, 3);

        verifyIndexedPropertyChanged("indexedObjectProperty", 1, 2, 3);
    }

}