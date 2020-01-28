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
package com.link_intersystems.lang;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A {@link URLClassLoader} that first tries to load a class by it's own and if
 * it can not be loaded it asks it's parent class loader to load.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0.0
 */
public class ParentLastURLClassLoader extends URLClassLoader {

	private ClassLoader parent;

	/**
	 * Constructs a new ParentLastURLClassLoader for the specified URLs using
	 * this class loader's {@link ClassLoader} as it's parent . The URLs will be
	 * searched in the order specified for classes and resources after first
	 * searching in the parent class loader. Any URL that ends with a '/' is
	 * assumed to refer to a directory. Otherwise, the URL is assumed to refer
	 * to a JAR file which will be downloaded and opened as needed.
	 *
	 * <p>
	 * If there is a security manager, this method first calls the security
	 * manager's <code>checkCreateClassLoader</code> method to ensure creation
	 * of a class loader is allowed.
	 *
	 * @param urls
	 *            the URLs from which to load classes and resources
	 *
	 * @exception SecurityException
	 *                if a security manager exists and its
	 *                <code>checkCreateClassLoader</code> method doesn't allow
	 *                creation of a class loader.
	 * @see SecurityManager#checkCreateClassLoader
	 * @since 1.2.0.0
	 */
	public ParentLastURLClassLoader(URL[] urls) {
		this(urls, ParentLastURLClassLoader.class.getClassLoader());
	}

	/**
	 * Same as {@link #ParentLastURLClassLoader(URL[])}, but uses the given
	 * <code>parent</code> as it's parent class loader.
	 *
	 * @param urls
	 *            the URLs from which to load classes and resources
	 *
	 * @exception SecurityException
	 *                if a security manager exists and its
	 *                <code>checkCreateClassLoader</code> method doesn't allow
	 *                creation of a class loader.
	 * @param parent
	 *            the parent class loader to use. Can be <code>null</code>.
	 * @since 1.2.0.0
	 */
	public ParentLastURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls);
		this.parent = parent;
	}

	/**
	 * Tries to load the requested class by it's own and delegates to it's
	 * parent if itself can not load the class.
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 *
	 * @since 1.2.0.0
	 */
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		// First, check if the class has already been loaded
		Class<?> c = findLoadedClass(name);
		if (c == null) {
			try {
				c = findClass(name);
			} catch (ClassNotFoundException e) {
				if (parent != null) {
					c = parent.loadClass(name);
				} else {
					throw new ClassNotFoundException(name, e);
				}
			}
		}
		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

}
