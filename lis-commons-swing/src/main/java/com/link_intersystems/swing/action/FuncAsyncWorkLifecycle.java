package com.link_intersystems.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class FuncAsyncWorkLifecycle<T, V> implements AsycWorkLifecycle<T, V> {

    private Optional<Runnable> prepareForExecution = Optional.empty();
    private Optional<Consumer<T>> doneConsumer = Optional.empty();
    private Optional<Consumer<List<V>>> intermediateResultsConsumer = Optional.empty();
    private Optional<Consumer<ExecutionException>> failedConsumer = Optional.empty();
    private Optional<Consumer<InterruptedException>> interruptedConsumer = Optional.empty();

    public FuncAsyncWorkLifecycle<T, V> setPrepareForExecution(Runnable prepareForExecution) {
        this.prepareForExecution = Optional.ofNullable(prepareForExecution);
        return this;
    }

    public FuncAsyncWorkLifecycle<T, V> setIntermediateResultsConsumer(Consumer<List<V>> intermediateResultsConsumer) {
        this.intermediateResultsConsumer = Optional.ofNullable(intermediateResultsConsumer);
        return this;
    }

    public FuncAsyncWorkLifecycle<T, V> setDoneConsumer(Consumer<T> doneConsumer) {
        this.doneConsumer = Optional.ofNullable(doneConsumer);
        return this;
    }

    public FuncAsyncWorkLifecycle<T, V> setFailedConsumer(Consumer<ExecutionException> failedConsumer) {
        this.failedConsumer = Optional.ofNullable(failedConsumer);
        return this;
    }

    public FuncAsyncWorkLifecycle<T, V> setInterruptedConsumer(Consumer<InterruptedException> interruptedConsumer) {
        this.interruptedConsumer = Optional.ofNullable(interruptedConsumer);
        return this;
    }

    @Override
    public void prepareForExecution() {
        prepareForExecution.ifPresent(Runnable::run);
    }

    @Override
    public void intermediateResults(List<V> chunks) {
        intermediateResultsConsumer.ifPresent(c -> c.accept(chunks));
    }

    @Override
    public void done(T result) {
        doneConsumer.ifPresent(c -> c.accept(result));
    }

    @Override
    public void failed(ExecutionException e) {
        failedConsumer.ifPresent(c -> c.accept(e));
    }

    @Override
    public void interrupted(InterruptedException e) {
        interruptedConsumer.ifPresent(c -> c.accept(e));
    }
}

