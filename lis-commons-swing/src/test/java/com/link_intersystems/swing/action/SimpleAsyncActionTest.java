package com.link_intersystems.swing.action;

import org.junit.jupiter.api.Nested;

import java.util.concurrent.Callable;

import static org.mockito.Mockito.*;

class SimpleAsyncActionTest {

    @Nested
    class AsyncActionRunnable extends AsyncActionTest {

        @Override
        protected AsyncAction<String, String> createAsyncAction() {
            return new SimpleAsyncAction<>(mock(Runnable.class));
        }
    }


    @Nested
    class AsyncActionCallable extends AsyncActionTest {

        @Override
        protected AsyncAction<String, String> createAsyncAction() {
            return new SimpleAsyncAction<String>(mock(Callable.class));
        }
    }

    @Nested
    class AsyncActionAsycWork extends AsyncActionTest {

        @Override
        protected AsyncAction<String, String> createAsyncAction() {
            return new SimpleAsyncAction<>(mock(AsycWork.class));
        }
    }


    @Nested
    class AsyncActionSimpleAsycWork extends AsyncActionTest {

        @Override
        protected AsyncAction<String, String> createAsyncAction() {
            return new SimpleAsyncAction<>(mock(SimpleAsyncWork.class));
        }
    }

}