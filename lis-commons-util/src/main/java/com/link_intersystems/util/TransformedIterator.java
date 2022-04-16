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
 * An {@link Iterator} that transforms each element of a base iterator before returning it
 */
public class TransformedIterator<S, T> implements Iterator<T> {

    private Iterator<? extends S> baseIterator;
    private Transformer<? super S, ? extends T> transformer;

    public TransformedIterator(Iterator<? extends S> baseIterator, Transformer<? super S, ? extends T> transformer) {
        this.baseIterator = requireNonNull(baseIterator);
        this.transformer = requireNonNull(transformer);
    }

    @Override
    public boolean hasNext() {
        return baseIterator.hasNext();
    }

    @Override
    public T next() {
        S nextBaseElement = baseIterator.next();
        return transformer.transform(nextBaseElement);
    }

    @Override
    public void remove() {
        baseIterator.remove();
    }

}