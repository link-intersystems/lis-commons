/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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

import org.apache.commons.collections4.Transformer;

/**
 * Transforms a {@link PropertyDescriptor} to an {@link Iterator} that iterates
 * through the properties accessor methods (read method first if any).
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0.0
 */
@SuppressWarnings("rawtypes")
class PropertyDescriptor2AccessorsTransformer implements Transformer {

	public static final Transformer INSTANCE = new PropertyDescriptor2AccessorsTransformer();

	public Object transform(Object input) {
		Object transformed = input;
		if (input instanceof PropertyDescriptor) {
			PropertyDescriptor descriptor = PropertyDescriptor.class
					.cast(input);
			Method readMethod = descriptor.getReadMethod();
			Method writeMethod = descriptor.getWriteMethod();
			List<Method> methods = new ArrayList<Method>();
			if (readMethod != null) {
				methods.add(readMethod);
			}

			if (writeMethod != null) {
				methods.add(writeMethod);
			}
			transformed = methods.iterator();
		}
		return transformed;
	}

}