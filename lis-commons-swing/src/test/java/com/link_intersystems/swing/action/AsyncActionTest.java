package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsyncActionTest {

    protected AsyncAction<String, String> asyncAction;
    protected AsyncWorkExecutor workExecutor;
    protected AsycWorkLifecycle<String, String> lifecycle;

    @BeforeEach
    void setUp() {
        asyncAction = createAsyncAction();

        workExecutor = mock(AsyncWorkExecutor.class);
        asyncAction.setAsyncWorkExecutor(workExecutor);

        lifecycle = mock(AsycWorkLifecycle.class);
        asyncAction.setAsyncWorkLifecycle(lifecycle);
    }

    protected AsyncAction<String, String> createAsyncAction() {
        Runnable asycWork = mock(Runnable.class);
        return new AsyncAction<String, String>(asycWork);
    }

    @Test
    void enablementOnSuccess() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(asyncAction.isEnabled());
                AsycWorkLifecycle asycWorkLifecycle = invocation.getArgument(1, AsycWorkLifecycle.class);
                asycWorkLifecycle.done("");
                return null;
            }
        }).when(workExecutor).execute(any(AsycWork.class), any(AsycWorkLifecycle.class), any(ProgressListener.class));

        asyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(asyncAction.isEnabled());
    }

    @Test
    void enablementOnFailed() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(asyncAction.isEnabled());
                AsycWorkLifecycle asycWorkLifecycle = invocation.getArgument(1, AsycWorkLifecycle.class);
                asycWorkLifecycle.failed(null);
                return null;
            }
        }).when(workExecutor).execute(any(AsycWork.class), any(AsycWorkLifecycle.class), any(ProgressListener.class));

        asyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(asyncAction.isEnabled());
    }

    @Test
    void enablementOnInterrupted() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(asyncAction.isEnabled());
                AsycWorkLifecycle asycWorkLifecycle = invocation.getArgument(1, AsycWorkLifecycle.class);
                asycWorkLifecycle.interrupted(null);
                return null;
            }
        }).when(workExecutor).execute(any(AsycWork.class), any(AsycWorkLifecycle.class), any(ProgressListener.class));

        asyncAction.actionPerformed(new ActionEvent(this, 1, "do"));
        assertTrue(asyncAction.isEnabled());
    }

    @Test
    void enablementOnPrepareException() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertFalse(asyncAction.isEnabled());
                throw new RuntimeException();
            }
        }).when(lifecycle).prepareForExecution();

        assertThrows(RuntimeException.class, () -> asyncAction.actionPerformed(new ActionEvent(this, 1, "do")));
        assertTrue(asyncAction.isEnabled());
    }

}