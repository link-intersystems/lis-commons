package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class AsyncAction<T, V> extends AbstractAction {

    private AsycWork<T, V> asycWork;
    private AsycWorkLifecycle<T, V> asyncWorkLifecycle = AsycWorkLifecycle.nullInstance();
    private AsyncWorkExecutor asyncWorkExecutor = new SwingWorkerAsyncWorkExecutor();
    private ProgressListener progressListener = ProgressListener.nullInstance();

    public AsyncAction(Runnable asyncWork) {
        this(() -> {
            asyncWork.run();
            return null;
        });
    }

    public AsyncAction(Callable<T> asyncWork) {
        this((pl) -> asyncWork.call());
    }

    public AsyncAction(AsycWork<T, V> asyncWork) {
        this.asycWork = requireNonNull(asyncWork);
    }

    public void setAsyncWorkExecutor(AsyncWorkExecutor asyncWorkExecutor) {
        this.asyncWorkExecutor = requireNonNull(asyncWorkExecutor);
    }

    public void setAsyncWorkLifecycle(AsycWorkLifecycle<T, V> asyncWorkLifecycle) {
        this.asyncWorkLifecycle = requireNonNull(asyncWorkLifecycle);
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener == null ? ProgressListener.nullInstance() : progressListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setEnabled(false);

        try {
            tryActionPerformed(e);
        } catch (RuntimeException re) {
            setEnabled(true);
            throw re;
        }
    }

    private void tryActionPerformed(ActionEvent e) {
        AsycWorkLifecycle<T, V> asyncWorkLifecycleAdapter = getAsyncWorkLifecycleAdapter();
        asyncWorkLifecycleAdapter.prepareForExecution();
        asyncWorkExecutor.execute(asycWork, asyncWorkLifecycleAdapter, progressListener);

    }

    protected AsycWorkLifecycle<T, V> getAsyncWorkLifecycleAdapter() {
        return new AsycWorkLifecycle<T, V>() {

            @Override
            public void prepareForExecution() {
                asyncWorkLifecycle.prepareForExecution();
            }

            @Override
            public void done(T result) {
                try {
                    asyncWorkLifecycle.done(result);
                } finally {
                    setEnabled(true);
                }
            }

            @Override
            public void failed(ExecutionException e) {
                try {
                    asyncWorkLifecycle.failed(e);
                } finally {
                    setEnabled(true);
                }
            }

            @Override
            public void interrupted(InterruptedException e) {
                try {
                    asyncWorkLifecycle.interrupted(e);
                } finally {
                    setEnabled(true);
                }
            }
        };
    }

}
