package com.link_intersystems.util.function;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class CallableRunnableTest {

    private String getString() {
        return "ABC";
    }

    @Test
    void run() {
        CallableRunnable<String> runnable = new CallableRunnable<>(this::getString);
        runnable.run();

        assertEquals("ABC", runnable.getResult());
    }

    @Test
    void onException() throws Exception {
        Callable callable = Mockito.mock(Callable.class);
        Mockito.when(callable.call()).thenThrow(IllegalStateException.class);

        CallableRunnable runnable = new CallableRunnable<>(callable);

        assertThrows(RuntimeException.class, () -> runnable.run());
    }
}