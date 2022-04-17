/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.lang.reflect;

import java.io.Serializable;
import java.util.List;

/**
 * Implements the logic to determine if a method or constructor is applicable
 * for some parameter types as described in the java language specification
 *
 * <ul>
 * <li>15.12.2, Compile-Time Step 2: Determine Method Signature</li>
 * <li>15.12.2.1 Identify Potentially Applicable Methods</li>
 * <li>15.12.2.2 Phase 1: Identify Matching Arity Methods Applicable by
 * Subtyping</li>
 * <li>15.12.2.3 Phase 2: Identify Matching Arity Methods Applicable by Method
 * Invocation Conversion</li>
 * <li>15.12.2.4 Phase 3: Identify Applicable Variable Arity Methods</li>
 * </ul>
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @see Member2#isApplicable(Class[])
 * @since 1.2.0;
 */
class Member2Applicability implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8429678766385138831L;

    private final Member2<?> member2;

    Member2Applicability(Member2<?> member2) {
        this.member2 = member2;
    }

    /**
     * Returns true if the member that this {@link Member2Applicability} was constructed with
     * is applicable for the given parameter types.
     *
     * @param paramTypes
     * @return true if this {@link Invokable} is applicable for the parameter
     * types.
     */
    public boolean isApplicable(Class<?>[] paramTypes) {
        boolean potentialApplicable = isPotentiallyApplicable(paramTypes);
        if (!potentialApplicable) {
            return false;
        }
        List<Parameter> parameters = member2.getParameters();

        boolean applicable = phase1CheckApplicableSubtype(paramTypes, parameters);
        if (applicable) {
            return true;
        }

        applicable = phase2CheckMethodInvocationConversion(paramTypes, parameters);
        if (applicable) {
            return true;
        }

        applicable = phase3CheckApplicableVariableArityMethods(paramTypes);
        return applicable;
    }

    /**
     * Fast checks to check the basic rules that must match before doing further
     * deeper checks like type conversion etc.
     *
     * @param paramTypes
     * @return
     * @since 1.0.0;
     */
    private boolean isPotentiallyApplicable(Class<?>[] paramTypes) {
        Class<?>[] declarationParameters = member2.getParameterTypes();

        int memberArity = declarationParameters.length;
        int invocationArity = paramTypes.length;

        /*
         * If the member is a variable arity method with arity n, the arity of
         * the method invocation is greater or equal to n-1.
         */
        if (member2.isVarArgs()) {
            if (!(invocationArity >= memberArity - 1)) {
                return false;
            }
        } else {
            /*
             * If the member is a fixed arity method with arity n, the arity of
             * the method invocation is equal to n.
             */
            if (!(paramTypes.length == declarationParameters.length)) {
                return false;
            }
        }

        return true;
    }

    /*
     * 15.12.2.2 Phase 1: Identify Matching Arity Methods Applicable by
     * Subtyping
     */
    private boolean phase1CheckApplicableSubtype(Class<?>[] paramTypes, List<Parameter> parameters) {
        boolean applicable = true;
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameterInfo = parameters.get(i);
            if (parameterInfo.isVarArg() && i >= paramTypes.length) {
                break;
            }

            Class<?> invocationParameter = paramTypes[i];
            if (!parameterInfo.isApplicableSubtype(invocationParameter) && !parameterInfo.isApplicableAutobixing(invocationParameter)) {
                applicable = false;
                break;

            }
        }
        return applicable;
    }

    /*
     * 15.12.2.3 Phase 2: Identify Matching Arity Methods Applicable by Method
     * Invocation Conversion
     */
    private boolean phase2CheckMethodInvocationConversion(Class<?>[] paramTypes, List<Parameter> parameters) {
        boolean applicable;
        applicable = true;
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameterInfo = parameters.get(i);
            Class<?> invocationParameter = paramTypes[i];
            if (!parameterInfo.isApplicableMethodInvocationConversion(invocationParameter)) {
                applicable = false;
                break;
            }
        }
        return applicable;
    }

    /*
     * 15.12.2.4 Phase 3: Identify Applicable Variable Arity Methods
     */
    private boolean phase3CheckApplicableVariableArityMethods(Class<?>[] paramTypes) {
        boolean applicable;
        applicable = true;
        for (int i = 0; i < paramTypes.length; i++) {
            Parameter param = member2.getParameter(i);
            Class<?> invocationParameter = paramTypes[i];
            if (!param.isApplicableVariableArityMethodInvocationConversion(invocationParameter)) {
                applicable = false;
                break;
            }
        }
        return applicable;
    }
}
