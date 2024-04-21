package com.link_intersystems.io.progress;

import java.util.EventListener;

/**
 * A listener interface for receiving progress events.
 *
 * <p>
 * The class that is interested in processing progress events should implement this interface.
 * The listener object is registered with a component using the component's <code>addProgressListener</code> method.
 * When the progress is changed, the <code>progressChanged</code> method is invoked.
 * </p>
 *
 * <p>
 * The <code>ProgressListener</code> interface extends the <code>EventListener</code> interface. Therefore, all
 * implementations of <code>ProgressListener</code> are also implementations of <code>EventListener</code>.
 * </p>
 *
 * <p>
 * The <code>ProgressListener</code> interface declares a single method <code>progressChanged</code> that
 * is called when the progress is changed. Implementations should define the behavior for handling the
 * progress event inside this method.
 * </p>
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ProgressListener extends EventListener {
    ProgressListener NULL = pe -> {
    };

    /**
     * Returns a null-safe version of the provided ProgressListener.
     *
     * @param progressListener the ProgressListener to make null-safe
     * @return a <code>ProgressListener</code> instance
     * * that acts as a no-op. This is useful for situations where a <code>ProgressListener</code> may be null,
     * * and it is necessary to ensure that a valid listener is always provided.
     */
    static ProgressListener nullSafe(ProgressListener progressListener) {
        return progressListener == null ? NULL : progressListener;
    }

    /**
     * Notifies the listener that the progress has changed.
     *
     * @param progressEvent The progress event containing the min, max, and current value of the progress.
     */
    void progressChanged(ProgressEvent progressEvent);
}