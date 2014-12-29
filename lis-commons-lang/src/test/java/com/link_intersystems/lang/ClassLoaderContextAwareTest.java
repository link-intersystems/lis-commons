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

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.concurrent.Callable;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class ClassLoaderContextAwareTest {

	private ClassLoader classLoader;
	private URL testResourceUrl;
	private ClassLoaderContextAware mockClassLoaderContext;

	@Before
	public void setup() throws MalformedURLException {
		classLoader = PowerMock.createStrictMock(ClassLoader.class);
		mockClassLoaderContext = ClassLoaderContextAware
				.forClassLoader(classLoader);
		replay(classLoader);
	}

	@Test
	public void getSystemClassLoaderContext() {
		ClassLoaderContextAware systemClassLoaderContext = ClassLoaderContextAware
				.forClassLoader(null);
		assertSame(ClassLoaderContextAware.SYSTEM_CLASS_LOADER_CONTEXT,
				systemClassLoaderContext);
	}

	@Test
	public void getCachedClassLoaderContext() {
		ClassLoaderContextAware classLoaderContext = ClassLoaderContextAware
				.forClassLoader(classLoader);
		assertSame(mockClassLoaderContext, classLoaderContext);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void runCallable() throws Exception {
		Callable<URL> callable = createStrictMock(Callable.class);
		expect(callable.call()).andReturn(testResourceUrl);
		replay(callable);
		URL runInContext = mockClassLoaderContext.runInContext(callable);

		verify(callable);
		assertEquals(testResourceUrl, runInContext);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callableResetThreadContextLoader() throws Exception {
		Callable<URL> callable = createStrictMock(Callable.class);
		expect(callable.call()).andReturn(testResourceUrl);
		replay(callable);
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		try {

			URLClassLoader urlClassLoader = new URLClassLoader(new URL[0]);

			currentThread.setContextClassLoader(urlClassLoader);

			ClassLoader contextClassLoaderBefore = currentThread
					.getContextClassLoader();

			URL runInContext = mockClassLoaderContext.runInContext(callable);

			verify(callable);
			assertEquals(testResourceUrl, runInContext);

			ClassLoader contextClassLoaderAfter = currentThread
					.getContextClassLoader();
			assertSame(contextClassLoaderBefore, contextClassLoaderAfter);
		} finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void callableResetThreadContextLoaderOnException() throws Exception {
		Callable<URL> callable = createStrictMock(Callable.class);
		expect(callable.call()).andThrow(new RuntimeException());
		replay(callable);
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		try {
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[0]);
			currentThread.setContextClassLoader(urlClassLoader);
			ClassLoader contextClassLoaderBefore = currentThread
					.getContextClassLoader();

			try {
				mockClassLoaderContext.runInContext(callable);
			} catch (Exception e) {
				ClassLoader contextClassLoaderAfter = currentThread
						.getContextClassLoader();
				assertSame(contextClassLoaderBefore, contextClassLoaderAfter);
			}
			verify(callable);
		} finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	@Test
	public void runRunnable() throws Exception {
		Runnable runnable = createStrictMock(Runnable.class);
		runnable.run();
		expectLastCall();
		replay(runnable);

		mockClassLoaderContext.runInContext(runnable);

		verify(runnable);
	}

	@Test
	public void runnableResetThreadContextLoader() throws Exception {
		Runnable runnable = createStrictMock(Runnable.class);
		runnable.run();
		expectLastCall();
		replay(runnable);
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		try {
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[0]);
			currentThread.setContextClassLoader(urlClassLoader);
			ClassLoader contextClassLoaderBefore = currentThread
					.getContextClassLoader();
			mockClassLoaderContext.runInContext(runnable);
			ClassLoader contextClassLoaderAfter = currentThread
					.getContextClassLoader();
			assertSame(contextClassLoaderBefore, contextClassLoaderAfter);
			verify(runnable);
		} finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	@Test
	public void runnableResetThreadContextLoaderOnException() throws Exception {
		Runnable runnable = createStrictMock(Runnable.class);
		runnable.run();
		expectLastCall().andThrow(new RuntimeException());
		replay(runnable);
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		try {

			URLClassLoader urlClassLoader = new URLClassLoader(new URL[0]);
			currentThread.setContextClassLoader(urlClassLoader);
			ClassLoader contextClassLoaderBefore = currentThread
					.getContextClassLoader();
			try {
				mockClassLoaderContext.runInContext(runnable);
			} catch (Exception e) {
				ClassLoader contextClassLoaderAfter = currentThread
						.getContextClassLoader();
				assertSame(contextClassLoaderBefore, contextClassLoaderAfter);
			}
			verify(runnable);
		} finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void classLoaderContextProxy() {
		List<String> targetMock = createStrictMock(List.class);
		expect(targetMock.size()).andAnswer(new IAnswer<Integer>() {

			public Integer answer() throws Throwable {
				Thread currentThread = Thread.currentThread();
				ClassLoader contextClassLoader = currentThread
						.getContextClassLoader();
				assertSame(classLoader, contextClassLoader);
				return 13;
			}
		});
		replay(targetMock);
		List<String> classLoaderContextProxy = mockClassLoaderContext
				.createContextProxy(targetMock);
		int size = classLoaderContextProxy.size();
		assertEquals(13, size);
		verify(targetMock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void classLoaderContextProxyWithNull() {
		mockClassLoaderContext.createContextProxy(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void classLoaderContextProxyOnObjectWithNoInterfaces() {
		mockClassLoaderContext.createContextProxy(new Object());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void invokeMethodUsingReflection() throws Exception {
		List<String> targetMock = createStrictMock(List.class);
		expect(targetMock.size()).andAnswer(new IAnswer<Integer>() {

			public Integer answer() throws Throwable {
				Thread currentThread = Thread.currentThread();
				ClassLoader contextClassLoader = currentThread
						.getContextClassLoader();
				assertSame(classLoader, contextClassLoader);
				return 13;
			}
		});
		replay(targetMock);
		Integer size = mockClassLoaderContext.invokeInContext(targetMock,
				"size");
		assertEquals(13, size.intValue());
		verify(targetMock);
	}

	public void teardown() {
		verify(classLoader);
	}
}
