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

/**
 * A {@link Result} defines how many result elements are selected by an
 * {@link ElementCriteria}.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0.0
 * @since 1.0.0.0 formerly named Selection
 * 
 */
public enum Result {
	/**
	 * Only the first element that matches the criteria is included in the
	 * result.
	 * 
	 * @since 1.2.0.0
	 */
	FIRST,
	/**
	 * Only the last element that matches the criteria is included in the
	 * result.
	 * 
	 * @since 1.2.0.0
	 */
	LAST,
	/**
	 * All elements that match the criteria are included in the result.
	 * 
	 * @since 1.2.0.0
	 */
	ALL;
}