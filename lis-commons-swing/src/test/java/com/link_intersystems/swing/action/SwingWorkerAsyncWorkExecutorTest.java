package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class SwingWorkerAsyncWorkExecutorTest {

    public static final int TIMEOUT_SECONDS = 1;
    private SwingWorkerAsyncWorkExecutor asyncWorkExecutor;
    private AsyncWorkMock asyncWork;
    private AsyncWorkLifecycleMock lifecycle;
    private ProgressListener progressListener;

    private static class AsyncWorkMock implements AsycWork<String, String> {

        private CountDownLatch beginLatch = new CountDownLatch(1);
        private CountDownLatch doneLatch = new CountDownLatch(1);
        private Semaphore iterationBegin = new Semaphore(1);
        private Semaphore iterationEnd = new Semaphore(1);
        private Exception throwException;

        @Override
        public synchronized String execute(AsyncProgress<String> asyncProgress) throws Exception {
            asyncProgress.begin(2);

            beginLatch.countDown();
            iterationBegin.acquire();
            iterationEnd.acquire();

            if (throwException != null) {
                throw throwException;
            } else {
                for (int i = 0; i < 2; i++) {
                    iterationBegin.tryAcquire(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    asyncProgress.worked(1);
                    asyncProgress.publish(Integer.toString(i));
                    iterationEnd.release();
                }
            }

            doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return "done";
        }

        public void nextWork() throws InterruptedException {
            iterationBegin.release();
            iterationEnd.tryAcquire(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }

        public void setDone() {
            doneLatch.countDown();
        }

        public void awaitBegin() throws InterruptedException {
            beginLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }

        public void setThrowException(Exception throwException) {
            this.throwException = throwException;
        }

    }

    private static class AsyncWorkLifecycleMock implements AsycWorkLifecycle<String, String> {

        private CountDownLatch doneLatch = new CountDownLatch(1);
        private CountDownLatch failedLatch = new CountDownLatch(1);
        private CountDownLatch interruptLatch = new CountDownLatch(1);

        private ExecutionException executionException;
        private InterruptedException interruptedException;

        @Override
        public void done(String result) {
            doneLatch.countDown();
        }

        @Override
        public void failed(ExecutionException e) {
            this.executionException = e;
            failedLatch.countDown();
        }

        @Override
        public void interrupted(InterruptedException e) {
            this.interruptedException = e;
            interruptLatch.countDown();
        }

        public void awaitFailed() throws InterruptedException {
            failedLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }

        public void awaitDone() throws InterruptedException {
            doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }

        public void awaitInterrupt() throws InterruptedException {
            interruptLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        asyncWorkExecutor = new SwingWorkerAsyncWorkExecutor();

        asyncWork = new AsyncWorkMock();
        lifecycle = new AsyncWorkLifecycleMock();
        progressListener = mock(ProgressListener.class);

    }

    @Test
    @Timeout(value = 1)
    void done() throws InterruptedException {
        asyncWorkExecutor.execute(asyncWork, lifecycle, progressListener);

        asyncWork.awaitBegin();
        verify(progressListener).begin(2);

        asyncWork.nextWork();
        verify(progressListener, times(1)).worked(1);
        asyncWork.nextWork();
        verify(progressListener, times(2)).worked(1);

        asyncWork.setDone();
        lifecycle.awaitDone();
    }

    @Test
    @Timeout(value = 1)
    void failed() throws InterruptedException {
        RuntimeException throwException = new RuntimeException();
        asyncWork.setThrowException(throwException);

        asyncWorkExecutor.execute(asyncWork, lifecycle, progressListener);

        asyncWork.awaitBegin();
        verify(progressListener).begin(2);

        lifecycle.awaitFailed();
        Assertions.assertSame(throwException, lifecycle.executionException.getCause());
    }
}
