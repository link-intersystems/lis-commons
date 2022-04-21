package com.link_intersystems.events;

import com.link_intersystems.events.awt.*;
import com.link_intersystems.events.beans.PropertyChangeMethod;
import com.link_intersystems.events.prefs.NodeChangeEventMethod;
import com.link_intersystems.events.swing.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.mockito.Mockito.mock;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AbstractEventMethodTest {
    private EventMethod<?, ?> eventMethod;

    public AbstractEventMethodTest(EventMethod<?, ?> eventMethod) {
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

    @DisplayName("EventMethodTests")
    public static class Factory {

        @TestFactory
        Stream<DynamicNode> awt() {
            return asList(
                    ActionEventMethod.class,
                    ComponentEventMethod.class,
                    ContainerEventMethod.class,
                    FocusEventMethod.class,
                    HierarchyEventMethod.class,
                    KeyEventMethod.class,
                    MouseEventMethod.class,
                    WindowsEventMethod.class
            ).stream().map(this::createEventMethodNode);
        }

        @TestFactory
        Stream<DynamicNode> beans() {
            return asList(
                    PropertyChangeMethod.class
            ).stream().map(this::createEventMethodNode);
        }

        @TestFactory
        Stream<DynamicNode> prefs() {
            return asList(
                    NodeChangeEventMethod.class
            ).stream().map(this::createEventMethodNode);
        }

        @TestFactory
        Stream<DynamicNode> swing() {
            return asList(
                    ChangeEventMethod.class,
                    InternalFrameEventMethod.class,
                    ListDataEventMethod.class,
                    ListSelectionEventMethod.class,
                    TableColumnModelEventMethod.class,
                    TableModelEventMethod.class,
                    TreeExpansionEventMethod.class,
                    TreeModelEventMethod.class,
                    TreeSelectionEventMethod.class
            ).stream().map(this::createEventMethodNode);
        }


        private DynamicContainer createEventMethodNode(Class<? extends EventMethod> eventMethodClass) {
            List<EventMethod<?, ?>> eventMethods = toEventMethods(eventMethodClass);


            Stream<DynamicNode> dynamicNodeStream = eventMethods.stream().map(em -> {
                EventMethodTests abstractEventMethodTest = new EventMethodTests(em);
                Stream<EventMethodTestCase> testCases = abstractEventMethodTest.testCases();
                return testCases.map(tc -> createTestCaseNode(abstractEventMethodTest, tc));
            }).flatMap(Function.identity());

            return DynamicContainer.dynamicContainer(eventMethodClass.getSimpleName(), dynamicNodeStream);
        }

        private DynamicNode createTestCaseNode(EventMethodTests abstractEventMethodTest, AbstractEventMethodTest.EventMethodTestCase testCase) {
            Class<?> aClass = abstractEventMethodTest.getClass();
            Method[] declaredMethods = aClass.getDeclaredMethods();

            Class<?>[] testMethoParams = new Class<?>[]{AbstractEventMethodTest.EventMethodTestCase.class};
            Stream<DynamicTest> dynamicTests = stream(declaredMethods).filter(m -> Arrays.equals(m.getParameterTypes(), testMethoParams))
                    .map(m -> createMethodTest(m, abstractEventMethodTest, testCase));
            return DynamicContainer.dynamicContainer(testCase.getTestMethod().getName(), dynamicTests);
        }

        private DynamicTest createMethodTest(Method m, EventMethodTests abstractEventMethodTest, AbstractEventMethodTest.EventMethodTestCase testCase) {
            return DynamicTest.dynamicTest(m.getName(), () -> m.invoke(abstractEventMethodTest, testCase));
        }

        private List<EventMethod<?, ?>> toEventMethods(Class<? extends EventMethod> eventMethodClass) {
            List<EventMethod<?, ?>> eventMethods = new ArrayList<>();

            Field[] declaredFields = eventMethodClass.getDeclaredFields();
            Stream<EventMethod> eventMethodStream = stream(declaredFields).filter(f -> EventMethod.class.isAssignableFrom(f.getType())).map(f -> {
                try {
                    return f.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).map(EventMethod.class::cast);

            eventMethodStream.forEach(em -> {
                for (int i = 0; i < eventMethods.size(); i++) {
                    EventMethod<?, ?> currEventMethod = eventMethods.get(i);
                    if (currEventMethod.isCompatible(em)) {
                        eventMethods.set(i, currEventMethod.join(em));
                        return;
                    }
                }

                eventMethods.add(em);
            });

            return eventMethods;
        }
    }

    protected static class EventMethodTestCase {

        protected Method method;
        protected EventMethod<?, ?> eventMethod;

        public EventMethodTestCase(Method method, EventMethod<?, ?> eventMethod) {
            this.method = method;
            this.eventMethod = eventMethod;
        }

        EventObject newEventObject() {
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

    protected void invokeListenerMethod(Object listener, Method method, EventObject eventObject) {
        try {
            method.invoke(listener, eventObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Assertions.fail(e);
        }
    }
}
