package com.link_intersystems.util.concurrent;

import org.junit.jupiter.api.Test;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class NullProgressListenerTest {

    @Test
    void ensureNullProgressListenerWorks() {
        NullProgressListener.INSTANCE.begin(1);
        NullProgressListener.INSTANCE.worked(1);
        NullProgressListener.INSTANCE.done();
    }
}