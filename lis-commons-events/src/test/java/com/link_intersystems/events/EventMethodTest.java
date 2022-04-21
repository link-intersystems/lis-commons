package com.link_intersystems.events;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EventListener;
import java.util.EventObject;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class EventMethodTest {

    private static class SomeEventMethod extends EventMethod<SomeListener, SomeEvent> {
    }

    private static class SomeEventMethod2 extends EventMethod<SomeListener, SomeEvent> {
    }


    private static class SomeOtherEventMethod extends EventMethod<SomeOtherListener, SomeOtherEvent> {
    }

    private SomeEventMethod someEventMethod;
    private SomeOtherEventMethod someOtherEventMethod;

    @BeforeEach
    void setUp() {
        someEventMethod = new SomeEventMethod();
        someOtherEventMethod = new SomeOtherEventMethod();
    }

    @Test
    void isCompatible() {
        assertTrue(someEventMethod.isCompatible(someEventMethod));

        assertFalse(someEventMethod.isCompatible(someOtherEventMethod));
        assertFalse(someOtherEventMethod.isCompatible(someEventMethod));
    }

    @Test
    void join() {
        someEventMethod.join(new SomeEventMethod2());

        assertThrows(IllegalArgumentException.class, () -> someEventMethod.join((EventMethod) someOtherEventMethod));
    }

    @Test
    void toStringTest() {
        assertNotNull(someEventMethod.toString());
    }


    private static interface SomeListener extends EventListener {

        public void someEvent(SomeEvent e);
    }

    private static class SomeEvent extends EventObject {

        public SomeEvent(Object source) {
            super(source);
        }
    }

    private static interface SomeOtherListener extends EventListener {

        public void someOtherEvent(SomeOtherEvent e);
    }

    private static class SomeOtherEvent extends EventObject {

        public SomeOtherEvent(Object source) {
            super(source);
        }
    }
}