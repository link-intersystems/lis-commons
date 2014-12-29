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

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

/**
 * A {@link ClassLoaderContextAware} encapsulated a thread's context class
 * loader management. It ensures that the {@link ClassLoaderContextAware}'s
 * class loader is available as the thread's context class loader when a
 * {@link Callable}s, {@link Runnable} or reflection method calls is executed.
 * See {@link ContextAware} for more details.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.2.0.0
 *
 * @see ContextAware#createContextProxy(Object)
 * @see ContextAware#runInContext(Callable)
 * @see ContextAware#runInContext(Runnable)
 * @see ContextAware#invokeInContext(Object, String, Object...)
 */
public final class ClassLoaderContextAware extends ContextAware<ClassLoader> {

	private static final int DEFAULT_CACHE_SIZE = 1024;

	public static final ClassLoaderContextAware SYSTEM_CLASS_LOADER_CONTEXT = new ClassLoaderContextAware(
			ClassLoader.getSystemClassLoader());

	/**
	 * Class loaders can also be garbage collected and therefore we use a weak
	 * hash map to prevent memory leaks.
	 */
	private static final Map<ClassLoader, ClassLoaderContextAware> CLASS_LOADER_CONTEXT_CACHE = new WeakHashMap<ClassLoader, ClassLoaderContextAware>(
			DEFAULT_CACHE_SIZE);

	private final ClassLoader contextClassLoader;

	/**
	 * Creates a class loader context based on the given class loader.
	 *
	 * @param contextClassLoader
	 */
	private ClassLoaderContextAware(ClassLoader contextClassLoader) {
		this.contextClassLoader = contextClassLoader;
	}

	/**
	 * Creates a {@link ClassLoaderContextAware} for the given classLoader. The
	 * system class loader is used if the given classLoader is <code>null</code>
	 * . This method uses an internal cache and returns the cached
	 * {@link ClassLoaderContextAware} if possible. The cache uses
	 * {@link WeakReference}s so that the {@link ClassLoaderContextAware}s will
	 * get garbage collected if they are only referenced by this cache.
	 *
	 * @param classLoader
	 *            the classLoader to use or <code>null</code> if the system
	 *            class loader should be used.
	 * @return
	 * @since 1.2.0.0
	 */
	public static ClassLoaderContextAware forClassLoader(ClassLoader classLoader) {
		if (classLoader == null) {
			return SYSTEM_CLASS_LOADER_CONTEXT;
		}

		ClassLoaderContextAware cachedCLassLoaderContext = getCachedClassLoaderContext(classLoader);
		if (cachedCLassLoaderContext != null) {
			return cachedCLassLoaderContext;
		}

		ClassLoaderContextAware classLoaderContext = createAndCacheContextClassLoaderFor(classLoader);

		return classLoaderContext;
	}

	private static ClassLoaderContextAware createAndCacheContextClassLoaderFor(
			ClassLoader classLoader) {
		ClassLoaderContextAware classLoaderContext = new ClassLoaderContextAware(
				classLoader);
		CLASS_LOADER_CONTEXT_CACHE.put(classLoader, classLoaderContext);
		return classLoaderContext;
	}

	private static ClassLoaderContextAware getCachedClassLoaderContext(
			ClassLoader classLoader) {
		return CLASS_LOADER_CONTEXT_CACHE.get(classLoader);
	}

	/**
	 * Binds the class loader that this {@link ClassLoaderContextAware} was
	 * created with to the current threads
	 * {@link Thread#getContextClassLoader()}.
	 *
	 * @return the previous class loader that was bound to the current thread's
	 *         {@link Thread#getContextClassLoader()} when this method was
	 *         called.
	 * @since 1.2.0.0
	 */
	@Override
	protected ClassLoader activateContext() {
		Thread currentThread = Thread.currentThread();
		ClassLoader initialClassLoader = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(contextClassLoader);
		return initialClassLoader;
	}

	/**
	 * Unbinds this {@link ClassLoaderContextAware}'s class loader from this
	 * {@link Thread#getContextClassLoader()} and restores the class loader that
	 * was previously.
	 *
	 * @since 1.2.0.0
	 */
	@Override
	protected void deactivateContext(ClassLoader previousClassLoader) {
		Thread currentThread = Thread.currentThread();
		currentThread.setContextClassLoader(previousClassLoader);
	}

}
