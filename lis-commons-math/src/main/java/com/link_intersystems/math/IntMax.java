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
package com.link_intersystems.math;

/**
 * A {@link Max} with an {@link Integer} precision.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0;
 * 
 */
public class IntMax implements Max<Integer> {

	private Integer max;

	/**
	 * 
	 * @inherited
	 * 
	 * @since 1.2.0;.
	 */
	public Integer getValue() {
		return max;
	}

	/**
	 * Adds the integer representation to this max ({@link Number#intValue()}).
	 * 
	 * @inherited
	 * 
	 * @since 1.2.0;
	 */
	public boolean addValue(Number value) {
		boolean changed = false;
		if (max == null || Integer.valueOf(value.intValue()).compareTo(max) > 0) {
			max = value.intValue();
			changed = true;
		}
		return changed;
	}
}
