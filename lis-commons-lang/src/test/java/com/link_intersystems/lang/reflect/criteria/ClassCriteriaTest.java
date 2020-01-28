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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.comparators.ReverseComparator;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.junit.Before;
import org.junit.Test;

import com.link_intersystems.lang.reflect.ReflectFacade;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.TraverseStrategy;
import com.link_intersystems.lang.reflect.testclasses.GenericInterface_Types_A_B_C;
import com.link_intersystems.lang.reflect.testclasses.GenericSubInterface;
import com.link_intersystems.lang.reflect.testclasses.GenericSubSubClass;
import com.link_intersystems.lang.reflect.testclasses.GenericSubSubWithMultipleInterfaces;
import com.link_intersystems.lang.reflect.testclasses.GenericSubWithInterface;

public class ClassCriteriaTest extends ElementCriteriaTest {

	private ClassCriteria classCriteria;

	@Override
	protected ElementCriteria getElementCriteria() {
		return classCriteria;
	}

	@Before
	public void createInstance() {
		classCriteria = new ClassCriteria();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullIterator() {
		classCriteria.getIterable(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTraverseStrategy() {
		classCriteria.setTraverseStrategy(null);
	}

	@Test
	public void iteratorWithStartAt() {
		Iterator<Class<?>> iterator = classCriteria
				.getIterable(ArrayList.class).iterator();
		assertNotNull(iterator);
	}

	@Test
	public void innerClassesTraversal() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.INNER_CLASSES);
		Iterable<Class<?>> iterable = criteria
				.getIterable(ClassWithInnerClasses.class);
		Iterator<Class<?>> classIterator = iterable.iterator();

		assertEquals(ClassWithInnerClasses.InnerClass.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.InnerInterface.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.StaticInnerClass.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.StaticInnerInterface.class,
				classIterator.next());
		assertFalse(classIterator.hasNext());
	}

	@Test
	public void innerClassesAndClassesTraversal() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.INNER_CLASSES, ClassType.CLASSES);
		Iterable<Class<?>> iterable = criteria
				.getIterable(ClassWithInnerClasses.class);
		Iterator<Class<?>> classIterator = iterable.iterator();

		assertEquals(ClassWithInnerClasses.InnerClass.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.InnerInterface.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.StaticInnerClass.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.StaticInnerInterface.class,
				classIterator.next());
		assertEquals(ClassWithInnerClasses.class, classIterator.next());
		assertEquals(Object.class, classIterator.next());
		assertEquals(Object.class, classIterator.next());
		assertEquals(Object.class, classIterator.next());
		assertFalse(classIterator.hasNext());
	}

	@Test
	public void changeClassCriteriaShouldNotAffectIterable() {
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
	public void reportFirst() {
		ClassCriteria classCriteria = new ClassCriteria();
		classCriteria.setResult(Result.FIRST);
		Iterator<Class<?>> classIterator = classCriteria.getIterable(
				ArrayList.class).iterator();
		assertTrue(classIterator.hasNext());
		Class<?> next = classIterator.next();
		assertEquals(ArrayList.class, next);
		assertFalse(classIterator.hasNext());
	}

	@Test
	public void non_separated_class_traversal_breadth_first() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
		criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
		criteria.setSeparatedClassTypeTraversal(false);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

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
	public void non_separated_class_traversal_depth_first() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
		criteria.setSeparatedClassTypeTraversal(false);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

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
	public void depthFirst_classes_interfaces_selected() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.CLASSES, ClassType.INTERFACES);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

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
	public void depthFirst_interfaces_classes() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
		criteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
		criteria.setTraverseClassesUniquely(true);
		criteria.setSeparatedClassTypeTraversal(true);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

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
	public void breadth_first_classes_interfaces() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.BREADTH_FIRST);
		criteria.setSeparatedClassTypeTraversal(true);
		criteria.setSelection(ClassType.CLASSES, ClassType.INTERFACES);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

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
	public void traverseSuperclassesOnly() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.CLASSES);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();
		assertEquals(ArrayList.class, classIterator.next());
		assertEquals(AbstractList.class, classIterator.next());
		assertEquals(AbstractCollection.class, classIterator.next());
		assertEquals(Object.class, classIterator.next());

		assertFalse(classIterator.hasNext());
	}

	@Test
	public void traverseSuperclassesOnlyStopAt() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.CLASSES);
		criteria.stopAt(AbstractList.class);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();
		Class<?> currentClass = null;

		currentClass = classIterator.next();
		assertEquals(ArrayList.class, currentClass);

		currentClass = classIterator.next();
		assertEquals(AbstractList.class, currentClass);

		assertFalse(classIterator.hasNext());
	}

	@Test
	public void traverseInterfacesOnly() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.INTERFACES);
		criteria.setTraverseClassesUniquely(true);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

		assertEquals(Serializable.class, classIterator.next());
		assertEquals(Cloneable.class, classIterator.next());
		assertEquals(List.class, classIterator.next());
		assertEquals(Collection.class, classIterator.next());
		assertEquals(Iterable.class, classIterator.next());
		assertEquals(RandomAccess.class, classIterator.next());

		assertFalse(classIterator.hasNext());
	}

	@Test
	public void traverseAllInterfacesOnly() {
		ClassCriteria criteria = new ClassCriteria();
		criteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		criteria.setSelection(ClassType.INTERFACES);
		Iterator<Class<?>> classIterator = criteria
				.getIterable(ArrayList.class).iterator();

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
	public void reportLast() {
		ClassCriteria classCriteria = new ClassCriteria();
		classCriteria.setResult(Result.LAST);
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.CLASSES);
		classCriteria.stopAt(AbstractCollection.class);
		Iterator<Class<?>> classIterator = classCriteria.getIterable(
				ArrayList.class).iterator();
		assertTrue(classIterator.hasNext());
		Class<?> next = classIterator.next();
		assertEquals(AbstractCollection.class, next);
		assertFalse(classIterator.hasNext());
	}

	@Test
	public void reportAll() {
		ClassCriteria classCriteria = new ClassCriteria();
		classCriteria.setResult(Result.ALL);
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.add(NotPredicate.notPredicate(ReflectFacade
				.getIsInterfacePredicate()));
		classCriteria.stopAt(AbstractCollection.class);

		Iterator<Class<?>> classIterator = classCriteria.getIterable(
				ArrayList.class).iterator();
		Class<?>[] expectedClasses = new Class<?>[] { ArrayList.class,
				AbstractList.class, AbstractCollection.class };
		for (int i = 0; i < expectedClasses.length; i++) {
			assertTrue(classIterator.hasNext());
			Class<?> next = classIterator.next();
			assertEquals(expectedClasses[i], next);
		}
		assertFalse(classIterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void nextOnHasNextFalse() {
		ClassCriteria classCriteria = new ClassCriteria();
		classCriteria.setResult(Result.FIRST);

		Iterator<Class<?>> classIterator = classCriteria.getIterable(
				ArrayList.class).iterator();
		assertTrue(classIterator.hasNext());
		classIterator.next();
		assertFalse(classIterator.hasNext());
		classIterator.next();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void removeClass() {
		ClassCriteria classCriteria = new ClassCriteria();
		classCriteria.setResult(Result.FIRST);
		Iterator<Class<?>> classIterator = classCriteria.getIterable(
				ArrayList.class).iterator();
		assertTrue(classIterator.hasNext());
		classIterator.next();
		classIterator.remove();
	}

	@Test
	public void localScopeOnly() {
		classCriteria.stopAt(ArrayList.class);
		Iterator<Class<?>> iterator = classCriteria
				.getIterable(ArrayList.class).iterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		Class<?> next = iterator.next();
		assertEquals(ArrayList.class, next);
		assertFalse(iterator.hasNext());
	}

	@Test
	public void classesOnly() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.CLASSES);
		classCriteria.add(NotPredicate.notPredicate(ReflectFacade
				.getIsInterfacePredicate()));
		Iterator<Class<?>> iterator = classCriteria
				.getIterable(ArrayList.class).iterator();
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

	@SuppressWarnings("unchecked")
	@Test
	public void allInterfaces() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.CLASSES);
		classCriteria.setTraverseClassesUniquely(false);
		classCriteria.add(ReflectFacade.getIsInterfacePredicate());
		Iterator<Class<?>> iterator = classCriteria
				.getIterable(ArrayList.class).iterator();

		List<Class<?>> interfaces = IteratorUtils.toList(iterator);
		List<Class<?>> expectedInterfaces = new ArrayList<Class<?>>(
				Arrays.asList(Serializable.class, Cloneable.class, List.class,
						Collection.class, Iterable.class, RandomAccess.class,
						List.class, Collection.class, Iterable.class,
						Collection.class, Iterable.class));

		for (int i = 0; i < interfaces.size(); i++) {
			Class<?> interfaceClass = interfaces.get(i);
			Class<?> expectedClass = expectedInterfaces.get(i);
			assertEquals(expectedClass, interfaceClass);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void interfacesComparator() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.INTERFACES);
		classCriteria.setTraverseClassesUniquely(false);
		classCriteria.add(ReflectFacade.getIsInterfacePredicate());
		classCriteria.setInterfacesIterationOrder(new ReverseComparator(
				ReflectFacade.getCanonicalClassNameComparator()));
		Iterator<Class<?>> iterator = classCriteria
				.getIterable(ArrayList.class).iterator();
		List<Class<?>> interfaces = IteratorUtils.toList(iterator);

		List<Class<?>> expectedInterfaces = new ArrayList<Class<?>>(
				Arrays.asList(RandomAccess.class, List.class, Collection.class,
						Iterable.class, Cloneable.class, Serializable.class,
						List.class, Collection.class, Iterable.class,
						Collection.class, Iterable.class));

		for (int i = 0; i < interfaces.size(); i++) {
			Class<?> interfaceClass = interfaces.get(i);
			Class<?> expectedClass = expectedInterfaces.get(i);
			assertEquals(expectedClass, interfaceClass);
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void visitOnceFilter() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.INTERFACES);
		classCriteria.add(ReflectFacade.getIsInterfacePredicate());
		classCriteria.setTraverseClassesUniquely(true);

		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubInterface.class).iterator();

		/*
		 * Convert the iterator to List makes debugging easier.
		 */
		List<Class<?>> interfaces = IteratorUtils.toList(iterator);
		List<Class<?>> expectedInterfaces = new ArrayList<Class<?>>(
				Arrays.asList(new Class<?>[] { GenericSubInterface.class,
						GenericInterface_Types_A_B_C.class }));
		for (Class<?> interf : interfaces) {
			assertTrue("Unexpected " + interf + " in interator",
					expectedInterfaces.remove(interf));
		}
		assertTrue("Iterator does not iterator over the interfaces "
				+ expectedInterfaces, expectedInterfaces.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void visitAllFilter() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.INTERFACES);
		classCriteria.add(ReflectFacade.getIsInterfacePredicate());
		classCriteria.add(NotPredicate.notPredicate(EqualPredicate
				.equalPredicate(GenericSubSubWithMultipleInterfaces.class)));
		classCriteria.setTraverseClassesUniquely(false);
		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubSubWithMultipleInterfaces.class).iterator();

		List<Class<?>> interfaces = IteratorUtils.toList(iterator);
		Collection<Class<?>> expectedInterfaces = new ArrayList<Class<?>>(
				Arrays.asList(GenericSubInterface.class,
						GenericInterface_Types_A_B_C.class,
						GenericInterface_Types_A_B_C.class));
		for (Class<?> interf : interfaces) {
			assertTrue("Unexpected " + interf + " in interator",
					expectedInterfaces.remove(interf));
		}
		assertTrue("Iterator does not iterator over the interfaces "
				+ expectedInterfaces, expectedInterfaces.isEmpty());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void superclassesRemove() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.CLASSES);
		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubSubClass.class).iterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.remove();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void interfacesRemove() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.INTERFACES);
		classCriteria.add(ReflectFacade.getIsInterfacePredicate());
		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubWithInterface.class).iterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.remove();
	}

	@Test(expected = NoSuchElementException.class)
	public void localScopeNoMoreElements() {
		classCriteria.stopAt(GenericSubSubClass.class);
		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubSubClass.class).iterator();
		while (iterator.hasNext()) {
			iterator.next();
		}
		iterator.next(); // should throw exception
	}

	@Test(expected = NoSuchElementException.class)
	public void superclassesNoMoreElements() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.CLASSES);
		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubSubClass.class).iterator();

		while (iterator.hasNext()) {
			iterator.next();
		}
		iterator.next(); // should throw exception
	}

	@Test(expected = NoSuchElementException.class)
	public void interfacesNoMoreElements() {
		classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
		classCriteria.setSelection(ClassType.INTERFACES);
		Iterator<Class<?>> iterator = classCriteria.getIterable(
				GenericSubWithInterface.class).iterator();
		while (iterator.hasNext()) {
			iterator.next();
		}
		iterator.next(); // should throw exception
	}

	@Test(expected = IllegalArgumentException.class)
	public void stopAtClassIsNotASuperclass() {
		classCriteria.stopAt(List.class);
		classCriteria.getIterable(GenericSubWithInterface.class).iterator();
	}

	@Test(expected = NoSuchElementException.class)
	public void superClassIteratorNoSuchElements() {
		SuperclassIterator superclassIterator = new SuperclassIterator(
				ArrayList.class);
		while (superclassIterator.hasNext()) {
			superclassIterator.next();
		}
		superclassIterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void interfacesIteratorNoSuchElements() {
		InterfacesIterator interfacesIterator = new InterfacesIterator(
				ArrayList.class,
				ReflectFacade.getCanonicalClassNameComparator());
		while (interfacesIterator.hasNext()) {
			interfacesIterator.next();
		}
		interfacesIterator.next();
	}

	@Test
	public void classTypeEnums() {
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
