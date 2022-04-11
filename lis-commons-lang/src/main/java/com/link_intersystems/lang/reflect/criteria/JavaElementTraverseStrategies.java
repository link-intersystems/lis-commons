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
package com.link_intersystems.lang.reflect.criteria;

import com.link_intersystems.util.ChainedIterator;

import java.lang.reflect.Member;
import java.util.*;

abstract class JavaElementTraverseStrategies {

    static final JavaElementTraverseStrategy PACKAGE_TRAVERSE_STRATEGY = new PackageTraverseStrategy();

    static final JavaElementTraverseStrategy SUPERCLASS_TRAVERSE_STRATEGY = new SuperclassTraverseStrategy();

    static final JavaElementTraverseStrategy INTERFACE_TRAVERSE_STRATEGY = new InterfaceTraverseStrategy();

    static final JavaElementTraverseStrategy CURRENT_CLASS_TRAVERSE_STRATEGY = new CurrentClassTraverseStrategy();

    static final JavaElementTraverseStrategy CURRENT_INTERFACE_TRAVERSE_STRATEGY = new CurrentClassTraverseStrategy(true);

    static final JavaElementTraverseStrategy MEMBER_TRAVERSE_STRATEGY = new MembersTraverseStrategy();

    private static class SuperclassTraverseStrategy implements JavaElementTraverseStrategy {

        private static final long serialVersionUID = -7099719523945697068L;

        public Iterator<?> getIterator(Class<?> currentClass, ElementCriteria elementCriteria) {
            return new SuperclassIterator(currentClass);
        }

    }

    private static class PackageTraverseStrategy implements JavaElementTraverseStrategy {

        private static final long serialVersionUID = 8917510401196634162L;

        public Iterator<?> getIterator(Class<?> currentClass, ElementCriteria elementCriteria) {
            return Collections.singleton(currentClass.getPackage()).iterator();
        }
    }

    private static class CurrentClassTraverseStrategy implements JavaElementTraverseStrategy {

        private static final long serialVersionUID = 8698437005189743019L;
        private final boolean interfacesOnly;

        public CurrentClassTraverseStrategy() {
            this.interfacesOnly = false;
        }

        public CurrentClassTraverseStrategy(boolean interfacesOnly) {
            this.interfacesOnly = interfacesOnly;
        }

        @SuppressWarnings("unchecked")
        public Iterator<?> getIterator(Class<?> currentClass, ElementCriteria elementCriteria) {
            if (interfacesOnly) {
                if (currentClass.isInterface()) {
                    return Collections.singleton(currentClass).iterator();
                } else {
                    return Collections.emptyIterator();
                }
            } else {
                return Collections.singletonList(currentClass).iterator();
            }
        }
    }

    private static class InterfaceTraverseStrategy implements JavaElementTraverseStrategy {

        /**
         *
         */
        private static final long serialVersionUID = -8550106254098361926L;

        public Iterator<?> getIterator(Class<?> currentClass, ElementCriteria elementCriteria) {
            Comparator<Class<?>> interfacesComparator = null;

            if (elementCriteria instanceof ClassCriteria) {
                ClassCriteria classCriteria = ClassCriteria.class.cast(elementCriteria);
                interfacesComparator = classCriteria.getInterfacesComparator();
            }

            return new InterfacesIterator(currentClass, interfacesComparator);
        }

    }

    private static class MembersTraverseStrategy implements JavaElementTraverseStrategy {

        /**
         *
         */
        private static final long serialVersionUID = 6115298421487915686L;

        public Iterator<?> getIterator(Class<?> currentClass, ElementCriteria elementCriteria) {
            if (elementCriteria instanceof MemberCriteria) {
                MemberCriteria memberCriteria = MemberCriteria.class.cast(elementCriteria);

                List<Member> memberList = memberCriteria.getSortedMembers(currentClass);
                /*
                 * only iterate the class's members
                 */
                Iterator<?> memberIterator = memberList.iterator();
                memberIterator = elementCriteria.applyElementFilter(memberIterator);
                memberIterator = elementCriteria.applySelectionFilter(memberIterator);
                return memberIterator;
            }

            return Collections.emptyIterator();
        }
    }
}

class CompositeJavaElementTraverseStrategy implements JavaElementTraverseStrategy {

    /**
     *
     */
    private static final long serialVersionUID = 3345188252751383941L;

    private final JavaElementTraverseStrategy[] javaElementTraverseStrategies;

    public CompositeJavaElementTraverseStrategy(JavaElementTraverseStrategy... javaElementTraverseStrategies) {
        this.javaElementTraverseStrategies = javaElementTraverseStrategies;
    }

    public Iterator<?> getIterator(Class<?> currentClass, ElementCriteria elementCriteria) {

        List<Iterator<?>> iterators = new ArrayList<>();
        for (int i = 0; i < javaElementTraverseStrategies.length; i++) {
            JavaElementTraverseStrategy javaElementTraverseStrategy = javaElementTraverseStrategies[i];
            Iterator<?> iterator = javaElementTraverseStrategy.getIterator(currentClass, elementCriteria);
            iterators.add(iterator);
        }


        return new ChainedIterator(iterators);
    }
}
