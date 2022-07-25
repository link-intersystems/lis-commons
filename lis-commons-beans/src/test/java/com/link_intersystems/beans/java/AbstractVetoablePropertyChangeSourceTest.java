package com.link_intersystems.beans.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AbstractVetoablePropertyChangeSourceTest extends AbstractVetoablePropertyChangeSource {

    private VetoableChangeListener changeListener;

    @BeforeEach
    void setup() {
        changeListener = mock(VetoableChangeListener.class);
        addVetoableChangeListener(changeListener);
    }

    @AfterEach
    void tearDown() {
        removeVetoableChangeListener(changeListener);
    }

    @Test
    void acceptObjectChange() throws PropertyVetoException {
        fireVetoableChange("stringProperty", "oldValue", "newValue");

        verifyVetoableChange("stringProperty", "oldValue", "newValue");
    }

    @Test
    void rejectObjectChange() throws PropertyVetoException {
        PropertyChangeEvent evt = new PropertyChangeEvent(this, "stringProperty", "oldValue", "newValue");
        doThrow(new PropertyVetoException("", evt)).when(changeListener).vetoableChange(Mockito.any());


        assertThrows(PropertyVetoException.class, () -> fireVetoableChange("stringProperty", "oldValue", "newValue"));

        verifyVetoableChange("stringProperty", "oldValue", "newValue");
    }

    private void verifyVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
        ArgumentCaptor<PropertyChangeEvent> changeEventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(changeListener, times(1)).vetoableChange(changeEventCaptor.capture());

        PropertyChangeEvent event = changeEventCaptor.getValue();
        assertEquals(this, event.getSource());
        assertEquals(propertyName, event.getPropertyName());
        assertEquals(oldValue, event.getOldValue());
        assertEquals(newValue, event.getNewValue());
    }


    @Test
    void acceptIntegerChange() throws PropertyVetoException {
        fireVetoableChange("intProperty", 1, 2);

        verifyVetoableChange("intProperty", 1, 2);
    }

    @Test
    void acceptBooleanChange() throws PropertyVetoException {
        fireVetoableChange("booleanProperty", false, true);

        verifyVetoableChange("booleanProperty", false, true);
    }

}