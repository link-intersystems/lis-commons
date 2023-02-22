package com.link_intersystems.util.function;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.*;

class FunctionExecutorAdapterTest {

    @Test
    void exec() throws Exception {
        Executor executor = mock(Executor.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgument(0, Runnable.class).run();
                return null;
            }
        }).when(executor).execute(any(Runnable.class));
        FunctionExecutorAdapter functionExecutorAdapter = new FunctionExecutorAdapter(executor);

        Callable<Object> callable = mock(Callable.class);

        functionExecutorAdapter.exec(callable);

        verify(executor).execute(any(Runnable.class));
        verify(callable).call();
    }
}