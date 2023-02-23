package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public abstract class AbstractWorkerAction<T, V> extends AbstractAction {

    protected BackgroundWorkExecutor backgroundWorkExecutor = new SwingWorkerBackgroundWorkExecutor();
    private ProgressListener progressListener = ProgressListener.nullInstance();

    public AbstractWorkerAction() {
    }

    public AbstractWorkerAction(String name) {
        super(name);
    }

    public AbstractWorkerAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setBackgroundWorkExecutor(BackgroundWorkExecutor backgroundWorkExecutor) {
        this.backgroundWorkExecutor = requireNonNull(backgroundWorkExecutor);
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
        BackgroundWorkResultHandler<T, V> resultHandler = getBackgroundWorkResultHandler();
        backgroundWorkExecutor.execute(this::doInBackground, resultHandler, progressListener);
    }

    private BackgroundWorkResultHandler<T, V> getBackgroundWorkResultHandler() {
        return new BackgroundWorkResultHandler<T, V>() {

            @Override
            public void publishIntermediateResults(List<V> chunks) {
                AbstractWorkerAction.this.publishIntermediateResults(chunks);
            }

            @Override
            public void done(T result) {
                try {
                    AbstractWorkerAction.this.done(result);
                } finally {
                    setEnabled(true);
                }
            }

            @Override
            public void failed(ExecutionException e) {
                try {
                    AbstractWorkerAction.this.failed(e);
                } finally {
                    setEnabled(true);
                }
            }

            @Override
            public void interrupted(InterruptedException e) {
                try {
                    AbstractWorkerAction.this.interrupted(e);
                } finally {
                    setEnabled(true);
                }
            }
        };
    }

    protected abstract T doInBackground(BackgroundProgress<V> backgroundProgress) throws Exception;

    protected void publishIntermediateResults(List<V> chunks) {
    }

    protected abstract void done(T result);

    protected void failed(ExecutionException e) {
    }

    protected void interrupted(InterruptedException e) {
    }


}
