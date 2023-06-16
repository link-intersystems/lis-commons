package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.ProgressListener;
import com.link_intersystems.util.concurrent.task.Task;
import com.link_intersystems.util.concurrent.task.TaskExecutor;
import com.link_intersystems.util.concurrent.task.TaskListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkerActionTest {

    protected GenericTaskAction<String, String> defaultAsyncAction;
    protected TaskExecutor taskExecutor;
    protected TaskActionListener<String, String> taskActionListener;

    @BeforeEach
    void setUp() {
        defaultAsyncAction = createAsyncAction();

        taskExecutor = mock(TaskExecutor.class);
        defaultAsyncAction.setTaskExecutor(taskExecutor);

        taskActionListener = mock(TaskActionListener.class);
        defaultAsyncAction.setTaskActionListener(taskActionListener);
    }

    protected GenericTaskAction<String, String> createAsyncAction() {
        Runnable asycWork = mock(Runnable.class);
        GenericTaskAction<String, String> workerAction = new GenericTaskAction<>();
        workerAction.setTask(asycWork);
        return workerAction;
    }

    @Test
    void enablementOnSuccess() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                TaskListener taskListener = invocation.getArgument(1, TaskListener.class);
                taskListener.done("");
                return null;
            }
        }).when(taskExecutor).execute(any(Task.class), any(TaskListener.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

    @Test
    void enablementOnFailed() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                TaskListener taskListener = invocation.getArgument(1, TaskListener.class);
                taskListener.failed(null);
                return null;
            }
        }).when(taskExecutor).execute(any(Task.class), any(TaskListener.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

    @Test
    void enablementOnInterrupted() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(defaultAsyncAction.isEnabled());
                TaskListener taskListener = invocation.getArgument(1, TaskListener.class);
                taskListener.interrupted(null);
                return null;
            }
        }).when(taskExecutor).execute(any(Task.class), any(TaskListener.class), any(ProgressListener.class));

        defaultAsyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(defaultAsyncAction.isEnabled());
    }

}