package com.link_intersystems.swing.action;

public interface Task<T, V> {

    public T execute(TaskProgress<V> taskProgress) throws Exception;

}
