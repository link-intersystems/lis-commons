package com.link_intersystems.swing.action;

import com.link_intersystems.swing.progress.ProgressListener;
import com.link_intersystems.swing.progress.ProgressListenerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static java.util.Objects.*;

public abstract class AbstractTaskAction<T, V> extends AbstractAction {

    protected TaskExecutor taskExecutor = new SwingWorkerTaskExecutor();
    private ProgressListenerFactory progressListenerFactory = () -> ProgressListener.nullInstance();
    private ActionInterceptor actionInterceptor = new EnablementActionInterceptor();
    private Executor actionPerformExecutor = new EventDispatchThreadExecutor();

    public AbstractTaskAction() {
    }

    public AbstractTaskAction(String name) {
        super(name);
    }

    public AbstractTaskAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * Set a {@link TaskExecutor} that is responsible for running {@link #doInBackground(TaskProgress)} tasks.
     * The default {@link SwingWorkerTaskExecutor}  uses a {@link SwingWorker} to run the task.
     *
     * @param taskExecutor
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = requireNonNull(taskExecutor);
    }

    public void setProgressListenerFactory(ProgressListenerFactory progressListenerFactory) {
        this.progressListenerFactory = progressListenerFactory == null ? () -> ProgressListener.nullInstance() : progressListenerFactory;
    }

    /**
     * The {@link Executor} to use to perform the action. The default is the {@link EventDispatchThreadExecutor} that ensures
     * that the {@link #actionPerformed(ActionEvent)} executes on the event-dispatch-thread.
     *
     * @param actionPerformExecutor
     */
    public void setActionPerformExecutor(Executor actionPerformExecutor) {
        this.actionPerformExecutor = requireNonNull(actionPerformExecutor);
    }

    public void setActionInterceptor(ActionInterceptor actionInterceptor) {
        this.actionInterceptor = requireNonNull(actionInterceptor);
    }

    public ActionInterceptor getActionInterceptor() {
        return actionInterceptor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Runnable runnable = () -> executeActionPerformed(e);
        actionPerformExecutor.execute(runnable);
    }

    private void executeActionPerformed(ActionEvent e) {
        ActionInterceptor actionInterceptor = getActionInterceptor();

        class DefaultActionJoinPoint implements ActionJoinPoint {

            private Action action = new ActionDelegate(AbstractTaskAction.this) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    proceed(e);
                }
            };

            @Override
            public void proceed(Runnable finallyRunnable) {
                proceed(e, finallyRunnable);
            }

            @Override
            public void proceed(ActionEvent actionEvent, Runnable finallyRunnable) {
                TaskResultHandler<T, V> resultHandler = getTaskResultHandler();
                Runnable[] doFinallyRunnables = {AbstractTaskAction.this::doFinally, finallyRunnable};
                FinallyTaskResultHandlerDecorator<T, V> doFinallyDecorator = new FinallyTaskResultHandlerDecorator<>(resultHandler, doFinallyRunnables);
                performAction(actionEvent, doFinallyDecorator);
            }

            @Override
            public Action getAction() {
                return action;
            }
        }

        actionInterceptor.execute(new DefaultActionJoinPoint());
    }

    protected void performAction(ActionEvent e, TaskResultHandler<T, V> resultHandler) {
        try {
            tryActionPerformed(resultHandler);
        } catch (Exception ex) {
            resultHandler.failed(new ExecutionException(ex));
        }
    }

    private void tryActionPerformed(TaskResultHandler<T, V> resultHandler) throws Exception {
        try {
            prepareExecution();
        } catch (Exception e) {
            resultHandler.aborted(e);
            return;
        }

        ProgressListener progressListener = progressListenerFactory.createProgressListener();
        taskExecutor.execute(this::doInBackground, resultHandler, progressListener);
    }

    private TaskResultHandler<T, V> getTaskResultHandler() {
        class AbstractTaskActionResultHandlerAdapter implements TaskResultHandler<T, V> {

            private AbstractTaskAction<T, V> taskAction;

            public AbstractTaskActionResultHandlerAdapter(AbstractTaskAction<T, V> taskAction) {
                this.taskAction = taskAction;
            }

            @Override
            public void publishIntermediateResults(List<V> chunks) {
                taskAction.publishIntermediateResults(chunks);
            }

            @Override
            public void done(T result) {
                taskAction.done(result);
            }

            @Override
            public void failed(ExecutionException e) {
                taskAction.failed(e);
            }

            @Override
            public void interrupted(InterruptedException e) {
                taskAction.interrupted(e);
            }

            @Override
            public void aborted(Exception e) {
                taskAction.aborted(e);
            }
        }

        return new AbstractTaskActionResultHandlerAdapter(this);
    }

    /**
     * Prepare the {@link #doInBackground(TaskProgress)} execution in
     * the same thread the {@link #actionPerformed(ActionEvent)} method has been called with.
     * Usually the event dispatch thread.
     */
    protected void prepareExecution() throws Exception {
    }

    /**
     * Runs in the thread that the {@link TaskExecutor} defines. The
     */
    protected abstract T doInBackground(TaskProgress<V> taskProgress) throws Exception;

    protected void publishIntermediateResults(List<V> chunks) {
    }

    protected abstract void done(T result);

    /**
     * Invoked when the {@link #doInBackground(TaskProgress)} raises an exception.
     *
     * @param e
     */
    protected void failed(ExecutionException e) {
        TaskResultHandler.defaultFailed(e);
    }

    protected void interrupted(InterruptedException e) {
    }

    /**
     * Invoked when the {@link #prepareExecution()} raises an exception.
     *
     * @param e
     */
    protected void aborted(Exception e) {
    }

    protected void doFinally() {
    }

}
