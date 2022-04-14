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

import com.link_intersystems.util.Serialization;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ElementCriteriaTest {

    protected abstract ElementCriteria getElementCriteria();

    @Test
    void nullResultSelection() {
        ElementCriteria criteria = getElementCriteria();
        assertThrows(IllegalArgumentException.class, () -> criteria.setResult(null));
    }

    @Test
    void nullAddPredicate() {
        ElementCriteria criteria = getElementCriteria();
        assertThrows(IllegalArgumentException.class, () -> criteria.add(null));
    }

    @Test
    void addNonSerializablePredicate() {
        ElementCriteria criteria = getElementCriteria();
        assertThrows(IllegalArgumentException.class, () -> criteria.add(new SomePredicateNotSerializable()));
    }

    @Test
    void elementCriteriaDefaultConstructor() {
        /*
         * Should not throw any exception
         */
        new ElementCriteria() {

            /**
             *
             */
            private static final long serialVersionUID = -6057707149446648089L;
        };
    }

    @Test
    void elementCriteriaSerializable() {
        Serialization.clone(getElementCriteria());
    }

    private static class SomePredicateNotSerializable implements Predicate<Object> {

        public boolean test(Object object) {
            return false;
        }

    }
}
