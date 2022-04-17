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

/**
 * A listener that is called when a context of a {@link ContextAware} gets
 * activated and deactivated.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @param <T>
 *            the type of the context object.
 * @since 1.2.0;
 */
public interface ContextListener<T> {

	/**
	 * Notifies the {@link ContextListener} that the context has been activated
	 * with the given contextObject.
	 * 
	 * @param contextObject
	 * @since 1.2.0;
	 */
	public void contextActivated(T contextObject);

	/**
	 * Notifies the {@link ContextListener} that the contextObject has been
	 * deactivated and no exception occurred during the execution of the
	 * {@link ContextAware} that this {@link ContextListener} is registered
	 * with.
	 * 
	 * @param contextObject
	 * @since 1.2.0;
	 */
	public void contextDeactivated(T contextObject);

	/**
	 * Notifies the {@link ContextListener} that the given contextObject has
	 * been deactivated while an {@link Exception} has been thorwn during the
	 * execution of the {@link ContextAware} that this {@link ContextListener}
	 * is registered with.
	 * 
	 * @param contextObject
	 * @param exception
	 *            the exception that was thrown while executing within the
	 *            context or <code>null</code> if no exception occurred.
	 * @since 1.2.0;
	 */
	public void contextDeactivated(T contextObject, Exception exception);
}
