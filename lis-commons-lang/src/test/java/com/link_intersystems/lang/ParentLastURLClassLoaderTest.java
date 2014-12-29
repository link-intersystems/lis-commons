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

import org.junit.Assert;
import org.junit.Test;

public class ParentLastURLClassLoaderTest {

	@Test
	public void parentLast() throws ClassNotFoundException {
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL[] urLs = urlClassLoader.getURLs();

		ParentLastURLClassLoader parentLastURLClassLoader = new ParentLastURLClassLoader(
				urLs);
		Class<?> loadClass = parentLastURLClassLoader
				.loadClass(ParentLastURLClassLoaderTest.class
						.getCanonicalName());
		ClassLoader classLoader = loadClass.getClassLoader();

		Assert.assertSame(parentLastURLClassLoader, classLoader);
	}

	@Test
	public void alreadyLoaded() throws ClassNotFoundException {
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL[] urLs = urlClassLoader.getURLs();

		ParentLastURLClassLoader parentLastURLClassLoader = new ParentLastURLClassLoader(
				urLs);
		Class<?> loadClass = parentLastURLClassLoader
				.loadClass(ParentLastURLClassLoaderTest.class
						.getCanonicalName());
		Class<?> loadClass2 = parentLastURLClassLoader
				.loadClass(ParentLastURLClassLoaderTest.class
						.getCanonicalName());
		Assert.assertSame(loadClass, loadClass2);
	}

	@Test(expected = ClassNotFoundException.class)
	public void parentLastClassNotFound() throws ClassNotFoundException {
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL[] urLs = urlClassLoader.getURLs();

		ParentLastURLClassLoader parentLastURLClassLoader = new ParentLastURLClassLoader(
				urLs);
		parentLastURLClassLoader.loadClass(ParentLastURLClassLoaderTest.class
				.getCanonicalName() + "2");
	}

	@Test(expected = ClassNotFoundException.class)
	public void nullParent() throws ClassNotFoundException {
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL[] urLs = urlClassLoader.getURLs();
		ParentLastURLClassLoader parentLastURLClassLoader = new ParentLastURLClassLoader(
				urLs, null);
		parentLastURLClassLoader.loadClass(ParentLastURLClassLoaderTest.class
				.getCanonicalName() + "2");
	}

	@Test
	public void resolveClass() throws ClassNotFoundException {
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		URL[] urLs = urlClassLoader.getURLs();

		ParentLastURLClassLoader parentLastURLClassLoader = new ParentLastURLClassLoader(
				urLs);
		Class<?> loadClass = parentLastURLClassLoader
				.loadClass(ParentLastURLClassLoaderTest.class
						.getCanonicalName(), true);
		ClassLoader classLoader = loadClass.getClassLoader();
		Assert.assertSame(parentLastURLClassLoader, classLoader);
	}
}
