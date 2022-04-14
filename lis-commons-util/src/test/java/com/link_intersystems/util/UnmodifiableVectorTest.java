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

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class UnmodifiableVectorTest extends UnmodifiableListTest {

    protected final List<Object> getUnmodifiableList() {
        return getUnmodifiableVector();
    }

    protected abstract Vector<Object> getUnmodifiableVector();

    @Test
    void addElement() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().addElement(createComponentObject()));
    }

    @Test
    void capacity() {
        getUnmodifiableVector().capacity();
    }

    @Test
    void elementAt() {
        getUnmodifiableVector().elementAt(0);
    }

    @Test
    void elements() {
        getUnmodifiableVector().elements();
    }

    @Test
    void ensureCapacity() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().ensureCapacity(1024));
    }

    @Test
    void firstElement() {
        getUnmodifiableVector().firstElement();
    }

    @Test
    void insertElementAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().insertElementAt(createComponentObject(), 0));
    }

    @Test
    void copyInto() {
        getUnmodifiableVector().copyInto(new Object[getUnmodifiableVector().size()]);
    }

    @Test
    void lastElement() {
        getUnmodifiableVector().lastElement();
    }

    @Test
    void lastIndexOfOffset() {
        getUnmodifiableVector().lastIndexOf(getCollectionObject(), 0);
    }

    @Test
    void indexOfOffset() {
        getUnmodifiableVector().indexOf(getCollectionObject(), 0);
    }

    @Test
    void removeAllElements() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().removeAllElements());
    }

    @Test
    void removeElement() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().removeElement(getCollectionObject()));
    }

    @Test
    void removeElementAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().removeElementAt(0));
    }

    @Test
    void setElementAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().setElementAt(createComponentObject(), 0));
    }

    @Test
    void trimToSize() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().trimToSize());
    }

    @Test
    void setSize() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableVector().setSize(getUnmodifiableVector().size() + 1));
    }

}
