package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;
import com.link_intersystems.swing.ProgressListenerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public abstract class AbstractTaskAction<T, V> extends AbstractAction {

    protected TaskExecutor taskExecutor = new SwingWorkerTaskExecutor();
    private ProgressListenerFactory progressListenerFactory = () -> ProgressListener.nullInstance();
    private ActionInterceptor actionInterceptor = new EnablementActionInterceptor();

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

    public void setActionInterceptor(ActionInterceptor actionInterceptor) {
        this.actionInterceptor = requireNonNull(actionInterceptor);
    }

    public ActionInterceptor getActionInterceptor() {
        return actionInterceptor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            SwingUtilities.invokeAndWait(() -> executeActionPerformed(e));
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            Throwable targetException = ex.getTargetException();
            failed(new ExecutionException(targetException));
        }
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
            public void proceed(Runnable runnable) {
                proceed(e, runnable);
            }

            @Override
            public void proceed(ActionEvent actionEvent, Runnable finallyRunnable) {
                TaskResultHandler<T, V> resultHandler = getTaskResultHandler();
                performAction(actionEvent, new FinallyTaskResultHandlerDecorator<>(resultHandler, finallyRunnable));
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
            prepareExecution();
            tryActionPerformed(resultHandler);
        } catch (Exception ex) {
            resultHandler.failed(new ExecutionException(ex));
        }
    }

    private void tryActionPerformed(TaskResultHandler<T, V> resultHandler) throws Exception {
        ProgressListener progressListener = progressListenerFactory.createProgressListener();
        taskExecutor.execute(this::doInBackground, resultHandler, progressListener);
    }

    private TaskResultHandler<T, V> getTaskResultHandler() {
        return new TaskResultHandler<T, V>() {

            @Override
            public void publishIntermediateResults(List<V> chunks) {
                AbstractTaskAction.this.publishIntermediateResults(chunks);
            }

            @Override
            public void done(T result) {
                setEnabled(true);
                AbstractTaskAction.this.done(result);
                doFinally();
            }

            @Override
            public void failed(ExecutionException e) {
                setEnabled(true);
                AbstractTaskAction.this.failed(e);
                doFinally();
            }

            @Override
            public void interrupted(InterruptedException e) {
                setEnabled(true);
                AbstractTaskAction.this.interrupted(e);
                doFinally();
            }
        };
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

    protected void failed(ExecutionException e) {
        TaskResultHandler.defaultFailed(e);
    }

    protected void interrupted(InterruptedException e) {
    }

    protected void doFinally() {
    }

}
