package com.link_intersystems.swing.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultWorkerActionTest {

    private DefaultWorkerAction<Object, Object> defaultWorkerAction;
    private ActionEvent actionEvent;
    private BackgroundWork<Object, Object> backgroundWork;
    private BackgroundWorkResultHandler<Object, Object> resultHandler;
    private Object backgroundWorkResult;
    private Object intermedateResult;
    private Semaphore resultSemaphore;
    private ArgumentCaptor<Object> argCaptor;

    @BeforeEach
    void setUp() throws Exception {
        backgroundWorkResult = null;
        intermedateResult = null;

        defaultWorkerAction = new DefaultWorkerAction<>();

        actionEvent = new ActionEvent(this, 1, "");

        backgroundWork = mock(BackgroundWork.class);
        defaultWorkerAction.setBackgroundWork(backgroundWork);
        resultHandler = mock(BackgroundWorkResultHandler.class);
        defaultWorkerAction.setBackgroundWorkResultHandler(resultHandler);

        resultSemaphore = new Semaphore(0);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (intermedateResult != null) {
                    BackgroundProgress backgroundProgress = invocation.getArgument(0, BackgroundProgress.class);
                    backgroundProgress.publish(intermedateResult);
                }
                if (backgroundWorkResult instanceof Exception) {
                    throw (Exception) backgroundWorkResult;
                }
                return backgroundWorkResult;
            }
        }).when(backgroundWork).execute(any(BackgroundProgress.class));
        Answer resultSemaphoreRelease = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                resultSemaphore.release();
                return null;
            }
        };

        doAnswer(resultSemaphoreRelease).when(resultHandler).done(any());
        doAnswer(resultSemaphoreRelease).when(resultHandler).failed(any(ExecutionException.class));
        doAnswer(resultSemaphoreRelease).when(resultHandler).interrupted(any(InterruptedException.class));

        argCaptor = ArgumentCaptor.forClass(Object.class);
    }

    private void performActionAndWait() throws InterruptedException {
        defaultWorkerAction.actionPerformed(actionEvent);
        resultSemaphore.acquire();
    }

    @Test
    void publishIntermediateResults() throws InterruptedException {
        intermedateResult = "B";

        performActionAndWait();

        ArgumentCaptor<List> intermediateResultCaptor = ArgumentCaptor.forClass(List.class);
        verify(resultHandler).publishIntermediateResults(intermediateResultCaptor.capture());

        assertEquals(Arrays.asList("B"), intermediateResultCaptor.getValue());
    }

    @Test
    void done() throws InterruptedException {
        backgroundWorkResult = "A";

        performActionAndWait();

        verify(resultHandler).done(argCaptor.capture());
        assertEquals("A", argCaptor.getValue());
    }

    @Test
    void failed() throws InterruptedException {
        RuntimeException re = new RuntimeException();
        backgroundWorkResult = re;

        performActionAndWait();

        ArgumentCaptor<ExecutionException> exceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(resultHandler).failed(exceptionArgumentCaptor.capture());

        assertEquals(re, exceptionArgumentCaptor.getValue().getCause());
    }

    @Test
    void actionWithNameAndIcon() {
        Icon icon = mock(Icon.class);
        DefaultWorkerAction<Object, Object> workerAction = new DefaultWorkerAction<>("name", icon);

        assertEquals("name", workerAction.getValue(Action.NAME));
        assertEquals(icon, workerAction.getValue(Action.SMALL_ICON));
    }

    @Test
    void actionWithName() {
        Icon icon = mock(Icon.class);
        DefaultWorkerAction<Object, Object> workerAction = new DefaultWorkerAction<>("name");

        assertEquals("name", workerAction.getValue(Action.NAME));
    }
}

