package com.link_intersystems.swing.action.concurrent;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class DefaultTaskAction<T, V> extends AbstractTaskAction<T, V> {

    private Optional<TaskActionListener<T, V>> taskActionListener = Optional.empty();

    public DefaultTaskAction() {
    }

    public DefaultTaskAction(String name) {
        super(name);
    }

    public DefaultTaskAction(String name, Icon icon) {
        super(name, icon);
    }


    public void setTaskActionListener(TaskActionListener<T, V> taskActionListener) {
        this.taskActionListener = Optional.ofNullable(taskActionListener);
    }

    @Override
    protected void prepareExecution() throws Exception {
        taskActionListener.ifPresent(TaskActionListener::aboutToRun);
    }

    @Override
    protected void done(T result) {
        taskActionListener.ifPresent(c -> c.done(result));
    }

    @Override
    protected void publishIntermediateResults(List<V> chunks) {
        taskActionListener.ifPresent(c -> c.publishIntermediateResults(chunks));
    }

    @Override
    protected void failed(ExecutionException e) {
        taskActionListener.ifPresent(c -> c.failed(e));
    }

    @Override
    protected void aborted(Exception e) {
        taskActionListener.ifPresent(c -> c.aborted(e));
    }

    @Override
    protected void interrupted(InterruptedException e) {
        taskActionListener.ifPresent(c -> c.interrupted(e));
    }

    @Override
    protected void doFinally() {
        taskActionListener.ifPresent(TaskActionListener::doFinally);
    }
}
