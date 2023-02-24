package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkerActionTest {

    protected DefaultWorkerAction<String, String> defaultAsyncAction;
    protected BackgroundWorkExecutor workExecutor;
    protected BackgroundWorkResultHandler<String, String> lifecycle;

    @BeforeEach
    void setUp() {
        defaultAsyncAction = createAsyncAction();

        workExecutor = mock(BackgroundWorkExecutor.class);
        defaultAsyncAction.setBackgroundWorkExecutor(workExecutor);

        lifecycle = mock(BackgroundWorkResultHandler.class);
        defaultAsyncAction.setBackgroundWorkResultHandler(lifecycle);
    }

    protected DefaultWorkerAction<String, String> createAsyncAction() {
        Runnable asycWork = mock(Runnable.class);
        DefaultWorkerAction<String, String> workerAction = new DefaultWorkerAction<>();
        workerAction.setBackgroundWork(asycWork);
        return workerAction;
    }

    @Test
    void enablementOnSuccess() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                BackgroundWorkResultHandler backgroundWorkResultHandler = invocation.getArgument(1, BackgroundWorkResultHandler.class);
                backgroundWorkResultHandler.done("");
                return null;
            }
        }).when(workExecutor).execute(any(BackgroundWork.class), any(BackgroundWorkResultHandler.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

    @Test
    void enablementOnFailed() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                BackgroundWorkResultHandler backgroundWorkResultHandler = invocation.getArgument(1, BackgroundWorkResultHandler.class);
                backgroundWorkResultHandler.failed(null);
                return null;
            }
        }).when(workExecutor).execute(any(BackgroundWork.class), any(BackgroundWorkResultHandler.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

    @Test
    void enablementOnInterrupted() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                BackgroundWorkResultHandler backgroundWorkResultHandler = invocation.getArgument(1, BackgroundWorkResultHandler.class);
                backgroundWorkResultHandler.interrupted(null);
                return null;
            }
        }).when(workExecutor).execute(any(BackgroundWork.class), any(BackgroundWorkResultHandler.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

}