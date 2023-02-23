package com.link_intersystems.swing.action;

public interface BackgroundWork<T, V> {

    public T execute(BackgroundProgress<V> backgroundProgress) throws Exception;

}
