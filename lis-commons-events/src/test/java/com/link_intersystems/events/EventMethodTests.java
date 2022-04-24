package com.link_intersystems.events;

import com.link_intersystems.events.awt.*;
import com.link_intersystems.events.beans.BeanContextMembershipEventMethod;
import com.link_intersystems.events.beans.BeanContextServiceRevokedEventMethod;
import com.link_intersystems.events.beans.PropertyChangeMethod;
import com.link_intersystems.events.beans.VetoableChangeMethod;
import com.link_intersystems.events.naming.NamespaceChangeEventMethod;
import com.link_intersystems.events.naming.NamingEventMethod;
import com.link_intersystems.events.prefs.NodeChangeEventMethod;
import com.link_intersystems.events.sql.ConnectionEventMethod;
import com.link_intersystems.events.sql.RowSetEventMethod;
import com.link_intersystems.events.sql.StatementEventMethod;
import com.link_intersystems.events.swing.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@DisplayName("EventMethodTests")
public class EventMethodTests {

    @TestFactory
    Stream<DynamicNode> awt() {
        return asList(
                ActionEventMethod.class,
                AdjustmentEventMethod.class,
                ComponentEventMethod.class,
                ContainerEventMethod.class,
                FocusEventMethod.class,
                HierarchyBoundsEventMethod.class,
                HierarchyEventMethod.class,
                ItemEventMethod.class,
                KeyEventMethod.class,
                MouseEventMethod.class,
                MouseWheelEventMethod.class,
                WindowEventMethod.class,
                WindowFocusEventMethod.class,
                WindowStateEventMethod.class
        ).stream().map(this::createEventMethodNode);
    }

    @TestFactory
    Stream<DynamicNode> beans() {
        return asList(
                BeanContextMembershipEventMethod.class,
                BeanContextServiceRevokedEventMethod.class,
                PropertyChangeMethod.class,
                VetoableChangeMethod.class
        ).stream().map(this::createEventMethodNode);
    }

    @TestFactory
    Stream<DynamicNode> naming() {
        return asList(
                NamingEventMethod.class,
                NamespaceChangeEventMethod.class
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
                AncestorEventMethod.class,
                CaretEventMethod.class,
                CellEditorEventMethod.class,
                ChangeEventMethod.class,
                DocumentEventMethod.class,
                InternalFrameEventMethod.class,
                ListDataEventMethod.class,
                ListSelectionEventMethod.class,
                MenuEventMethod.class,
                PopupMenuEventMethod.class,
                TableColumnModelEventMethod.class,
                TableModelEventMethod.class,
                TreeExpansionEventMethod.class,
                TreeModelEventMethod.class,
                TreeSelectionEventMethod.class
        ).stream().map(this::createEventMethodNode);
    }

    @TestFactory
    Stream<DynamicNode> sql() {
        return asList(
                ConnectionEventMethod.class,
                RowSetEventMethod.class,
                StatementEventMethod.class
        ).stream().map(this::createEventMethodNode);
    }


    private DynamicContainer createEventMethodNode(Class<? extends EventMethod> eventMethodClass) {
        List<EventMethod<?, ?>> eventMethods = toEventMethods(eventMethodClass);


        Stream<DynamicNode> dynamicNodeStream = eventMethods.stream().map(em -> {
            EventMethodInstanceTests abstractEventMethodTest = new EventMethodInstanceTests(em);
            Stream<AbstractEventMethodInstanceTest.EventMethodTestCase> testCases = abstractEventMethodTest.testCases();
            return testCases.map(tc -> createTestCaseNode(abstractEventMethodTest, tc));
        }).flatMap(Function.identity());

        return DynamicContainer.dynamicContainer(eventMethodClass.getSimpleName(), dynamicNodeStream);
    }

    private DynamicNode createTestCaseNode(EventMethodInstanceTests abstractEventMethodTest, AbstractEventMethodInstanceTest.EventMethodTestCase testCase) {
        Class<?> aClass = abstractEventMethodTest.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();

        Class<?>[] testMethoParams = new Class<?>[]{AbstractEventMethodInstanceTest.EventMethodTestCase.class};
        Stream<DynamicTest> dynamicTests = stream(declaredMethods).filter(m -> Arrays.equals(m.getParameterTypes(), testMethoParams))
                .map(m -> createMethodTest(m, abstractEventMethodTest, testCase));
        return DynamicContainer.dynamicContainer(testCase.getTestMethod().getName(), dynamicTests);
    }

    private DynamicTest createMethodTest(Method m, EventMethodInstanceTests abstractEventMethodTest, AbstractEventMethodInstanceTest.EventMethodTestCase testCase) {
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
