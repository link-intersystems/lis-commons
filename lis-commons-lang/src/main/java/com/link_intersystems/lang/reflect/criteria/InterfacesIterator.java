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
package com.link_intersystems.lang.reflect.criteria;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

class InterfacesIterator implements Iterator<Class<?>> {

	private Class<?>[] interfaces;

	private int index;

	private final Class<?> clazz;

	private final Comparator<Class<?>> interfacesComparator;

	public InterfacesIterator(Class<?> clazz,
			Comparator<Class<?>> interfacesComparator) {
		this.clazz = clazz;
		this.interfacesComparator = interfacesComparator;
	}

	public boolean hasNext() {
		if (interfaces == null) {
			Class<?>[] interfaces = clazz.getInterfaces();
			if (interfacesComparator != null) {
				Arrays.sort(interfaces, interfacesComparator);
			}
			this.interfaces = interfaces;
			this.index = 0;
		}
		return index < interfaces.length;
	}

	public Class<?> next() {
		if (hasNext()) {
			Class<?> next = interfaces[index++];
			return next;
		} else {
			throw new NoSuchElementException(
					"iterator doesn't have more elements");
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}