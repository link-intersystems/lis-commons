package com.link_intersystems.swing.action;

import java.util.concurrent.Callable;

public class SimpleAsyncAction<T> extends AsyncAction<T, T> {

    public SimpleAsyncAction(Runnable asyncWork) {
        super(asyncWork);
    }

    public SimpleAsyncAction(Callable<T> asyncWork) {
        super(asyncWork);
    }

    public SimpleAsyncAction(AsycWork<T, T> asyncWork) {
        super(asyncWork);
    }

    public SimpleAsyncAction(SimpleAsyncWork<T> asyncWork) {
        super(asyncWork);
    }
}
