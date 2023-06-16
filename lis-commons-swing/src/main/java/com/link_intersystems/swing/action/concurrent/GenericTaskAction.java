package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.task.Task;
import com.link_intersystems.util.concurrent.task.TaskProgress;

import javax.swing.*;
import java.util.concurrent.Callable;

import static java.util.Objects.*;

public class GenericTaskAction<T, V> extends DefaultTaskAction<T, V> {

    private Task<T, V> task;

    public GenericTaskAction() {
    }

    public GenericTaskAction(String name) {
        super(name);
    }

    public GenericTaskAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setTask(Runnable asyncTask) {
        setTask(() -> {
            asyncTask.run();
            return null;
        });
    }

    public void setTask(Callable<T> asyncTask) {
        setTask((pl) -> asyncTask.call());
    }

    public void setTask(Task<T, V> asyncTask) {
        this.task = requireNonNull(asyncTask);
    }

    @Override
    protected T doInBackground(TaskProgress<V> taskProgress) throws Exception {
        return task.execute(taskProgress);
    }

}
