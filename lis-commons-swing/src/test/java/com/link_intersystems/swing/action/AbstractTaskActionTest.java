package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractTaskActionTest {

    @Test
    void publishIntermediateResults() {
        Consumer<List<Object>> consumer = mock(Consumer.class);

        AbstractTaskAction abstractTaskAction = new AbstractTaskAction<Object, Object>() {
            @Override
            protected Object doInBackground(TaskProgress backgroundProgress) throws Exception {
                backgroundProgress.publish("A");
                return null;
            }

            @Override
            protected void done(Object result) {
            }

            @Override
            protected void publishIntermediateResults(List<Object> chunks) {
                super.publishIntermediateResults(chunks);
                consumer.accept(chunks);
            }
        };

        abstractTaskAction.setTaskExecutor(new DirectTaskExecutor());

        abstractTaskAction.actionPerformed(new ActionEvent(this, 1, ""));

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(consumer).accept(listArgumentCaptor.capture());

        assertEquals(Arrays.asList("A"), listArgumentCaptor.getValue());
    }

    @Test
    void nullProgressListener() {
        AbstractTaskAction abstractTaskAction = new AbstractTaskAction<Object, Object>() {
            @Override
            protected Object doInBackground(TaskProgress backgroundProgress) throws Exception {
                backgroundProgress.begin("A", 2);
                backgroundProgress.worked(2);
                return null;
            }

            @Override
            protected void done(Object result) {
            }

        };
        abstractTaskAction.setTaskExecutor(new DirectTaskExecutor());

        abstractTaskAction.actionPerformed(new ActionEvent(this, 1, ""));
    }


    @Test
    void progressListener() {
        ProgressListener progressListener = mock(ProgressListener.class);

        AbstractTaskAction abstractTaskAction = new AbstractTaskAction<Object, Object>() {
            @Override
            protected Object doInBackground(TaskProgress backgroundProgress) throws Exception {
                backgroundProgress.begin("A", 2);
                backgroundProgress.worked(2);
                return null;
            }

            @Override
            protected void done(Object result) {
            }

        };
        abstractTaskAction.setProgressListenerFactory(() -> progressListener);
        abstractTaskAction.setTaskExecutor(new DirectTaskExecutor());

        abstractTaskAction.actionPerformed(new ActionEvent(this, 1, ""));

        verify(progressListener).begin("A", 2);
        verify(progressListener).worked(2);
    }


    @Test
    void enableOnException() {
        Consumer<ExecutionException> consumer = mock(Consumer.class);
        ProgressListener progressListener = mock(ProgressListener.class);

        AbstractTaskAction abstractTaskAction = new AbstractTaskAction<Object, Object>() {
            @Override
            protected Object doInBackground(TaskProgress backgroundProgress) throws Exception {
                return null;
            }

            @Override
            protected void done(Object result) {
            }

            @Override
            protected void failed(ExecutionException e) {
                consumer.accept(e);
            }
        };

        assertTrue(abstractTaskAction.isEnabled());

        RuntimeException runtimeException = new RuntimeException();

        abstractTaskAction.setProgressListenerFactory(() -> progressListener);
        abstractTaskAction.setTaskExecutor(new TaskExecutor() {
            @Override
            public <T, V> void execute(Task<T, V> task, TaskResultHandler<T, V> resultHandler, ProgressListener progressListener) {
                assertFalse(abstractTaskAction.isEnabled());
                throw runtimeException;
            }
        });

        abstractTaskAction.actionPerformed(new ActionEvent(this, 1, ""));

        assertTrue(abstractTaskAction.isEnabled());

        ArgumentCaptor<ExecutionException> exceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(consumer).accept(exceptionArgumentCaptor.capture());

        assertEquals(runtimeException, exceptionArgumentCaptor.getValue().getCause());
    }
}