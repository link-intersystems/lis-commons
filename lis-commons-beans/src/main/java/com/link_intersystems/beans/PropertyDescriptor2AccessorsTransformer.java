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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Transforms a {@link PropertyDescriptor} to an {@link Iterator} that iterates
 * through the properties accessor methods (read method first if any).
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
@SuppressWarnings("rawtypes")
class PropertyDescriptor2AccessorsTransformer implements Function<PropertyDescriptor, List<Method>> {

    public static final Function<PropertyDescriptor, List<Method>> INSTANCE = new PropertyDescriptor2AccessorsTransformer();

    public List<Method> apply(PropertyDescriptor descriptor) {
        List<Method> methods = new ArrayList<Method>();

        Method readMethod = descriptor.getReadMethod();
        if (readMethod != null) {
            methods.add(readMethod);
        }

        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod != null) {
            methods.add(writeMethod);
        }

        return methods;
    }

}