package com.link_intersystems.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class FuncAsyncWorkLifecycle<T, V> implements AsycWorkLifecycle<T, V> {
    public static class Builder<T, V> {
        private Optional<Runnable> prepareForExecution = Optional.empty();
        private Optional<Consumer<T>> doneConsumer = Optional.empty();
        private Optional<Consumer<List<V>>> intermediateResultsConsumer = Optional.empty();
        private Optional<Consumer<ExecutionException>> failedConsumer = Optional.empty();
        private Optional<Consumer<InterruptedException>> interruptedConsumer = Optional.empty();

        public Builder<T, V> setPrepareForExecution(Runnable prepareForExecution) {
            this.prepareForExecution = Optional.ofNullable(prepareForExecution);
            return this;
        }

        public Builder<T, V> setIntermediateResultsConsumer(Consumer<List<V>> intermediateResultsConsumer) {
            this.intermediateResultsConsumer = Optional.ofNullable(intermediateResultsConsumer);
            return this;
        }

        public Builder<T, V> setDoneConsumer(Consumer<T> doneConsumer) {
            this.doneConsumer = Optional.ofNullable(doneConsumer);
            return this;
        }

        public Builder<T, V> setFailedConsumer(Consumer<ExecutionException> failedConsumer) {
            this.failedConsumer = Optional.ofNullable(failedConsumer);
            return this;
        }

        public Builder<T, V> setInterruptedConsumer(Consumer<InterruptedException> interruptedConsumer) {
            this.interruptedConsumer = Optional.ofNullable(interruptedConsumer);
            return this;
        }

        public FuncAsyncWorkLifecycle build() {
            return new FuncAsyncWorkLifecycle(prepareForExecution, doneConsumer, intermediateResultsConsumer, failedConsumer, interruptedConsumer);
        }
    }

    private Optional<Runnable> prepareForExecution;
    private Optional<Consumer<T>> doneConsumer;
    private Optional<Consumer<List<V>>> intermediateResultsConsumer;
    private Optional<Consumer<ExecutionException>> failedConsumer;
    private Optional<Consumer<InterruptedException>> interruptedConsumer;

    private FuncAsyncWorkLifecycle(Optional<Runnable> prepareForExecution, Optional<Consumer<T>> doneConsumer, Optional<Consumer<List<V>>> intermediateResultsConsumer, Optional<Consumer<ExecutionException>> failedConsumer, Optional<Consumer<InterruptedException>> interruptedConsumer) {
        this.prepareForExecution = prepareForExecution;
        this.doneConsumer = doneConsumer;
        this.intermediateResultsConsumer = intermediateResultsConsumer;
        this.failedConsumer = failedConsumer;
        this.interruptedConsumer = interruptedConsumer;
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

