package com.link_intersystems.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

public class TestTaskAction<T, V> extends AbstractTaskAction<T, V> {

    private Optional<Function<TaskProgress<V>, T>> backgroundFunc = Optional.empty();
    private Optional<Consumer<List<V>>> intermediateResultsConsumer = Optional.empty();
    private Consumer<ExecutionException> failedConsumer;
    private PrepareExecution prepareExecution;
    private Optional<Consumer<T>> doneConsumer = Optional.empty();

    public void setDoneConsumer(Consumer<T> doneConsumer) {
        this.doneConsumer = Optional.ofNullable(doneConsumer);
    }

    public void setFailedConsumer(Consumer<ExecutionException> failedConsumer) {
        this.failedConsumer = failedConsumer;
    }

    public void setBackgroundConsumer(Consumer<TaskProgress<V>> backgroundFunc) {
        setBackgroundFunc(p -> {
            backgroundFunc.accept(p);
            return null;
        });
    }

    public void setBackgroundFunc(Function<TaskProgress<V>, T> backgroundFunc) {
        this.backgroundFunc = Optional.ofNullable(backgroundFunc);
    }

    public void setIntermediateResultsConsumer(Consumer<List<V>> intermediateResultsConsumer) {
        this.intermediateResultsConsumer = Optional.ofNullable(intermediateResultsConsumer);
    }

    @Override
    protected T doInBackground(TaskProgress<V> taskProgress) throws Exception {
        return backgroundFunc.map(f -> f.apply(taskProgress)).orElse(null);
    }

    @Override
    protected void done(T result) {
        doneConsumer.ifPresent(c -> c.accept(result));
    }

    @Override
    protected void publishIntermediateResults(List<V> chunks) {
        intermediateResultsConsumer.ifPresent(c -> c.accept(chunks));
    }

    @Override
    protected void failed(ExecutionException e) {
        if (failedConsumer != null) {
            failedConsumer.accept(e);
        } else {
            super.failed(e);
        }
    }

    public void setPrepareExecution(PrepareExecution prepareExecution) {

        this.prepareExecution = prepareExecution;
    }

    @Override
    protected void prepareExecution() throws Exception {
        if (prepareExecution != null) {
            prepareExecution.run();
        }
    }


}
