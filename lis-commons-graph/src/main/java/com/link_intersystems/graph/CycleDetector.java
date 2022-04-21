package com.link_intersystems.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class CycleDetector<T> implements Consumer<T> {

    protected static class ElementIdentity {
        private int identityHashCode;
        private Object element;

        ElementIdentity(Object element) {
            identityHashCode = System.identityHashCode(element);
            this.element = element;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElementIdentity that = (ElementIdentity) o;
            return identityHashCode == that.identityHashCode && Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(identityHashCode, element);
        }
    }

    private Collection<Object> acceptedElements = new HashSet<>();

    @Override
    public void accept(T t) {
        Object identityObject = getIdentityObject(t);
        if (!acceptedElements.add(identityObject)) {
            throw createCycleException(t);
        }
    }

    protected CycleException createCycleException(T cycleCause) {
        return new CycleException(cycleCause);
    }

    protected Object getIdentityObject(T element){
        return element;
    }
}
