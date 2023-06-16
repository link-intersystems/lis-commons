package com.link_intersystems.swing.progress;

import com.link_intersystems.util.concurrent.AbstractProgressListenerDelegate;
import com.link_intersystems.util.concurrent.ProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AbstractProgressListenerDelegateTest {

    private AbstractProgressListenerDelegate progressListenerDelegate;
    private ProgressListener delegate;
    private AbstractProgressListenerDelegate progressListenerDelegateWithoutDelegate;

    @BeforeEach
    void setUp() {
        delegate = mock(ProgressListener.class);
        progressListenerDelegate = createInstance(delegate);
        progressListenerDelegateWithoutDelegate = createInstance(null);
    }

    protected AbstractProgressListenerDelegate createInstance(ProgressListener delegate) {
        return new AbstractProgressListenerDelegate() {

            @Override
            public ProgressListener getProgressListener() {
                return delegate;
            }
        };
    }

    @Test
    void begin() {
        progressListenerDelegate.begin("A", 2);

        verify(delegate).begin("A", 2);
    }

    @Test
    void worked() {
        progressListenerDelegate.worked(2);

        verify(delegate).worked(2);
    }

    @Test
    void done() {
        progressListenerDelegate.done();

        verify(delegate).done();
    }

    @Test
    void beginWithoutDelegate() {
        progressListenerDelegate.begin("A", 2);
    }

    @Test
    void workedWithoutDelegate() {
        progressListenerDelegate.worked(2);
    }

    @Test
    void doneWithoutDelegate() {
        progressListenerDelegate.done();
    }
}