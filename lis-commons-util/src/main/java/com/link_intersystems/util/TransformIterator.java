/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.util;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * Decorates an iterator such that each element returned is transformed.
 *
 * @since 1.0
 */
public class TransformIterator<S, T> implements Iterator<T> {

    /**
     * The iterator being used
     */
    private Iterator<? extends S> iterator;
    /**
     * The transformer being used
     */
    private Transformer<? super S, ? extends T> transformer;

    /**
     * Constructs a new {@code TransformIterator} that will use the
     * given iterator and transformer.  If the given transformer is null,
     * then objects will not be transformed.
     *
     * @param iterator    the iterator to use
     * @param transformer the transformer to use
     */
    public TransformIterator(Iterator<? extends S> iterator, Transformer<? super S, ? extends T> transformer) {
        this.iterator = requireNonNull(iterator);
        this.transformer = requireNonNull(transformer);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Gets the next object from the iteration, transforming it using the
     * current transformer. If the transformer is null, no transformation
     * occurs and the object from the iterator is returned directly.
     *
     * @return the next object
     * @throws java.util.NoSuchElementException if there are no more elements
     */
    @Override
    public T next() {
        return transform(iterator.next());
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    /**
     * Transforms the given object using the transformer.
     * If the transformer is null, the original object is returned as-is.
     *
     * @param source the object to transform
     * @return the transformed object
     */
    protected T transform(final S source) {
        return transformer.transform(source);
    }
}