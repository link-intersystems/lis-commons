package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.task.Task;
import com.link_intersystems.util.concurrent.task.TaskProgress;
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

class GenericTaskActionTest {

    private GenericTaskAction<Object, Object> defaultWorkerAction;
    private ActionEvent actionEvent;
    private TaskActionListener<Object, Object> taskActionListener;
    private Object backgroundWorkResult;
    private Object intermedateResult;
    private Semaphore resultSemaphore;
    private ArgumentCaptor<Object> argCaptor;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        backgroundWorkResult = null;
        intermedateResult = null;

        defaultWorkerAction = new GenericTaskAction<>();

        actionEvent = new ActionEvent(this, 1, "");

        Task<Object, Object> task = mock(Task.class);
        defaultWorkerAction.setTask(task);
        taskActionListener = mock(TaskActionListener.class);
        defaultWorkerAction.setTaskActionListener(taskActionListener);

        resultSemaphore = new Semaphore(0);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (intermedateResult != null) {
                    TaskProgress taskProgress = invocation.getArgument(0, TaskProgress.class);
                    taskProgress.publish(intermedateResult);
                }
                if (backgroundWorkResult instanceof Exception) {
                    throw (Exception) backgroundWorkResult;
                }
                return backgroundWorkResult;
            }
        }).when(task).execute(any(TaskProgress.class));
        Answer resultSemaphoreRelease = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                resultSemaphore.release();
                return null;
            }
        };

        doAnswer(resultSemaphoreRelease).when(taskActionListener).done(any());
        doAnswer(resultSemaphoreRelease).when(taskActionListener).failed(any(ExecutionException.class));
        doAnswer(resultSemaphoreRelease).when(taskActionListener).interrupted(any(InterruptedException.class));

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
        verify(taskActionListener).publishIntermediateResults(intermediateResultCaptor.capture());

        assertEquals(Arrays.asList("B"), intermediateResultCaptor.getValue());
    }

    @Test
    void done() throws InterruptedException {
        backgroundWorkResult = "A";

        performActionAndWait();

        verify(taskActionListener).done(argCaptor.capture());
        assertEquals("A", argCaptor.getValue());
    }

    @Test
    void failed() throws InterruptedException {
        RuntimeException re = new RuntimeException();
        backgroundWorkResult = re;

        performActionAndWait();

        ArgumentCaptor<ExecutionException> exceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(taskActionListener).failed(exceptionArgumentCaptor.capture());

        assertEquals(re, exceptionArgumentCaptor.getValue().getCause());
    }

    @Test
    void actionWithNameAndIcon() {
        Icon icon = mock(Icon.class);
        GenericTaskAction<Object, Object> workerAction = new GenericTaskAction<>("name", icon);

        assertEquals("name", workerAction.getValue(Action.NAME));
        assertEquals(icon, workerAction.getValue(Action.SMALL_ICON));
    }

    @Test
    void actionWithName() {
        Icon icon = mock(Icon.class);
        GenericTaskAction<Object, Object> workerAction = new GenericTaskAction<>("name");

        assertEquals("name", workerAction.getValue(Action.NAME));
    }
}

