package com.link_intersystems.swing.action;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class FinallyTaskResultHandlerDecorator<T, V> implements TaskResultHandler<T, V> {

    private TaskResultHandler<T, V> target;
    private Runnable finallyRunnable;

    public FinallyTaskResultHandlerDecorator(TaskResultHandler<T, V> target, Runnable finallyRunnable) {
        this.target = requireNonNull(target);
        this.finallyRunnable = finallyRunnable == null ? () -> {
        } : finallyRunnable;
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
            finallyRunnable.run();
        }
    }

    @Override
    public void failed(ExecutionException e) {
        try {
            target.failed(e);
        } finally {
            finallyRunnable.run();
        }
    }

    @Override
    public void interrupted(InterruptedException e) {
        try {
            target.interrupted(e);
        } finally {
            finallyRunnable.run();
        }
    }
}
