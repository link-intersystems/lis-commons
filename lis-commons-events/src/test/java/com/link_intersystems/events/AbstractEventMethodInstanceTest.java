package com.link_intersystems.events;

import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.mockito.Mockito.mock;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AbstractEventMethodInstanceTest {
    private EventMethod<?, ?> eventMethod;

    public AbstractEventMethodInstanceTest(EventMethod<?, ?> eventMethod) {
        this.eventMethod = eventMethod;
    }

    Stream<EventMethodTestCase> testCases() {
        EventMethod<?, ?> eventMethod = getEventMethod();
        List<Method> methodNames = eventMethod.getEventMethods();

        return methodNames.stream().map(m -> new EventMethodTestCase(m, eventMethod));
    }

    protected EventMethod<?, ?> getEventMethod() {
        return eventMethod;
    }

    protected static class EventMethodTestCase {

        protected Method method;
        protected EventMethod<?, ?> eventMethod;

        public EventMethodTestCase(Method method, EventMethod<?, ?> eventMethod) {
            this.method = method;
            this.eventMethod = eventMethod;
        }

        Object newEventObject() {
            return mock(eventMethod.getEventObjectClass());
        }

        @Override
        public String toString() {
            return eventMethod.getEventObjectClass().getSimpleName() + "." + method.getName();
        }

        public Member getTestMethod() {
            return method;
        }
    }

    protected class CompilerHelper<T, U> {

        private BiConsumer<T, U> biConsumer;

        CompilerHelper(BiConsumer<T, U> biConsumer) {
            this.biConsumer = biConsumer;
        }

        protected void consume(T t, U u) {
            biConsumer.accept(t, u);
        }
    }

    protected void invokeListenerMethod(Object listener, Method method, Object eventObject) {
        try {
            method.invoke(listener, eventObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Assertions.fail(e);
        }
    }
}
