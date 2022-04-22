package com.link_intersystems.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EventListener;
import java.util.EventObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@SuppressWarnings("rawtypes")
class EventMethodTest {

    private static class SomeEventMethod extends EventMethod<SomeListener, SomeEvent> {
    }

    private static class SomeEventMethod2 extends EventMethod<SomeListener, SomeEvent> {
    }

    private static class SomeOtherEventMethod extends EventMethod<SomeOtherListener, SomeOtherEvent> {
    }

    private static abstract class BaseEventMethod<L, E extends EventObject> extends EventMethod<L, E> {
    }

    private static class ExtendsBase extends BaseEventMethod<SomeListener, SomeEvent> {
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

    @Test
    void resolveEventListenerAndObjectClassDeeperHierarchy() {
        ExtendsBase extendsBase = new ExtendsBase();

        assertEquals(SomeEvent.class, extendsBase.getEventObjectClass());
        assertEquals(SomeListener.class, extendsBase.getListenerClass());
    }

    @Test
    void resolveEventListenerAndObjectClassAnonymousClass() {
        EventMethod eventMethod = new EventMethod<SomeListener, SomeEvent>() {
        };

        assertEquals(SomeEvent.class, eventMethod.getEventObjectClass());
        assertEquals(SomeListener.class, eventMethod.getListenerClass());
    }

    @Test
    void resolveEventListenerAndObjectClassByTypes() {
        EventMethod<SomeListener, SomeEvent> eventMethod = new EventMethod<>(SomeListener.class, SomeEvent.class);

        assertEquals(SomeEvent.class, eventMethod.getEventObjectClass());
        assertEquals(SomeListener.class, eventMethod.getListenerClass());
    }

    @Test
    void wrongListenerMethod() {
        EventMethod<WrongListener, SomeEvent> eventMethod = new EventMethod<>(WrongListener.class, SomeEvent.class);

        WrongListener wrongListener = eventMethod.listener(() -> {
        });

        assertThrows(UnsupportedOperationException.class, () -> wrongListener.someMethod(new SomeEvent("")));
    }

    @Test
    void notSupportedMethod() {
        EventMethod<SomeListener, SomeEvent> eventMethod = new EventMethod<>(SomeListener.class, SomeEvent.class, "someEvent2");

        Runnable runnable = mock(Runnable.class);
        SomeListener someListener = eventMethod.listener(runnable);

        someListener.someEvent(new SomeEvent(""));

        verify(runnable, never()).run();
    }

    private static interface SomeListener extends EventListener {

        public void someEvent(SomeEvent e);

        public void someEvent2(SomeEvent e);
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

    interface WrongListener {
        String someMethod(SomeEvent e);
    }
}