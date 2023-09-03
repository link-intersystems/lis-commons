package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.swing.action.ActionTrigger;
import com.link_intersystems.util.concurrent.task.TaskProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DefaultTaskActionTest {

    private DefaultTaskAction<String, Integer> defaultTaskAction;
    private TaskActionListener<String, Integer> taskActionListener;

    @BeforeEach
    void setUp() {
        defaultTaskAction = Mockito.spy(new DefaultTaskAction<String, Integer>() {
            @Override
            protected String doInBackground(TaskProgress taskProgress) throws Exception {
                taskProgress.publish(1, 2, 3);
                return "A";
            }
        });
        defaultTaskAction.setTaskExecutor(new DirectTaskExecutor());

        taskActionListener = mock(TaskActionListener.class);
        defaultTaskAction.setTaskActionListener(taskActionListener);
        when(defaultTaskAction.isEnabled()).thenReturn(true);
    }

    @Test
    void execute() throws Exception {
        ActionTrigger.performAction(this, defaultTaskAction);

        verify(defaultTaskAction).doInBackground(any(TaskProgress.class));
        verify(taskActionListener).aboutToRun();
        verify(taskActionListener).publishIntermediateResults(Arrays.asList(1, 2, 3));
        verify(taskActionListener).done("A");
        verify(taskActionListener).doFinally();
    }

    @Test
    void failed() throws Exception {
        RuntimeException runtimeException = new RuntimeException();
        doThrow(runtimeException).when(defaultTaskAction).doInBackground(any(TaskProgress.class));

        ActionTrigger.performAction(this, defaultTaskAction);

        verify(taskActionListener).aboutToRun();

        ArgumentCaptor<ExecutionException> executionExceptionCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        verify(taskActionListener).failed(executionExceptionCaptor.capture());
        assertEquals(runtimeException, executionExceptionCaptor.getValue().getCause());

        verify(taskActionListener).doFinally();
    }

}