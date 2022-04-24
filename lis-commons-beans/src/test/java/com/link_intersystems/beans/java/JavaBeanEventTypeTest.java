package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.event.ChangeListener;
import java.beans.EventSetDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class JavaBeanEventTypeTest {


    private JavaBeanEventType javaBeanEventType;
    private EventSetDescriptor eventDescriptor;

    @BeforeEach
    void setUp() {
        eventDescriptor = SomeBean.getPropertyChangeEventDescriptor();
        javaBeanEventType = new JavaBeanEventType(new JavaBeanClass<>(SomeBean.class), eventDescriptor);
    }

    @Test
    void getEventDescriptor() {
        assertEquals(eventDescriptor, javaBeanEventType.getEventDescriptor());
    }

    @Test
    void getName() {
        assertEquals("propertyChange", javaBeanEventType.getName());
    }

    @Test
    void newBeanEvent() {
        JavaBeanEvent javaBeanEvent = javaBeanEventType.newBeanEvent(new SomeBean());

        assertNotNull(javaBeanEvent);
    }

    @Test
    void newBeanEventWrongInstance() {
        assertThrows(IllegalArgumentException.class, () -> javaBeanEventType.newBeanEvent(""));
    }

    @Test
    void isApplicableByObject() {
        assertTrue(javaBeanEventType.isApplicable(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        }));
    }

    @Test
    void isApplicableByClass() {
        assertTrue(javaBeanEventType.isApplicable(PropertyChangeListener.class));
    }

    @Test
    void isNotApplicableByClass() {
        assertFalse(javaBeanEventType.isApplicable(ChangeListener.class));
    }

    @Test
    void addListener() {
        SomeBean someBean = new SomeBean();

        PropertyChangeListener listener = mock(PropertyChangeListener.class);

        javaBeanEventType.addListener(someBean, listener);

        someBean.setStringProperty("TEST");

        verify(listener, times(1)).propertyChange(any());
    }

    @Test
    void removeListener() {
        SomeBean someBean = new SomeBean();

        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        someBean.addPropertyChangeListener(listener);

        javaBeanEventType.removeListener(someBean, listener);

        someBean.setStringProperty("TEST");

        verify(listener, never()).propertyChange(any());
    }

    @Test
    void testToString() {
        assertNotNull(javaBeanEventType.toString());
    }
}