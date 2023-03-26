package com.link_intersystems.swing.action;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class DefaultTaskAction<T, V> extends AbstractTaskAction<T, V> {

    private TaskResultHandler<T, V> taskResultHandler = TaskResultHandler.nullInstance();

    private Task<T, V> task;

    public DefaultTaskAction() {
    }

    public DefaultTaskAction(String name) {
        super(name);
    }

    public DefaultTaskAction(String name, Icon icon) {
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

    public void setBackgroundWork(Task<T, V> asyncTask) {
        this.task = requireNonNull(asyncTask);
    }

    public void setBackgroundWorkResultHandler(TaskResultHandler<T, V> taskResultHandler) {
        this.taskResultHandler = requireNonNull(taskResultHandler);
    }

    @Override
    protected T doInBackground(TaskProgress<V> taskProgress) throws Exception {
        return task.execute(taskProgress);
    }

    @Override
    protected void publishIntermediateResults(List<V> chunks) {
        taskResultHandler.publishIntermediateResults(chunks);
    }

    @Override
    protected void done(T result) {
        taskResultHandler.done(result);
    }

    @Override
    public void failed(ExecutionException e) {
        taskResultHandler.failed(e);
    }

    @Override
    public void interrupted(InterruptedException e) {
        taskResultHandler.interrupted(e);
    }
}
