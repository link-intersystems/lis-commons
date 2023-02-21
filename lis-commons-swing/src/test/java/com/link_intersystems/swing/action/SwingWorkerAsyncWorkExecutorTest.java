package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
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

        private Semaphore step = new Semaphore(0);
        private Semaphore caller = new Semaphore(0);


        @Override
        public synchronized String execute(AsyncProgress<String> asyncProgress) throws Exception {
            nextStep(() -> {
                asyncProgress.begin(2);
            });

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

        private void nextStep(Runnable run) throws InterruptedException {
            step.acquire();
            run.run();
            caller.release();
        }

        public void continueThread() throws InterruptedException {
            step.release();
            caller.acquire();
        }

    }

    private static class AsyncWorkLifecycleMock implements AsycWorkLifecycle<String, String> {

        private CountDownLatch doneLatch = new CountDownLatch(1);

        @Override
        public void done(String result) {
            doneLatch.countDown();
        }

        public void awaitDone() throws InterruptedException {
            doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
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
