package com.link_intersystems.swing.action;

public interface AsycWork<T, V> {

    public T execute(AsyncProgress<V> asyncProgress) throws Exception;

}
