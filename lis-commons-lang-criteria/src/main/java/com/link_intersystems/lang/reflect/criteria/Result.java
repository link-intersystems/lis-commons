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
package com.link_intersystems.lang.reflect.criteria;

import java.util.Iterator;
import java.util.function.Predicate;

import static com.link_intersystems.util.Iterators.filtered;

/**
 * A {@link Result} defines how many result elements are selected by an
 * {@link ElementCriteria}.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 * @since 1.0.0; formerly named Selection
 */
public enum Result {
    /**
     * Only the first element that matches the criteria is included in the
     * result.
     *
     * @since 1.2.0;
     */
    FIRST {
        @Override
        <T> Iterator<T> apply(Iterator<T> iterator) {
            Predicate<T> firstPredicate = new Predicate<T>() {

                private boolean first = true;

                public boolean test(Object object) {
                    if (first) {
                        first = false;
                        return true;
                    }
                    return false;
                }
            };
            return filtered(iterator, firstPredicate);
        }
    },
    /**
     * Only the last element that matches the criteria is included in the
     * result.
     *
     * @since 1.2.0;
     */
    LAST {
        @Override
        <T> Iterator<T> apply(Iterator<T> iterator) {
            Predicate<T> lastElementPredicate = object -> !iterator.hasNext();
            return filtered(iterator, lastElementPredicate);
        }
    },
    /**
     * All elements that match the criteria are included in the result.
     *
     * @since 1.2.0;
     */
    ALL {
        @Override
        <T> Iterator<T> apply(Iterator<T> iterator) {
            return iterator;
        }
    };

    abstract <T> Iterator<T> apply(Iterator<T> iterator);
}