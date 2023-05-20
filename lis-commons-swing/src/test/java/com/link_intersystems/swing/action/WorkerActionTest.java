package com.link_intersystems.swing.action;

import com.link_intersystems.swing.progress.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkerActionTest {

    protected DefaultTaskAction<String, String> defaultAsyncAction;
    protected TaskExecutor taskExecutor;
    protected TaskResultHandler<String, String> lifecycle;

    @BeforeEach
    void setUp() {
        defaultAsyncAction = createAsyncAction();

        taskExecutor = mock(TaskExecutor.class);
        defaultAsyncAction.setTaskExecutor(taskExecutor);

        lifecycle = mock(TaskResultHandler.class);
        defaultAsyncAction.setBackgroundWorkResultHandler(lifecycle);
    }

    protected DefaultTaskAction<String, String> createAsyncAction() {
        Runnable asycWork = mock(Runnable.class);
        DefaultTaskAction<String, String> workerAction = new DefaultTaskAction<>();
        workerAction.setBackgroundWork(asycWork);
        return workerAction;
    }

    @Test
    void enablementOnSuccess() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                TaskResultHandler taskResultHandler = invocation.getArgument(1, TaskResultHandler.class);
                taskResultHandler.done("");
                return null;
            }
        }).when(taskExecutor).execute(any(Task.class), any(TaskResultHandler.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

    @Test
    void enablementOnFailed() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                TaskResultHandler taskResultHandler = invocation.getArgument(1, TaskResultHandler.class);
                taskResultHandler.failed(null);
                return null;
            }
        }).when(taskExecutor).execute(any(Task.class), any(TaskResultHandler.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

    @Test
    void enablementOnInterrupted() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                TaskResultHandler taskResultHandler = invocation.getArgument(1, TaskResultHandler.class);
                taskResultHandler.interrupted(null);
                return null;
            }
        }).when(taskExecutor).execute(any(Task.class), any(TaskResultHandler.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

}