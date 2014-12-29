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
package com.link_intersystems.lang.reflect.test_Member2Applicability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericMethod {

	public void genericMethod(List<? extends String> aList,
			Map<Integer, Long> aMap) {

	}

	public void anotherGenericMethod(List<List<String>> aList,
			Map<Integer, Long> aMap) {

	}

	public static class ListStringArray extends ArrayList<List<String>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7834651063446855372L;

	}

	public static class StringArray extends ArrayList<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7834651063446855372L;

	}

	public static class IntegerArray extends ArrayList<Integer> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7834651063446855372L;

	}

	public static class IntLongMap extends HashMap<Integer, Long> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6961935256028575894L;

	}
}
