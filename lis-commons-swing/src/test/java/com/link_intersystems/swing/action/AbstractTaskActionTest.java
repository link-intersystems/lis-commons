package com.link_intersystems.swing.action;

import com.link_intersystems.swing.progress.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
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
    private Consumer<Exception> abortConsumer;

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
        abortConsumer = mock(Consumer.class);
        taskAction.setAbortConsumer(abortConsumer);
    }

    private ExecutionException getExecutionException() {
        ArgumentCaptor<ExecutionException> executionExceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(failedConsumer).accept(executionExceptionArgumentCaptor.capture());
        return executionExceptionArgumentCaptor.getValue();
    }

    private Exception getAbortException() {
        ArgumentCaptor<Exception> exceptionArgumentCaptor = ArgumentCaptor.forClass(Exception.class);
        verify(abortConsumer).accept(exceptionArgumentCaptor.capture());
        return exceptionArgumentCaptor.getValue();
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
            p.done();
        });

        actionTrigger.performAction(taskAction);
    }


    @Test
    void progressListener() {
        taskAction.setBackgroundConsumer(p -> {
            p.begin("A", 2);
            p.worked(2);
            p.done();
        });

        actionTrigger.performAction(taskAction);

        verify(progressListener).begin("A", 2);
        verify(progressListener).worked(2);
        verify(progressListener).done();
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
    void prepareAbortsExecution() throws Exception {
        RuntimeException runtimeException = new RuntimeException();
        taskAction.setPrepareExecution(() -> {
            throw runtimeException;
        });
        Consumer consumer = mock(Consumer.class);
        taskAction.setBackgroundConsumer(consumer);

        actionTrigger.performAction(taskAction);

        Exception abortException = getAbortException();
        assertEquals(runtimeException, abortException);
    }

    @Test
    void invokeAndWait() throws Exception {
        PrepareExecution prepareExecution = mock(PrepareExecution.class);
        taskAction.setPrepareExecution(prepareExecution);

        SwingUtilities.invokeAndWait(() -> actionTrigger.performAction(taskAction));

        verify(prepareExecution).run();
    }

    @Test
    void invokeLater() throws Exception {
        PrepareExecution prepareExecution = mock(PrepareExecution.class);
        Semaphore semaphore = new Semaphore(0);
        doAnswer(invocation -> {
            semaphore.release();
            return null;
        }).when(prepareExecution).run();
        taskAction.setPrepareExecution(prepareExecution);

        SwingUtilities.invokeLater(() -> actionTrigger.performAction(taskAction));
        semaphore.acquire();
        verify(prepareExecution).run();
    }

}