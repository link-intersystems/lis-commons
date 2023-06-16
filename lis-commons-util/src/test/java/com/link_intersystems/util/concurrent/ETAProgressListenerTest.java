package com.link_intersystems.util.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ETAProgressListenerTest {

    private ETAProgressListener etaProgressListener;
    private ProgressListener progressListener;
    private ETAChangedListener etaChangedListener;
    private ETATest etaTest;

    @BeforeEach
    void setUp() {
        etaTest = new ETATest();
        etaTest.setUp();
        progressListener = mock(ProgressListener.class);
        etaChangedListener = mock(ETAChangedListener.class);
        etaProgressListener = new ETAProgressListener(etaChangedListener, progressListener, etaTest.eta);
    }

    @Test
    void progress() {
        etaTest.returnTimesMins(1, 2, 3);

        etaProgressListener.begin("A",10);
        verify(progressListener, times(1)).begin("A",10);

        etaProgressListener.worked(1);
        assertDuration(SECONDS.convert(9, MINUTES));
        verify(progressListener, times(1)).worked(1);

        etaProgressListener.worked(1);
        assertDuration(SECONDS.convert(8, MINUTES));
        verify(progressListener, times(2)).worked(1);

        etaProgressListener.worked(1);
        assertDuration(SECONDS.convert(7, MINUTES));
        verify(progressListener, times(3)).worked(1);

        etaProgressListener.done();
        assertDuration(SECONDS.convert(0, MINUTES));
        verify(progressListener, times(1)).done();
    }

    private void assertDuration(long seconds) {
        assertDuration(Duration.ofSeconds(seconds));
    }

    private void assertDuration(Duration expectedDuration) {
        ArgumentCaptor<Supplier<Duration>> durationSupplierCaptor = ArgumentCaptor.forClass(Supplier.class);
        Mockito.verify(etaChangedListener, atLeast(1)).onWorked(durationSupplierCaptor.capture());
        Supplier<Duration> durationSupplier = durationSupplierCaptor.getValue();
        Duration duration = durationSupplier.get();
        assertEquals(expectedDuration, duration);
    }

}