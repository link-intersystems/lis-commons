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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UnmodifiableListIteratorTest  {

    private UnmodifiableListIterator<String> unmodifiableListIterator;

    @BeforeEach
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        ListIterator<String> listIterator = arrayList.listIterator();
        unmodifiableListIterator = new UnmodifiableListIterator<String>(listIterator);
    }

    @Test
    void add() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListIterator.add("A"));
    }

    @Test
    void hasNext() {
        unmodifiableListIterator.hasNext();
    }

    @Test
    void hasPrevious() {
        unmodifiableListIterator.hasPrevious();
    }

    @Test
    void next() {
        unmodifiableListIterator.next();
    }

    @Test
    void nextIndex() {
        unmodifiableListIterator.nextIndex();
    }

    @Test
    void previous() {
        unmodifiableListIterator.next();
        unmodifiableListIterator.previous();
    }

    @Test
    void previousIndex() {
        unmodifiableListIterator.previousIndex();
    }

    @Test
    void remove() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListIterator.remove());
    }

    @Test
    void set() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListIterator.set("A"));
    }

}
