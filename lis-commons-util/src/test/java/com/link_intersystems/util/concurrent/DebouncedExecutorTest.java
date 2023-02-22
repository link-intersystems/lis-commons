package com.link_intersystems.util.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DebouncedExecutorTest {

    long endTime = 0;
    private Runnable runnable;
    private Semaphore semaphore;
    private DebouncedExecutor debouncedExecutor;

    @BeforeEach
    void setUp() {
        runnable = mock(Runnable.class);

        semaphore = new Semaphore(0);

        debouncedExecutor = new DebouncedExecutor(new Executor() {
            @Override
            public void execute(Runnable command) {
                endTime = System.currentTimeMillis();
                command.run();
                semaphore.release();
            }
        });

        endTime = 0;
    }

    @Test
    void executeWithDelay() throws InterruptedException {
        debouncedExecutor.setDelay(50, TimeUnit.MILLISECONDS);

        long startTime = System.currentTimeMillis();

        debouncedExecutor.execute(runnable);
        debouncedExecutor.execute(runnable);
        debouncedExecutor.execute(runnable);
        debouncedExecutor.execute(runnable);

        semaphore.acquire();

        long duration = endTime - startTime;

        assertTrue(duration >= 50, duration + " >= 50");
        assertTrue(duration <= 80, duration + " <= 80");

        verify(runnable, times(1)).run();
    }

    @Test
    void executeNoDelay() throws InterruptedException {
        debouncedExecutor.setDelay(0, TimeUnit.MILLISECONDS);

        long startTime = System.currentTimeMillis();
        debouncedExecutor.execute(runnable);
        semaphore.acquire();

        long duration = endTime - startTime;

        assertTrue(duration >= 0, duration + " >= 0");
        assertTrue(duration <= 10, duration + " <= 10");

        verify(runnable, times(1)).run();
    }
}