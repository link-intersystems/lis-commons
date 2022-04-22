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
package com.link_intersystems.lang;

import java.util.Arrays;

/**
 * Assertion logic that is mostly used to validate methods pre- and
 * post-conditions. Encapsulates assertion logic.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public abstract class Assert {

    /**
     * Assert that a value mets a condition or throw an
     * {@link IllegalArgumentException}.
     *
     * @param expectedTruth     the truth that is expected.
     * @param messageFormat     the message of the {@link IllegalArgumentException}. The
     *                          message can be a formatted message as defined by
     *                          {@link String#format(String, Object...)}.
     * @param messageFormatArgs the arguments that should be applied to the formatted message.
     * @throws IllegalArgumentException if the condition is not met.
     * @since 1.2.0;
     */
    public static void isTrue(boolean expectedTruth, String messageFormat,
                              Object... messageFormatArgs) throws IllegalArgumentException {
        if (not(expectedTruth)) {
            String formatted = String.format(messageFormat, messageFormatArgs);
            throw new IllegalArgumentException(formatted);
        }
    }

    /**
     * Assert that a value is an instance of one of expected classes or throw an
     * {@link IllegalArgumentException}.
     *
     * @param name                the name of the value.
     * @param value               the value.
     * @param expectedInstanceOfs the expected classes that the value must be an instance of.
     * @since 1.2.0;
     */
    public static void instanceOf(String name, Object value,
                                  Class<?>... expectedInstanceOfs) {
        for (Class<?> expectedInstanceOf : expectedInstanceOfs) {
            if (expectedInstanceOf.isInstance(value)) {
                return;
            }
        }
        String exceptionMessage = String.format("%s must be an instance of %s",
                name, Arrays.toString(expectedInstanceOfs));
        throw new IllegalArgumentException(exceptionMessage);
    }

    /*
     * Increase the readability of conditional statements.
     */
    private static boolean not(boolean condition) {
        return !condition;
    }
}