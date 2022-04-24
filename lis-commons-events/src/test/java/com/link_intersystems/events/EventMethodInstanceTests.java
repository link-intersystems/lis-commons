package com.link_intersystems.events;

import java.util.function.*;

import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class EventMethodInstanceTests extends AbstractEventMethodInstanceTest {

    public EventMethodInstanceTests(EventMethod<?, ?> eventMethod) {
        super(eventMethod);
    }

    void runnable(EventMethodTestCase description) {
        Runnable runnable = mock(Runnable.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(runnable);

        invokeListenerMethod(listener, description.method, description.newEventObject());

        verify(runnable, times(1)).run();
    }

    void runnableFiltered(EventMethodTestCase description) {
        Runnable runnable = mock(Runnable.class);
        Predicate predicate = mock(Predicate.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(runnable, predicate);

        Object eventObject = description.newEventObject();
        when(predicate.test(eventObject)).thenReturn(true).thenReturn(false);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(runnable, times(1)).run();
        verify(predicate, times(2)).test(eventObject);
    }


    void consumer(EventMethodTestCase description) {
        Consumer consumer = mock(Consumer.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer);

        Object eventObject = description.newEventObject();
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept(eventObject);
    }


    void consumerFiltered(EventMethodTestCase description) {
        Consumer consumer = mock(Consumer.class);
        Predicate predicate = mock(Predicate.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, predicate);

        Object eventObject = description.newEventObject();
        when(predicate.test(eventObject)).thenReturn(true).thenReturn(false);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept(eventObject);
        verify(predicate, times(2)).test(eventObject);
    }

    void consumerTransformed(EventMethodTestCase description) {
        Consumer consumer = mock(Consumer.class);
        Function transformFunc = mock(Function.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, transformFunc);

        Object eventObject = description.newEventObject();
        when(transformFunc.apply(eventObject)).thenReturn("A");

        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept("A");
    }

    void consumerFilteredTransformed(EventMethodTestCase description) {
        Consumer consumer = mock(Consumer.class);
        Function transformFunc = mock(Function.class);
        Predicate predicate = mock(Predicate.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, transformFunc, predicate);

        Object eventObject = description.newEventObject();
        when(transformFunc.apply(eventObject)).thenReturn("A");
        when(predicate.test(eventObject)).thenReturn(true).thenReturn(false);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept("A");
        verify(transformFunc, times(1)).apply(eventObject);
        verify(predicate, times(2)).test(eventObject);
    }


    void biConsumer(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, "A");

        Object eventObject = description.newEventObject();

        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept(eventObject, "A");
    }

    void biConsumerFiltered(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Predicate predicate = mock(Predicate.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, "A", predicate);

        Object eventObject = description.newEventObject();

        when(predicate.test(eventObject)).thenReturn(true).thenReturn(false);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept(eventObject, "A");
        verify(predicate, times(2)).test(eventObject);
    }


    void biConsumerTransformed(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Function transformFunc = mock(Function.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, transformFunc, "B");

        Object eventObject = description.newEventObject();

        when(transformFunc.apply(eventObject)).thenReturn("A");

        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept("A", "B");
        verify(transformFunc, times(1)).apply(eventObject);
    }

    void biConsumerFilteredTransformed(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Function transformFunc = mock(Function.class);
        Predicate predicate = mock(Predicate.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;
        Object listener = testEventMethod.listener(consumer, transformFunc, "B", predicate);

        Object eventObject = description.newEventObject();
        when(transformFunc.apply(eventObject)).thenReturn("A");
        when(predicate.test(eventObject)).thenReturn(true).thenReturn(false);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept("A", "B");
        verify(transformFunc, times(1)).apply(eventObject);
        verify(predicate, times(2)).test(eventObject);
    }

    void biConsumerWithParamSupplier(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Supplier supplier = mock(Supplier.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;

        CompilerHelper<Object, String> testClass = new CompilerHelper<>(consumer);
        Object listener = testEventMethod.listener(testClass::consume, supplier);

        Object eventObject = description.newEventObject();
        when(supplier.get()).thenReturn("A");

        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept(eventObject, "A");
        verify(supplier, times(1)).get();
    }

    void biConsumerFilteredWithParamSupplier(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Predicate predicate = mock(Predicate.class);
        Supplier supplier = mock(Supplier.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;

        CompilerHelper<Object, String> testClass = new CompilerHelper<>(consumer);
        Object listener = testEventMethod.listener(testClass::consume, supplier, predicate);

        Object eventObject = description.newEventObject();
        when(supplier.get()).thenReturn("A");
        when(predicate.test(eventObject)).thenReturn(false).thenReturn(true);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept(eventObject, "A");
        verify(supplier, times(1)).get();
        verify(predicate, times(2)).test(eventObject);
    }


    void biConsumerTransformedWithParamSupplier(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Function transformFunc = mock(Function.class);
        Supplier supplier = mock(Supplier.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;

        CompilerHelper<String, String> compilerHelper = new CompilerHelper(consumer);
        Object listener = testEventMethod.listener(compilerHelper::consume, transformFunc, supplier);

        Object eventObject = description.newEventObject();
        when(supplier.get()).thenReturn("B");
        when(transformFunc.apply(eventObject)).thenReturn("A");

        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept("A", "B");
        verify(supplier, times(1)).get();
    }

    void biConsumerFilteredTransformedWithParamSupplier(EventMethodTestCase description) {
        BiConsumer consumer = mock(BiConsumer.class);
        Function transformFunc = mock(Function.class);
        Predicate predicate = mock(Predicate.class);
        Supplier supplier = mock(Supplier.class);

        EventMethod<?, ?> testEventMethod = description.eventMethod;

        CompilerHelper<String, String> compilerHelper = new CompilerHelper(consumer);
        Object listener = testEventMethod.listener(compilerHelper::consume, transformFunc, supplier, predicate);

        Object eventObject = description.newEventObject();
        when(supplier.get()).thenReturn("B");
        when(transformFunc.apply(eventObject)).thenReturn("A");
        when(predicate.test(eventObject)).thenReturn(false).thenReturn(true);

        invokeListenerMethod(listener, description.method, eventObject);
        invokeListenerMethod(listener, description.method, eventObject);

        verify(consumer, times(1)).accept("A", "B");
        verify(supplier, times(1)).get();
        verify(predicate, times(2)).test(eventObject);
    }


}
