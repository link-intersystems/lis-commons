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

class AbstractWorkerActionTest {

    @Test
    void publishIntermediateResults() {
        Consumer<List<Object>> consumer = mock(Consumer.class);

        AbstractWorkerAction abstractWorkerAction = new AbstractWorkerAction<Object, Object>() {
            @Override
            protected Object doInBackground(BackgroundProgress backgroundProgress) throws Exception {
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

        abstractWorkerAction.setBackgroundWorkExecutor(new DirectBackgroundWorkExecutor());

        abstractWorkerAction.actionPerformed(new ActionEvent(this, 1, ""));

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(consumer).accept(listArgumentCaptor.capture());

        assertEquals(Arrays.asList("A"), listArgumentCaptor.getValue());
    }

    @Test
    void nullProgressListener() {
        AbstractWorkerAction abstractWorkerAction = new AbstractWorkerAction<Object, Object>() {
            @Override
            protected Object doInBackground(BackgroundProgress backgroundProgress) throws Exception {
                backgroundProgress.begin("A", 2);
                backgroundProgress.worked(2);
                return null;
            }

            @Override
            protected void done(Object result) {
            }

        };
        abstractWorkerAction.setBackgroundWorkExecutor(new DirectBackgroundWorkExecutor());

        abstractWorkerAction.actionPerformed(new ActionEvent(this, 1, ""));
    }


    @Test
    void progressListener() {
        ProgressListener progressListener = mock(ProgressListener.class);

        AbstractWorkerAction abstractWorkerAction = new AbstractWorkerAction<Object, Object>() {
            @Override
            protected Object doInBackground(BackgroundProgress backgroundProgress) throws Exception {
                backgroundProgress.begin("A", 2);
                backgroundProgress.worked(2);
                return null;
            }

            @Override
            protected void done(Object result) {
            }

        };
        abstractWorkerAction.setProgressListenerFactory(() -> progressListener);
        abstractWorkerAction.setBackgroundWorkExecutor(new DirectBackgroundWorkExecutor());

        abstractWorkerAction.actionPerformed(new ActionEvent(this, 1, ""));

        verify(progressListener).begin("A", 2);
        verify(progressListener).worked(2);
    }


    @Test
    void enableOnException() {
        Consumer<ExecutionException> consumer = mock(Consumer.class);
        ProgressListener progressListener = mock(ProgressListener.class);

        AbstractWorkerAction abstractWorkerAction = new AbstractWorkerAction<Object, Object>() {
            @Override
            protected Object doInBackground(BackgroundProgress backgroundProgress) throws Exception {
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

        assertTrue(abstractWorkerAction.isEnabled());

        RuntimeException runtimeException = new RuntimeException();

        abstractWorkerAction.setProgressListenerFactory(() -> progressListener);
        abstractWorkerAction.setBackgroundWorkExecutor(new BackgroundWorkExecutor() {
            @Override
            public <T, V> void execute(BackgroundWork<T, V> backgroundWork, BackgroundWorkResultHandler<T, V> resultHandler, ProgressListener progressListener) {
                assertFalse(abstractWorkerAction.isEnabled());
                throw runtimeException;
            }
        });

        abstractWorkerAction.actionPerformed(new ActionEvent(this, 1, ""));

        assertTrue(abstractWorkerAction.isEnabled());

        ArgumentCaptor<ExecutionException> exceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(consumer).accept(exceptionArgumentCaptor.capture());

        assertEquals(runtimeException, exceptionArgumentCaptor.getValue().getCause());
    }
}