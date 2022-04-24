package com.link_intersystems.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class InheritanceTest {

    private Consumer consumer;
    private BiConsumer<CharSequence, String> biconsumer;
    private BiConsumer<Integer, String> biconsumerInt;
    private Callable<String> callable;

    @BeforeEach
    void setUp() {
        consumer = mock(Consumer.class);
        biconsumer = mock(BiConsumer.class);
        biconsumerInt = mock(BiConsumer.class);
        callable = mock(Callable.class);
    }


    @Test
    void ifInstanceConsumer() {
        Inheritance.INSTANCE.ifInstance("ABC", CharSequence.class, consumer);

        verify(consumer, times(1)).accept("ABC");
    }

    @Test
    void ifNotInstanceConsumer() {
        Inheritance.INSTANCE.ifInstance("ABC", Integer.class, consumer);

        verify(consumer, never()).accept(any());
    }

    @Test
    void testIfInstanceBiConsumerParamSource() {
        Inheritance.INSTANCE.ifInstance("ABC", CharSequence.class, biconsumer, this::paramSupplier);

        verify(biconsumer, times(1)).accept("ABC", "PARAM");
    }

    private String paramSupplier() {
        return "PARAM";
    }


    @Test
    void testIfNotInstanceBiConsumerParamSource() {
        Inheritance.INSTANCE.ifInstance("ABC", Integer.class, biconsumerInt, this::paramSupplier);

        verify(biconsumerInt, never()).accept(any(), any());
    }

    @Test
    void ifInstanceCall() {
        Inheritance.INSTANCE.ifInstance("ABC", Integer.class, biconsumerInt, this::paramSupplier);

        verify(biconsumerInt, never()).accept(any(), any());
    }

    @Test
    void testIfInstanceCall() {
        String result = Inheritance.INSTANCE.ifInstanceCall("ABC", CharSequence.class, this::callWithCharSequence);

        assertEquals("ABC PARAM", result);
    }


    private String callWithCharSequence(CharSequence value) {
        return value + " PARAM";
    }

    @Test
    void testIfInstanceCallNotAdaptable() {
        String result = Inheritance.INSTANCE.ifInstanceCall("ABC", Integer.class, this::callWithInteger, "NOT_ADAPTABLE");

        assertEquals("NOT_ADAPTABLE", result);
    }

    private String callWithInteger(Integer value) {
        return value + " PARAM";
    }


    @Test
    void testIfInstanceCallWithParamSupplier() {
        String result = Inheritance.INSTANCE.ifInstanceCall("ABC", CharSequence.class, this::callWithCharSequenceAndInteger, () -> 1);

        assertEquals("ABC 1 PARAM", result);
    }

    private String callWithCharSequenceAndInteger(CharSequence charSequence, Integer value) {
        return charSequence + " " + value + " PARAM";
    }

    @Test
    void testIfInstanceCallWithParamSupplierNull() {
        String result = Inheritance.INSTANCE.ifInstanceCall(null, CharSequence.class, this::callWithCharSequenceAndInteger, () -> 1);

        assertEquals(null, result);
    }

}