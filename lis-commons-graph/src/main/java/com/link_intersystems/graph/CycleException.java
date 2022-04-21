package com.link_intersystems.graph;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class CycleException extends RuntimeException {

    private Object cycleCause;

    public CycleException(Object cycleCause) {
        this.cycleCause = cycleCause;
    }

    public Object getCycleCause() {
        return cycleCause;
    }
}
