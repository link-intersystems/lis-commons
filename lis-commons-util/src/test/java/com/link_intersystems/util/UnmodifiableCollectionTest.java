/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class UnmodifiableCollectionTest {

    protected abstract Object createComponentObject();

    protected abstract Collection<Object> getUnmodifiableCollection();

    protected Collection<Object> createComponentObjects() {
        Collection<Object> collection = new ArrayList<Object>();
        for (int i = 0; i < 2; i++) {
            collection.add(createComponentObject());
        }
        return collection;
    }

    protected abstract Collection<Object> getCollectionObjects();

    protected abstract Object getCollectionObject();

    @Test
    void contains() {
        getUnmodifiableCollection().contains(createComponentObject());
    }

    @Test
    void containsAll() {
        getUnmodifiableCollection().containsAll(createComponentObjects());
    }

    @Test
    void isEmpty() {
        getUnmodifiableCollection().isEmpty();
    }

    @Test
    void iterator() {
        getUnmodifiableCollection().iterator();
    }

    @Test
    void size() {
        getUnmodifiableCollection().size();
    }

    @Test
    void toArray() {
        getUnmodifiableCollection().toArray();
    }

    @Test
    void toTypedArray() {
        getUnmodifiableCollection().toArray(new Object[getUnmodifiableCollection().size()]);
    }

    @Test
    void add() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableCollection().add(createComponentObject()));
    }

    @Test
    void addAll() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableCollection().addAll(createComponentObjects()));
    }

    @Test
    void clear() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableCollection().clear());
    }

    @Test
    void remove() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableCollection().remove(getCollectionObject()));
    }

    @Test
    void removeAll() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableCollection().removeAll(getCollectionObjects()));
    }

    @Test
    void retainAll() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableCollection().retainAll(getCollectionObjects()));
    }

}
