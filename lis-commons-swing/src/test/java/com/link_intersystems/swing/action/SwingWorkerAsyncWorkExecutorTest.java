package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class SwingWorkerAsyncWorkExecutorTest {


    private SwingWorkerAsyncWorkExecutor asyncWorkExecutor;
    private AsyncWorkMock asyncWork;
    private AsyncWorkLifecycleMock lifecycle;
    private ProgressListener progressListener;

    public static interface RunnableWithException {

        public void run() throws Exception;
    }

    private static class AsyncWorkMock implements AsycWork<String, String> {

        private Semaphore step = new Semaphore(0);
        private Semaphore caller = new Semaphore(0);
        private Exception exception;


        @Override
        public synchronized String execute(AsyncProgress<String> asyncProgress) throws Exception {
            nextStep(() -> {
                asyncProgress.begin(2);
            });

            if (exception != null) {
                nextStep(() -> {
                    throw exception;
                });
            }

            for (int i = 0; i < 2; i++) {
                final int act = i;
                nextStep(() -> {
                    asyncProgress.worked(1);
                    asyncProgress.publish(Integer.toString(act));
                });
            }


            nextStep(() -> {
            });
            return "done";
        }

        private void nextStep(RunnableWithException run) throws Exception {
            step.acquire();
            try {
                run.run();
                caller.release();
            } catch (Exception e){
                caller.release();
                throw e;
            }
        }

        public void continueThread() throws InterruptedException {
            step.release();
            caller.acquire();
        }

        public void setThrowException(Exception exception) {
            this.exception = exception;
        }
    }

    private static class AsyncWorkLifecycleMock implements AsycWorkLifecycle<String, String> {

        private CountDownLatch doneLatch = new CountDownLatch(1);
        private CountDownLatch failedLatch = new CountDownLatch(1);
        private ExecutionException executionException;

        @Override
        public void done(String result) {
            doneLatch.countDown();
        }

        @Override
        public void failed(ExecutionException executionException) {
            this.executionException = executionException;
            failedLatch.countDown();
        }

        public void awaitFailed() throws InterruptedException {
            failedLatch.await();
        }

        public void awaitDone() throws InterruptedException {
            doneLatch.await();
        }

    }

    @BeforeEach
    void setUp() {
        asyncWorkExecutor = new SwingWorkerAsyncWorkExecutor();

        asyncWork = new AsyncWorkMock();
        lifecycle = new AsyncWorkLifecycleMock();
        progressListener = mock(ProgressListener.class);

    }

    @Test
    @Timeout(value = 1)
    void fail() throws InterruptedException {
        asyncWork.setThrowException(new RuntimeException());
        asyncWorkExecutor.execute(asyncWork, lifecycle, progressListener);

        asyncWork.continueThread();
        verify(progressListener).begin(2);

        asyncWork.continueThread();
        lifecycle.awaitFailed();
    }

    @Test
    @Timeout(value = 1)
    void done() throws InterruptedException {
        asyncWorkExecutor.execute(asyncWork, lifecycle, progressListener);

        asyncWork.continueThread();
        verify(progressListener).begin(2);

        asyncWork.continueThread();
        verify(progressListener, times(1)).worked(1);
        asyncWork.continueThread();
        verify(progressListener, times(2)).worked(1);

        asyncWork.continueThread();
        lifecycle.awaitDone();
    }

}
