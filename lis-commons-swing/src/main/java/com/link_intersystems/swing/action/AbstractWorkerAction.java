package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import com.link_intersystems.swing.ProgressListenerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public abstract class AbstractWorkerAction<T, V> extends AbstractAction {

    protected BackgroundWorkExecutor backgroundWorkExecutor = new SwingWorkerBackgroundWorkExecutor();
    private ProgressListenerFactory progressListenerFactory = () -> ProgressListener.nullInstance();

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

    public void setProgressListenerFactory(ProgressListenerFactory progressListenerFactory) {
        this.progressListenerFactory = progressListenerFactory == null ? () -> ProgressListener.nullInstance() : progressListenerFactory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setEnabled(false);

        BackgroundWorkResultHandler<T, V> resultHandler = getBackgroundWorkResultHandler();
        try {
            tryActionPerformed(resultHandler);
            setEnabled(true);
        } catch (Exception ex) {
            setEnabled(true);
            resultHandler.failed(new ExecutionException(ex));
        }
    }

    private void tryActionPerformed(BackgroundWorkResultHandler<T, V> resultHandler) throws Exception {
        ProgressListener progressListener = progressListenerFactory.createProgressListener();
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
                setEnabled(true);
                AbstractWorkerAction.this.done(result);
            }

            @Override
            public void failed(ExecutionException e) {
                setEnabled(true);
                AbstractWorkerAction.this.failed(e);
            }

            @Override
            public void interrupted(InterruptedException e) {
                setEnabled(true);
                AbstractWorkerAction.this.interrupted(e);
            }
        };
    }

    protected abstract T doInBackground(BackgroundProgress<V> backgroundProgress) throws Exception;

    protected void publishIntermediateResults(List<V> chunks) {
    }

    protected abstract void done(T result);

    protected void failed(ExecutionException e) {
        Throwable cause = e.getCause();

        if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
        }

        throw new RuntimeException(e);
    }

    protected void interrupted(InterruptedException e) {
    }


}
