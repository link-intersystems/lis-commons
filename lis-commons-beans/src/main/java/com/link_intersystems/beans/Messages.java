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
package com.link_intersystems.beans;

import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

/**
 * Package internal helper class to encapsulate message resource bundle handling
 * for the com.link_intersystems.beans package. The advantage of this class is
 * that clients need not to know the message bundle keys and if the clients get
 * moved to other packages (refactoring) a compiler error will occure when you
 * forget to move the message bundle and edit this class.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
abstract class Messages {

    static ResourceBundle get() {
        String baseName = Messages.class.getPackage().getName() + ".messages";
        return ResourceBundle.getBundle(baseName);
    }

    public static String formatNoSuchProperty(Class<?> beanClass,
                                              String propertyName) {
        requireNonNull(beanClass);
        ResourceBundle resourceBundle = get();
        String template = resourceBundle.getString("noSuchProperty");
        return String.format(template, beanClass.getCanonicalName(), propertyName);
    }

}
