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

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class UnmodifiableListTest extends UnmodifiableCollectionTest {

    protected final Collection<Object> getUnmodifiableCollection() {
        return getUnmodifiableList();
    }

    protected abstract List<Object> getUnmodifiableList();

    @Test
    public void getAt() {
        getUnmodifiableList().get(0);
    }

    @Test
    public void indexOf() {
        getUnmodifiableList().indexOf(getCollectionObject());
    }

    @Test
    public void lastIndexOf() {
        getUnmodifiableList().lastIndexOf(getCollectionObject());
    }

    @Test
    public void listIterator() {
        getUnmodifiableList().listIterator();
    }

    @Test
    public void listIteratorAt() {
        getUnmodifiableList().listIterator(0);
    }

    @Test
    public void subList() {
        getUnmodifiableList().subList(0, 1);
    }

    @Test
    public void addAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableList().add(0, createComponentObject()));
    }

    @Test
    public void addAllAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableList().addAll(0, createComponentObjects()));
    }

    @Test
    public void listIteratorAdd() {
        ListIterator<Object> listIterator = getUnmodifiableList().listIterator();
        listIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> listIterator.add(createComponentObject()));
    }

    @Test
    public void listIteratorSet() {
        ListIterator<Object> listIterator = getUnmodifiableList().listIterator();
        listIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> listIterator.set(createComponentObject()));
    }

    @Test
    public void listIteratorRemove() {
        ListIterator<Object> listIterator = getUnmodifiableList().listIterator();
        listIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> listIterator.remove());
    }

    @Test
    public void listIteratorAtAdd() {
        ListIterator<Object> listIterator = getUnmodifiableList().listIterator(0);
        listIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> listIterator.add(createComponentObject()));
    }

    @Test
    public void listIteratorAtSet() {
        ListIterator<Object> listIterator = getUnmodifiableList().listIterator(0);
        listIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> listIterator.set(createComponentObject()));
    }

    @Test
    public void listIteratorAtRemove() {
        ListIterator<Object> listIterator = getUnmodifiableList().listIterator(0);
        listIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> listIterator.remove());
    }

    @Test
    public void removeAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableList().remove(0));
    }

    @Test
    public void setAt() {
        assertThrows(UnsupportedOperationException.class, () -> getUnmodifiableList().set(0, createComponentObject()));
    }

}
