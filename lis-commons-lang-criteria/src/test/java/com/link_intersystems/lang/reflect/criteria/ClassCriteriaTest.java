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

import com.link_intersystems.lang.reflect.ReflectFacade;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.TraverseStrategy;
import com.link_intersystems.lang.reflect.testclasses.*;
import com.link_intersystems.util.Iterators;
import com.link_intersystems.util.Predicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class ClassCriteriaTest extends ElementCriteriaTest {

    private ClassCriteria classCriteria;

    @Override
    protected ElementCriteria<Class<?>> getElementCriteria() {
        return classCriteria;
    }

    @BeforeEach
    public void createInstance() {
        classCriteria = new ClassCriteria();
    }

    @Test
    void nullIterator() {
        assertThrows(NullPointerException.class, () -> classCriteria.getIterable(null));
    }

    @Test
    void nullTraverseStrategy() {
        assertThrows(NullPointerException.class, () -> classCriteria.setTraverseStrategy(null));
    }

    @Test
    void iteratorWithStartAt() {
        Iterator<Class<?>> iterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertNotNull(iterator);
    }

    @Test
    void innerClassesTraversal() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.INNER_CLASSES);
        Iterable<Class<?>> iterable = criteria.getIterable(ClassWithInnerClasses.class);
        Iterator<Class<?>> classIterator = iterable.iterator();

        assertEquals(ClassWithInnerClasses.InnerClass.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.InnerInterface.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.StaticInnerClass.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.StaticInnerInterface.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void innerClassesAndClassesTraversal() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.INNER_CLASSES, ClassType.CLASSES);
        Iterable<Class<?>> iterable = criteria.getIterable(ClassWithInnerClasses.class);
        Iterator<Class<?>> classIterator = iterable.iterator();

        assertEquals(ClassWithInnerClasses.InnerClass.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.InnerInterface.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.StaticInnerClass.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.StaticInnerInterface.class, classIterator.next());
        assertEquals(ClassWithInnerClasses.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void changeClassCriteriaShouldNotAffectIterable() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.CLASSES);

        Iterable<Class<?>> iterable = criteria.getIterable(ArrayList.class);

        criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
        criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
        criteria.setSeparatedClassTypeTraversal(true);
        criteria.setTraverseClassesUniquely(true);

        Iterator<Class<?>> classIterator = iterable.iterator();

        assertEquals(ArrayList.class, classIterator.next());
        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void reportFirst() {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setResult(Result.FIRST);
        Iterator<Class<?>> classIterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertTrue(classIterator.hasNext());
        Class<?> next = classIterator.next();
        assertEquals(ArrayList.class, next);
        assertFalse(classIterator.hasNext());
    }

    @Test
    void non_separated_class_traversal_breadth_first() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
        criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
        criteria.setSeparatedClassTypeTraversal(false);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(ArrayList.class, classIterator.next());
        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());
        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void non_separated_class_traversal_depth_first() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
        criteria.setSeparatedClassTypeTraversal(false);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(ArrayList.class, classIterator.next());

        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());

        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());

        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());

        assertEquals(Object.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void depthFirst_classes_interfaces_selected() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.CLASSES, ClassType.INTERFACES);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(ArrayList.class, classIterator.next());
        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void depthFirst_interfaces_classes() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
        criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
        criteria.setTraverseClassesUniquely(true);
        criteria.setSeparatedClassTypeTraversal(true);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());

        assertEquals(ArrayList.class, classIterator.next());
        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());

        assertFalse(classIterator.hasNext());
    }

    @Test
    void breadth_first_classes_interfaces() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
        criteria.setSeparatedClassTypeTraversal(true);
        criteria.setSelection(ClassType.CLASSES, ClassType.INTERFACES);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(ArrayList.class, classIterator.next());
        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());

        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());

        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());

        assertEquals(Collection.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());

        assertEquals(Iterable.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertFalse(classIterator.hasNext());
    }

    @Test
    void traverseSuperclassesOnly() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.CLASSES);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();
        assertEquals(ArrayList.class, classIterator.next());
        assertEquals(AbstractList.class, classIterator.next());
        assertEquals(AbstractCollection.class, classIterator.next());
        assertEquals(Object.class, classIterator.next());

        assertFalse(classIterator.hasNext());
    }

    @Test
    void traverseSuperclassesOnlyStopAt() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.CLASSES);
        criteria.stopAt(AbstractList.class);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        Class<?> currentClass = classIterator.next();
        assertEquals(ArrayList.class, currentClass);

        currentClass = classIterator.next();
        assertEquals(AbstractList.class, currentClass);

        assertFalse(classIterator.hasNext());
    }

    @Test
    void traverseInterfacesOnly() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.INTERFACES);
        criteria.setTraverseClassesUniquely(true);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());

        assertFalse(classIterator.hasNext());
    }

    @Test
    void traverseAllInterfacesOnly() {
        ClassCriteria criteria = new ClassCriteria();
        criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        criteria.setSelection(ClassType.INTERFACES);
        Iterator<Class<?>> classIterator = criteria.getIterable(ArrayList.class).iterator();

        assertEquals(Serializable.class, classIterator.next());
        assertEquals(Cloneable.class, classIterator.next());
        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());
        assertEquals(RandomAccess.class, classIterator.next());

        assertEquals(List.class, classIterator.next());
        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());

        assertEquals(Collection.class, classIterator.next());
        assertEquals(Iterable.class, classIterator.next());

        assertFalse(classIterator.hasNext());
    }

    @Test
    void reportLast() {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setResult(Result.LAST);
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.CLASSES);
        classCriteria.stopAt(AbstractCollection.class);
        Iterator<Class<?>> classIterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertTrue(classIterator.hasNext());
        Class<?> next = classIterator.next();
        assertEquals(AbstractCollection.class, next);
        assertFalse(classIterator.hasNext());
    }

    @Test
    void reportAll() {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setResult(Result.ALL);
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate().negate());
        classCriteria.stopAt(AbstractCollection.class);

        Iterator<Class<?>> classIterator = classCriteria.getIterable(ArrayList.class).iterator();
        Class<?>[] expectedClasses = new Class<?>[]{ArrayList.class, AbstractList.class, AbstractCollection.class};
        for (Class<?> expectedClass : expectedClasses) {
            assertTrue(classIterator.hasNext());
            Class<?> next = classIterator.next();
            assertEquals(expectedClass, next);
        }
        assertFalse(classIterator.hasNext());
    }

    @Test
    void nextOnHasNextFalse() {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setResult(Result.FIRST);

        Iterator<Class<?>> classIterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertTrue(classIterator.hasNext());
        classIterator.next();
        assertFalse(classIterator.hasNext());
        assertThrows(NoSuchElementException.class, classIterator::next);
    }

    @Test
    void removeClass() {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setResult(Result.FIRST);
        Iterator<Class<?>> classIterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertTrue(classIterator.hasNext());
        classIterator.next();
        assertThrows(UnsupportedOperationException.class, classIterator::remove);
    }

    @Test
    void localScopeOnly() {
        classCriteria.stopAt(ArrayList.class);
        Iterator<Class<?>> iterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        Class<?> next = iterator.next();
        assertEquals(ArrayList.class, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    void classesOnly() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.CLASSES);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate().negate());
        Iterator<Class<?>> iterator = classCriteria.getIterable(ArrayList.class).iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        Class<?> next = iterator.next();
        assertEquals(ArrayList.class, next);
        next = iterator.next();
        assertEquals(AbstractList.class, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(AbstractCollection.class, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(Object.class, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    void allInterfaces() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.CLASSES);
        classCriteria.setTraverseClassesUniquely(false);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate());
        Iterator<Class<?>> iterator = classCriteria.getIterable(ArrayList.class).iterator();

        List<Class<?>> interfaces = Iterators.toList(iterator);
        List<Class<?>> expectedInterfaces = new ArrayList<>(asList(
                Serializable.class,
                Cloneable.class,
                List.class,
                Collection.class,
                Iterable.class,
                RandomAccess.class,
                List.class,
                Collection.class,
                Iterable.class,
                Collection.class,
                Iterable.class));

        for (int i = 0; i < interfaces.size(); i++) {
            Class<?> interfaceClass = interfaces.get(i);
            Class<?> expectedClass = expectedInterfaces.get(i);
            assertEquals(expectedClass, interfaceClass);
        }
    }

    @Test
    void interfacesComparator() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.INTERFACES);
        classCriteria.setTraverseClassesUniquely(false);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate());
        classCriteria.setInterfacesIterationOrder(ReflectFacade.getCanonicalClassNameComparator().reversed());
        Iterator<Class<?>> iterator = classCriteria.getIterable(ArrayList.class).iterator();
        List<Class<?>> interfaces = Iterators.toList(iterator);

        List<Class<?>> expectedInterfaces = new ArrayList<>(asList(
                RandomAccess.class,
                List.class,
                Collection.class,
                Iterable.class,
                Cloneable.class,
                Serializable.class,
                List.class,
                Collection.class,
                Iterable.class,
                Collection.class,
                Iterable.class));

        for (int i = 0; i < interfaces.size(); i++) {
            Class<?> interfaceClass = interfaces.get(i);
            Class<?> expectedClass = expectedInterfaces.get(i);
            assertEquals(expectedClass, interfaceClass);
        }

    }

    @Test
    void visitOnceFilter() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.INTERFACES);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate());
        classCriteria.setTraverseClassesUniquely(true);

        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubInterface.class).iterator();

        /*
         * Convert the iterator to List makes debugging easier.
         */
        List<Class<?>> interfaces = Iterators.toList(iterator);
        List<Class<?>> expectedInterfaces = new ArrayList<>(asList(new Class<?>[]{
                GenericSubInterface.class,
                GenericInterface_Types_A_B_C.class}));
        for (Class<?> interf : interfaces) {
            assertTrue(expectedInterfaces.remove(interf), "Unexpected " + interf + " in interator");
        }
        assertTrue(expectedInterfaces.isEmpty(), "Iterator does not iterator over the interfaces " + expectedInterfaces);
    }

    @Test
    void visitAllFilter() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.INTERFACES);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate());
        Class<?> genericSubSubWithMultipleInterfacesClass = GenericSubSubWithMultipleInterfaces.class;
        Predicate<Class<?>> classEqualPredicate = Predicates.equal(genericSubSubWithMultipleInterfacesClass);
        Predicate<Class<?>> negate = classEqualPredicate.negate();
        classCriteria.add(negate);
        classCriteria.setTraverseClassesUniquely(false);
        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubSubWithMultipleInterfaces.class).iterator();

        List<Class<?>> interfaces = Iterators.toList(iterator);
        Collection<Class<?>> expectedInterfaces = new ArrayList<>(asList(
                GenericSubInterface.class,
                GenericInterface_Types_A_B_C.class,
                GenericInterface_Types_A_B_C.class));
        for (Class<?> interf : interfaces) {
            assertTrue(expectedInterfaces.remove(interf), "Unexpected " + interf + " in interator");
        }
        assertTrue(expectedInterfaces.isEmpty(), "Iterator does not iterator over the interfaces " + expectedInterfaces);
    }

    @Test
    void superclassesRemove() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.CLASSES);
        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubSubClass.class).iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void interfacesRemove() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.INTERFACES);
        classCriteria.add(ReflectFacade.getIsInterfacePredicate());
        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubWithInterface.class).iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void localScopeNoMoreElements() {
        classCriteria.stopAt(GenericSubSubClass.class);
        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubSubClass.class).iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next); // should throw exception
    }

    @Test
    void superclassesNoMoreElements() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.CLASSES);
        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubSubClass.class).iterator();

        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next); // should throw exception
    }

    @Test
    void interfacesNoMoreElements() {
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.INTERFACES);
        Iterator<Class<?>> iterator = classCriteria.getIterable(GenericSubWithInterface.class).iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next); // should throw exception
    }

    @Test
    void stopAtClassIsNotASuperclass() {
        classCriteria.stopAt(List.class);
        assertThrows(IllegalArgumentException.class, () -> classCriteria.getIterable(GenericSubWithInterface.class).iterator());
    }

    @Test
    void superClassIteratorNoSuchElements() {
        SuperclassIterator superclassIterator = new SuperclassIterator(ArrayList.class);
        while (superclassIterator.hasNext()) {
            superclassIterator.next();
        }
        assertThrows(NoSuchElementException.class, superclassIterator::next);
    }

    @Test
    void interfacesIteratorNoSuchElements() {
        InterfacesIterator interfacesIterator = new InterfacesIterator(ArrayList.class, ReflectFacade.getCanonicalClassNameComparator());
        while (interfacesIterator.hasNext()) {
            interfacesIterator.next();
        }
        assertThrows(NoSuchElementException.class, interfacesIterator::next);
    }

    @Test
    void classTypeEnums() {
        /*
         * Code coverage for synthetic methods
         */
        ClassType[] values = ClassType.values();
        for (ClassType classType : values) {
            ClassType valueOf = ClassType.valueOf(classType.name());
            assertEquals(classType, valueOf);
        }
    }
}
