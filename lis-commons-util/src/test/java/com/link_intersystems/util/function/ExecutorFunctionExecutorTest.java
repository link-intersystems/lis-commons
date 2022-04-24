package com.link_intersystems.util.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ExecutorFunctionExecutorTest {

    private FunctionExecutor functionExecutor;
    private CountDownLatch countDownLatch;

    @BeforeEach
    void setUp() {
        functionExecutor = ExecutorFunctionExecutor.newSingleThreadFunctionExecutor();
        countDownLatch = new CountDownLatch(1);
    }

    @Test
    void exec() throws InterruptedException {

        functionExecutor.exec(() -> countDownLatch.countDown());

        countDownLatch.await(10L, TimeUnit.MILLISECONDS);
    }

    @Test
    void execCallable() {
        String result = functionExecutor.exec(() -> "A");

        assertEquals("A", result);
    }

    @Test
    void execBiFunction() {
        String result = functionExecutor.exec((s1, s2) -> s1 + s2, "Hello ", "World");

        assertEquals("Hello World", result);
    }

    @Test
    void execBiConsumer() {
        BiConsumer<String, String> consumer = Mockito.mock(BiConsumer.class);

        functionExecutor.exec(consumer, "Hello", "World");

        verify(consumer, times(1)).accept("Hello", "World");
    }


    @Test
    void execAdapter() {
        BiConsumer<String, String> consumer = Mockito.mock(BiConsumer.class);

        Function<String, String> toLowerCaseExecutor = functionExecutor.execAdapter((String s) -> s.toLowerCase());

        assertEquals("hello", toLowerCaseExecutor.apply("HELLO"));
    }

}