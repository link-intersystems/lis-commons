package com.link_intersystems.lang.ref;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObservableReferenceAdapterTest {

    private ObservableReference<String> observableReference;
    private PropertyChangeListener propertyChangeListenerMock;

    @BeforeEach
    public void setup() {
        DefaultMutableReference<String> defaultMutableReference = new DefaultMutableReference<String>("hello");
        observableReference = new ObservableReferenceAdapter<String>(defaultMutableReference);
        propertyChangeListenerMock = EasyMock.createNiceMock(PropertyChangeListener.class);
    }

    @Test
    public void propertyChangeEvent() {
        propertyChangeListenerMock.propertyChange(EasyMock.anyObject(PropertyChangeEvent.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Void>() {

            public Void answer() throws Throwable {
                Object[] currentArguments = EasyMock.getCurrentArguments();
                PropertyChangeEvent event = (PropertyChangeEvent) currentArguments[0];

                String propertyName = event.getPropertyName();
                assertEquals(ObservableReference.PROPERTY_REFERENT, propertyName);

                Object oldValue = event.getOldValue();
                assertEquals("hello", oldValue);

                Object newObject = event.getNewValue();
                assertEquals("hello world", newObject);

                Object source = event.getSource();
                assertEquals(observableReference, source);

                return null;
            }
        });
        replay(propertyChangeListenerMock);

        observableReference.addPropertyChangeListener(propertyChangeListenerMock);

        observableReference.set("hello world");

        verify(propertyChangeListenerMock);
    }

    @Test
    public void setAndGet() {
        assertEquals("hello", observableReference.get());
        observableReference.set("hello world");
        assertEquals("hello world", observableReference.get());
    }

    @Test
    public void propertyChangeEventAfterRemove() {
        replay(propertyChangeListenerMock);
        observableReference.addPropertyChangeListener(propertyChangeListenerMock);
        observableReference.removePropertyChangeListener(propertyChangeListenerMock);
        observableReference.set("hello world");

        verify(propertyChangeListenerMock);
    }
}
