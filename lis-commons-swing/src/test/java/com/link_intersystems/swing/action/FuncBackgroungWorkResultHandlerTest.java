package com.link_intersystems.swing.action;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class FuncBackgroungWorkResultHandlerTest {

    @Test
    void intermediateResults() {
        Consumer<List<String>> consumer = mock(Consumer.class);

        FuncBackgroundWorkResultHandler<String, String> funcAsyncWorkLifecycle = new FuncBackgroundWorkResultHandler.Builder<String, String>()
                .setIntermediateResultsConsumer(consumer).build();

        funcAsyncWorkLifecycle.publishIntermediateResults(Arrays.asList("A", "B"));
        verify(consumer).accept(Arrays.asList("A", "B"));
    }

    @Test
    void done() {
        Consumer<String> consumer = mock(Consumer.class);

        FuncBackgroundWorkResultHandler<String, String> funcAsyncWorkLifecycle = new FuncBackgroundWorkResultHandler.Builder<String, String>()
                .setDoneConsumer(consumer).build();

        funcAsyncWorkLifecycle.done("A");
        verify(consumer).accept("A");
    }

    @Test
    void failed() {
        Consumer<ExecutionException> consumer = mock(Consumer.class);

        FuncBackgroundWorkResultHandler<String, String> funcAsyncWorkLifecycle = new FuncBackgroundWorkResultHandler.Builder<String, String>()
                .setFailedConsumer(consumer).build();

        ExecutionException executionException = new ExecutionException(new RuntimeException());
        funcAsyncWorkLifecycle.failed(executionException);
        verify(consumer).accept(executionException);
    }

    @Test
    void interrupted() {
        Consumer<InterruptedException> consumer = mock(Consumer.class);

        FuncBackgroundWorkResultHandler<String, String> funcAsyncWorkLifecycle = new FuncBackgroundWorkResultHandler.Builder<String, String>()
                .setInterruptedConsumer(consumer).build();

        InterruptedException interruptedException = new InterruptedException();
        funcAsyncWorkLifecycle.interrupted(interruptedException);
        verify(consumer).accept(interruptedException);
    }
}