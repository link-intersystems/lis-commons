package com.link_intersystems.swing.action;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class DefaultWorkerAction<T, V> extends AbstractWorkerAction<T, V> {

    private BackgroundWorkResultHandler<T, V> backgroundWorkResultHandler = BackgroundWorkResultHandler.nullInstance();

    private BackgroundWork<T, V> backgroundWork;

    public DefaultWorkerAction() {
    }

    public DefaultWorkerAction(String name) {
        super(name);
    }

    public DefaultWorkerAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setBackgroundWork(Runnable asyncWork) {
        setBackgroundWork(() -> {
            asyncWork.run();
            return null;
        });
    }

    public void setBackgroundWork(Callable<T> asyncWork) {
        setBackgroundWork((pl) -> asyncWork.call());
    }

    public void setBackgroundWork(BackgroundWork<T, V> asyncWork) {
        this.backgroundWork = requireNonNull(asyncWork);
    }

    public void setBackgroundWorkResultHandler(BackgroundWorkResultHandler<T, V> backgroundWorkResultHandler) {
        this.backgroundWorkResultHandler = requireNonNull(backgroundWorkResultHandler);
    }

    public void setAsyncWorkExecutor(BackgroundWorkExecutor backgroundWorkExecutor) {
        this.backgroundWorkExecutor = requireNonNull(backgroundWorkExecutor);
    }

    @Override
    protected T doInBackground(BackgroundProgress<V> backgroundProgress) throws Exception {
        return backgroundWork.execute(backgroundProgress);
    }

    @Override
    protected void publishIntermediateResults(List<V> chunks) {
        backgroundWorkResultHandler.publishIntermediateResults(chunks);
    }

    @Override
    protected void done(T result) {
        backgroundWorkResultHandler.done(result);
    }

    @Override
    public void failed(ExecutionException e) {
        backgroundWorkResultHandler.failed(e);
    }

    @Override
    public void interrupted(InterruptedException e) {
        backgroundWorkResultHandler.interrupted(e);
    }
}
