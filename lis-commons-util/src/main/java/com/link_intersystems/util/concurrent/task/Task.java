package com.link_intersystems.util.concurrent.task;

public interface Task<T, V> {

    public T execute(TaskProgress<V> taskProgress) throws Exception;

}
