package com.link_intersystems.util.concurrent;

import java.time.Duration;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ETAProgressListener implements ProgressListener {

    private ETAChangedListener etaChangedListener;

    private ETA eta;
    private Supplier<Duration> durationSupplier;
    private ProgressListener progressListener;

    private int totalWork;
    private int worked;

    public ETAProgressListener(ETAChangedListener etaChangedListener) {
        this(etaChangedListener, NullProgressListener.INSTANCE);
    }

    public ETAProgressListener(ETAChangedListener etaChangedListener, ProgressListener progressListener) {
        this(etaChangedListener, progressListener, new ETA());
    }

    ETAProgressListener(ETAChangedListener etaChangedListener, ProgressListener progressListener, ETA eta) {
        this.etaChangedListener = requireNonNull(etaChangedListener);
        this.progressListener = requireNonNull(progressListener);
        this.eta = requireNonNull(eta);
        this.durationSupplier = eta::getRemainingDuration;
    }

    @Override
    public void begin(int totalWork) {
        this.totalWork = totalWork;
        eta.begin(totalWork);
        progressListener.begin(totalWork);
    }

    @Override
    public void worked(int work) {
        this.worked += work;
        eta.worked(work);
        etaChangedListener.onWorked(durationSupplier);
        progressListener.worked(work);
    }

    @Override
    public void done() {
        int remainingWork = totalWork - worked;
        eta.worked(remainingWork);
        progressListener.done();
    }
}
