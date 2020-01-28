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
package com.link_intersystems.lang.reflect;

import java.util.List;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotPredicate;

/**
 * Implementation of the "Choosing the Most Specific Method" algorithm as
 * defined by the java language specification, chapter 15.12.2.5.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <CANDIDATE_TYPE>
 * @since 1.2.0.0
 */
class ChooseMostSpecificMemberStrategy<CANDIDATE_TYPE extends Member2<?>> {

	private static class VarargInvokablePredicate implements Predicate {

		public static final Predicate INSTANCE = new VarargInvokablePredicate();

		public boolean evaluate(Object object) {
			Member2<?> invokable = (Member2<?>) object;
			return invokable.isVarArgs();
		}
	}

	@SuppressWarnings("unchecked")
	public CANDIDATE_TYPE chooseMostSpecific(
			List<CANDIDATE_TYPE> potentiallyApplicable) {
		CANDIDATE_TYPE choosen = null;
		switch (potentiallyApplicable.size()) {
		case 0:
			/*
			 * No potentially applicable methods so fall through.
			 */
			break;
		case 1:
			/*
			 * Only one applicable method available. So we choose it.
			 */
			choosen = potentiallyApplicable.get(0);
			break;
		default:
			/*
			 * More than one applicable invokable. We have to choose the most
			 * specific.
			 */
			choosen = (CANDIDATE_TYPE) chooseMostSpecificImpl(potentiallyApplicable);
			break;
		}
		return choosen;
	}

	protected Member2<?> chooseMostSpecificImpl(
			List<? extends Member2<?>> potentiallyApplicable) {
		boolean containsVarargsInvokable = CollectionUtils.exists(
				potentiallyApplicable, VarargInvokablePredicate.INSTANCE);
		Member2<?> mostSpecific = null;
		if (containsVarargsInvokable) {
			Member2<?> noVarargsMethod = (Member2<?>) CollectionUtils
					.find(potentiallyApplicable, NotPredicate.notPredicate(VarargInvokablePredicate.INSTANCE));
			mostSpecific = noVarargsMethod;
		} else {
			MostSpecificInvokableClosure mostSpecificInvokableClosure = new MostSpecificInvokableClosure();
			CollectionUtils.forAllDo(potentiallyApplicable,
					mostSpecificInvokableClosure);
			mostSpecific = mostSpecificInvokableClosure.getMostSpecific();
		}
		return mostSpecific;
	}

	private static class MostSpecificInvokableClosure implements Closure {

		private Member2<?> mostSpecific;

		public void execute(Object input) {
			Member2<?> invokable2 = (Member2<?>) input;
			if (mostSpecific == null) {
				mostSpecific = invokable2;
			} else if (moreSpecific(mostSpecific, invokable2)) {
				mostSpecific = invokable2;
			}

		}

		public Member2<?> getMostSpecific() {
			return mostSpecific;
		}

		private boolean moreSpecific(Member2<?> reference, Member2<?> comparedTo) {
			Class<?>[] parameterTypes1 = reference.getParameterTypes();
			Class<?>[] parameterTypes2 = comparedTo.getParameterTypes();
			boolean moreSpecific = true;
			for (int i = 0; i < parameterTypes1.length; i++) {
				Class<?> referenceType = parameterTypes1[i];
				Class<?> compareToType = parameterTypes2[i];
				if (compareToType.isAssignableFrom(referenceType)) {
					moreSpecific = false;
					break;
				}
			}
			return moreSpecific;
		}
	}
}
