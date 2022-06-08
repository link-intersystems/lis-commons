package com.link_intersystems.util.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.concurrent.Executors;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FunctionExecutorAdaptersTest {

    private FunctionExecutorAdapters executorAdapters;
    private FunctionExecutor functionExecutor;

    @BeforeEach
    void setUp() {
        functionExecutor = new ExecutorFunctionExecutor(Executors.newSingleThreadExecutor());
        executorAdapters = new FunctionExecutorAdapters(functionExecutor);
    }

    @Test
    void runnableAdapter() {
        Runnable runnable = mock(Runnable.class);

        executorAdapters.adapter(runnable).run();

        verify(runnable, times(1)).run();
    }


    @Test
    void functionAdapter() {
        Function<Integer, String> adaptable = mock(Function.class);
        when(adaptable.apply(5)).thenReturn("%");

        String result = executorAdapters.adapter(adaptable).apply(5);

        assertEquals("%", result);
    }

    @Test
    void biFunctionAdapter() {
        BiFunction<Integer, String, Double> adaptable = mock(BiFunction.class);
        when(adaptable.apply(5, "A")).thenReturn(10.5);

        Double result = executorAdapters.adapter(adaptable).apply(5, "A");

        assertEquals(10.5, result);
    }

    @Test
    void consumerAdapter() {
        Consumer<Integer> adaptable = mock(Consumer.class);

        executorAdapters.adapter(adaptable).accept(5);

        verify(adaptable, times(1)).accept(5);
    }

    @Test
    void biConsumerAdapter() {
        BiConsumer<Integer, String> adaptable = mock(BiConsumer.class);

        executorAdapters.adapter(adaptable).accept(5, "A");

        verify(adaptable, times(1)).accept(5, "A");
    }

    @Test
    void biConsumerWithValue() {
        BiConsumer<Integer, String> adaptable = mock(BiConsumer.class);

        executorAdapters.adapter(adaptable, "A").accept(5);

        verify(adaptable, times(1)).accept(5, "A");
    }

    @Test
    void biConsumerWithValueSupplier() {
        BiConsumer<Integer, String> adaptable = mock(BiConsumer.class);

        executorAdapters.adapter(adaptable, (Supplier<String>) () -> "A").accept(5);

        verify(adaptable, times(1)).accept(5, "A");
    }

}