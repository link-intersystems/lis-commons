package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.task.TaskListener;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class FinallyTaskListenerDecorator<T, V> implements TaskListener<T, V> {

    private TaskListener<T, V> target;
    private Runnable[] finallyRunnables;

    public FinallyTaskListenerDecorator(TaskListener<T, V> target, Runnable... finallyRunnables) {
        this.target = requireNonNull(target);
        this.finallyRunnables = finallyRunnables;
    }

    @Override
    public void publishIntermediateResults(List<V> chunks) {
        target.publishIntermediateResults(chunks);
    }

    @Override
    public void done(T result) {
        try {
            target.done(result);
        } finally {
            runFinallyRunnables();
        }
    }

    @Override
    public void failed(ExecutionException e) {
        try {
            target.failed(e);
        } finally {
            runFinallyRunnables();
        }
    }

    @Override
    public void interrupted(InterruptedException e) {
        try {
            target.interrupted(e);
        } finally {
            runFinallyRunnables();
        }
    }

    @Override
    public void aborted(Exception e) {
        try {
            target.aborted(e);
        } finally {
            runFinallyRunnables();
        }
    }

    private void runFinallyRunnables(){
        for (Runnable finallyRunnable : finallyRunnables) {
            finallyRunnable.run();
        }
    }
}
