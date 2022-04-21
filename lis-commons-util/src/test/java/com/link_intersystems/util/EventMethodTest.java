package com.link_intersystems.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Consumer;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class EventMethodTest {

    static class ComponentEventMethods extends EventMethod<ComponentListener, ComponentEvent> {

        public static final ComponentEventMethods INSTANCE = new ComponentEventMethods();

        public ComponentEventMethods(String... methodNames) {
            super(ComponentListener.class, ComponentEvent.class, methodNames);
        }
    }

    private Component component;

    @BeforeEach
    void setUp() {
        component = new Container();
    }

    @Test
    void accept() {
        Consumer<ComponentEvent> eventConsumer = Mockito.mock(Consumer.class);
        ComponentListener listener = ComponentEventMethods.INSTANCE.listener(eventConsumer);

        ComponentEvent e = new ComponentEvent(component, 1);
        listener.componentResized(e);

        Mockito.verify(eventConsumer, Mockito.times(1)).accept(e);

    }
}