package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractTaskActionTest {

    private ActionTrigger actionTrigger;
    private TestTaskAction<Object, Object> taskAction;
    private Consumer<Object> doneConsumer;
    private Consumer<List<Object>> intermediateResultsConsumer;
    private ProgressListener progressListener;
    private Consumer<ExecutionException> failedConsumer;

    @BeforeEach
    void setUp() {
        actionTrigger = new ActionTrigger(this);
        taskAction = new TestTaskAction<>();
        taskAction.setTaskExecutor(new DirectTaskExecutor());
        doneConsumer = mock(Consumer.class);
        taskAction.setDoneConsumer(doneConsumer);

        intermediateResultsConsumer = mock(Consumer.class);
        taskAction.setIntermediateResultsConsumer(intermediateResultsConsumer);

        progressListener = mock(ProgressListener.class);
        taskAction.setProgressListenerFactory(() -> progressListener);

        failedConsumer = mock(Consumer.class);
        taskAction.setFailedConsumer(failedConsumer);
    }

    private ExecutionException getExecutionException() {
        ArgumentCaptor<ExecutionException> executionExceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(failedConsumer).accept(executionExceptionArgumentCaptor.capture());
        return executionExceptionArgumentCaptor.getValue();
    }

    @Test
    void done() {
        taskAction.setBackgroundFunc(p -> "A");

        actionTrigger.performAction(taskAction);

        verify(doneConsumer).accept("A");
    }

    @Test
    void publishIntermediateResults() {
        taskAction.setBackgroundConsumer(p -> p.publish("A"));

        actionTrigger.performAction(taskAction);

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(intermediateResultsConsumer).accept(listArgumentCaptor.capture());
        assertEquals(Arrays.asList("A"), listArgumentCaptor.getValue());
    }

    @Test
    void nullProgressListener() {
        taskAction.setBackgroundConsumer(p -> {
            p.begin("A", 2);
            p.worked(2);
        });

        actionTrigger.performAction(taskAction);
    }


    @Test
    void progressListener() {
        taskAction.setBackgroundConsumer(p -> {
            p.begin("A", 2);
            p.worked(2);
        });

        actionTrigger.performAction(taskAction);

        verify(progressListener).begin("A", 2);
        verify(progressListener).worked(2);
    }


    @Test
    void enableOnException() {
        assertTrue(taskAction.isEnabled());
        RuntimeException runtimeException = new RuntimeException();
        taskAction.setTaskExecutor(new TaskExecutor() {
            @Override
            public <T, V> void execute(Task<T, V> task, TaskResultHandler<T, V> resultHandler, ProgressListener progressListener) {
                assertFalse(taskAction.isEnabled());
                throw runtimeException;
            }
        });

        actionTrigger.performAction(taskAction);

        assertTrue(taskAction.isEnabled());
        assertEquals(runtimeException, getExecutionException().getCause());
    }


    @Test
    void prepareExecution() throws Exception {
        PrepareExecution prepareExecution = mock(PrepareExecution.class);
        taskAction.setPrepareExecution(prepareExecution);

        actionTrigger.performAction(taskAction);

        verify(prepareExecution).run();
    }

    @Test
    void prepareExecutionException() {
        RuntimeException runtimeException = new RuntimeException();
        taskAction.setPrepareExecution(() -> {
            throw runtimeException;
        });

        actionTrigger.performAction(taskAction);

        ExecutionException executionException = getExecutionException();
        assertNotNull(executionException);
        assertEquals(runtimeException, executionException.getCause());
    }


}