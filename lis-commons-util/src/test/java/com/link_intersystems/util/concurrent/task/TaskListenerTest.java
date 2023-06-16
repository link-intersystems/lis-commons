package com.link_intersystems.util.concurrent.task;

import com.link_intersystems.util.concurrent.task.TaskListener;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskListenerTest {

    @Test
    void nullInstance() {
        TaskListener<Object, Object> nullInstance = TaskListener.nullInstance();

        nullInstance.publishIntermediateResults(Arrays.asList());
        nullInstance.done("");

        RuntimeException runtimeException = new RuntimeException();
        RuntimeException thrownRuntimeException = assertThrows(RuntimeException.class, () -> nullInstance.failed(new ExecutionException(runtimeException)));
        assertEquals(runtimeException, thrownRuntimeException);


        Exception checkedException = new Exception();
        RuntimeException thrownCheckedException = assertThrows(RuntimeException.class, () -> nullInstance.failed(new ExecutionException(checkedException)));
        assertEquals(checkedException, thrownCheckedException.getCause());


        InterruptedException interruptedException = new InterruptedException();
        RuntimeException thrownInterruptedException = assertThrows(RuntimeException.class, () -> nullInstance.interrupted(interruptedException));
        assertEquals(interruptedException, thrownInterruptedException.getCause());
    }

    @Test
    void intermediateResults() {
        Consumer<List<String>> consumer = mock(Consumer.class);

        FuncTaskListener<String, String> funcAsyncWorkLifecycle = new FuncTaskListener.Builder<String, String>()
                .setIntermediateResultsConsumer(consumer).build();

        funcAsyncWorkLifecycle.publishIntermediateResults(Arrays.asList("A", "B"));
        verify(consumer).accept(Arrays.asList("A", "B"));
    }

    @Test
    void done() {
        Consumer<String> consumer = mock(Consumer.class);

        FuncTaskListener<String, String> funcAsyncWorkLifecycle = new FuncTaskListener.Builder<String, String>()
                .setDoneConsumer(consumer).build();

        funcAsyncWorkLifecycle.done("A");
        verify(consumer).accept("A");
    }

    @Test
    void failed() {
        Consumer<ExecutionException> consumer = mock(Consumer.class);

        FuncTaskListener<String, String> funcAsyncWorkLifecycle = new FuncTaskListener.Builder<String, String>()
                .setFailedConsumer(consumer).build();

        ExecutionException executionException = new ExecutionException(new RuntimeException());
        funcAsyncWorkLifecycle.failed(executionException);
        verify(consumer).accept(executionException);
    }

    @Test
    void interrupted() {
        Consumer<InterruptedException> consumer = mock(Consumer.class);

        FuncTaskListener<String, String> funcAsyncWorkLifecycle = new FuncTaskListener.Builder<String, String>()
                .setInterruptedConsumer(consumer).build();

        InterruptedException interruptedException = new InterruptedException();
        funcAsyncWorkLifecycle.interrupted(interruptedException);
        verify(consumer).accept(interruptedException);
    }
}