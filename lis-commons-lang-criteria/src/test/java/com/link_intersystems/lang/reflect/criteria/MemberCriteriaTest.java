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

import com.link_intersystems.lang.reflect.AccessType;
import com.link_intersystems.lang.reflect.ReflectFacade;
import com.link_intersystems.lang.reflect.SignaturePredicate;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.TraverseStrategy;
import com.link_intersystems.lang.reflect.criteria.MemberCriteria.IterateStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MemberCriteriaTest {

    private MemberCriteria memberCriteria;

    @BeforeEach
    public void setup() {
        memberCriteria = new MemberCriteria();
    }

    @Test
    void nullMemberIterateOrderComparator() {
        assertThrows(NullPointerException.class, () -> memberCriteria.setMemberIterateOrder(null));
    }

    @Test
    void defaultMemberIterator() {
        Iterable<Class<?>> classIterable = new ClassCriteria().getIterable(Object.class);
        Iterable<Member> iterable = memberCriteria.getIterable(classIterable);
        Assertions.assertNotNull(iterable);
        Iterator<Member> iterator = iterable.iterator();
        Assertions.assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        Member next = iterator.next();
        Assertions.assertNotNull(next);

    }

    @Test
    void annotatedElementIterator() {
        Iterable<Class<?>> classIterable = new ClassCriteria().getIterable(Object.class);
        Iterable<? extends AnnotatedElement> iterable = memberCriteria.getAnnotatedElementIterable(classIterable);
        Assertions.assertNotNull(iterable);
        Iterator<? extends AnnotatedElement> iterator = iterable.iterator();
        Assertions.assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        AnnotatedElement next = iterator.next();
        Assertions.assertNotNull(next);

    }

    @Test
    void notAllowdModifiers() throws IllegalAccessException {
        Field[] declaredFields = Modifier.class.getDeclaredFields();
        int maxModifierValue = 0;
        for (Field field : declaredFields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.getType().equals(Integer.TYPE)) {
                try {
                    Integer value = (Integer) field.get(Modifier.class);
                    maxModifierValue = Math.max(maxModifierValue, value.intValue());
                } catch (IllegalArgumentException e) {
                    throw new AssertionError(e);
                }
            }
        }
        maxModifierValue = maxModifierValue << 2;
        try {
            memberCriteria.withModifiers(maxModifierValue);
            throw new AssertionError("modifiers are not allowed");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.startsWith("modifiers must be"));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void publicAbstractMethodStartingWithAdd() {
        memberCriteria.membersOfType(Method.class);
        memberCriteria.withModifiers(Modifier.ABSTRACT);
        memberCriteria.named(Pattern.compile("add.*"));
        memberCriteria.setMemberIterateOrder(ReflectFacade.getMemberNameComparator());
        Collection<Class<?>> iteratedTypes = new ArrayList<Class<?>>(Arrays.asList(List.class, Collection.class));
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Method, "member must be a method");
            iteratedTypes.remove(member.getDeclaringClass());
            String name = member.getName();
            assertTrue(name.startsWith("add"));
            int modifiers = member.getModifiers();
            assertTrue(Modifier.isAbstract(modifiers));
        }
        assertTrue(iteratedTypes.isEmpty(), "Some types have not been iterated. " + iteratedTypes);
    }

    @Test
    void getFirstMethodWithSameSignatureAsCollectionAddObject() throws SecurityException, NoSuchMethodException {
        memberCriteria.setResult(Result.FIRST);
        memberCriteria.membersOfType(Method.class);

        Method method = List.class.getDeclaredMethod("isEmpty");

        memberCriteria.add(new SignaturePredicate(method));

        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setTraverseStrategy(TraverseStrategy.DEPTH_FIRST);
        classCriteria.setSelection(ClassType.CLASSES);
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);

        Iterator<Member> criteriaIterator = memberIterable.iterator();
        Member member = criteriaIterator.next();
        Method firstMatch = (Method) member;
        Method declaredMethod = ArrayList.class.getDeclaredMethod("isEmpty");
        Assertions.assertEquals(declaredMethod, firstMatch);
        assertFalse(criteriaIterator.hasNext());
    }

    @Test
    void hierarchyTraveral() {
        memberCriteria.membersOfType(Method.class);
        memberCriteria.withModifiers(Modifier.ABSTRACT);
        memberCriteria.named(Pattern.compile("add.*"));
        Class<?> currentHierarchyClass = ArrayList.class;
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Method, "member must be a method");
            if (!currentHierarchyClass.equals(member.getDeclaringClass())) {
                if (!member.getDeclaringClass().isInterface()) {
                    assertTrue(((Class<?>) member.getDeclaringClass()).isAssignableFrom(currentHierarchyClass), "members must be iterated " + "from subclass to superclass");
                    // so the current declaring class is not the members
                    // declaring
                    // class which means we went up the hierarchy
                    currentHierarchyClass = member.getDeclaringClass();
                }
            }
        }
    }

    @Test
    void publicMethods() {
        memberCriteria.membersOfType(Method.class);
        memberCriteria.withAccess(AccessType.PUBLIC);
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Method, "member must be a method");
            int modifiers = member.getModifiers();
            assertTrue(Modifier.isPublic(modifiers));
            assertFalse(Modifier.isProtected(modifiers));
            assertFalse(Modifier.isPrivate(modifiers));
        }
    }

    @Test
    void defaultAccessMethods() {
        memberCriteria.membersOfType(Method.class);
        memberCriteria.withAccess(AccessType.DEFAULT);
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(Class.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Method, "member must be a method");
            int modifiers = member.getModifiers();
            assertFalse(Modifier.isPublic(modifiers));
            assertFalse(Modifier.isProtected(modifiers));
            assertFalse(Modifier.isPrivate(modifiers));
        }
    }

    @Test
    void defaultAccessMethodsWithStaticNativeOnly() {
        memberCriteria.membersOfType(Method.class);
        memberCriteria.withAccess(AccessType.DEFAULT);
        memberCriteria.withModifiers(Modifier.STATIC | Modifier.NATIVE);
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(Class.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Method, "member must be a method");
            int modifiers = member.getModifiers();
            assertFalse(Modifier.isPublic(modifiers));
            assertFalse(Modifier.isProtected(modifiers));
            assertFalse(Modifier.isPrivate(modifiers));
            assertTrue(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isNative(modifiers));
        }
    }

    @Test
    void usingFilterTest() {
        memberCriteria.membersOfType(Method.class);
        memberCriteria.add(new ToStringPredicate());
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(Object.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        Iterator<Member> iterator = memberIterable.iterator();
        assertTrue(iterator.hasNext());
        Member member = iterator.next();
        int modifiers = member.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(iterator.hasNext());
    }

    @Test
    void privateStaticFields() {
        memberCriteria.membersOfType(Field.class);
        memberCriteria.withAccess(AccessType.PRIVATE);
        memberCriteria.withModifiers(Modifier.STATIC);
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(Class.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Field, "member must be a field");
            int modifiers = member.getModifiers();
            assertFalse(Modifier.isPublic(modifiers));
            assertFalse(Modifier.isProtected(modifiers));
            assertTrue(Modifier.isPrivate(modifiers));
            assertTrue(Modifier.isStatic(modifiers));
        }
    }

    @Test
    void protecedConstructors() {
        memberCriteria.membersOfType(Constructor.class);
        memberCriteria.withAccess(AccessType.PROTECTED);
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        assertTrue(memberIterable.iterator().hasNext());
        for (Member member : memberIterable) {
            assertTrue(member instanceof Constructor<?>, "member must be a constructor");
            int modifiers = member.getModifiers();
            assertFalse(Modifier.isPublic(modifiers));
            assertTrue(Modifier.isProtected(modifiers));
            assertFalse(Modifier.isPrivate(modifiers));
        }
    }

    @Test
    void illegalMemeberTypes() {
        assertThrows(IllegalArgumentException.class, () -> memberCriteria.membersOfType(String.class));
    }

    @Test
    void illegalModifierPrivate() {
        assertThrows(IllegalArgumentException.class, () -> memberCriteria.withModifiers(Modifier.PRIVATE));
    }

    @Test
    void illegalModifierPublic() {
        assertThrows(IllegalArgumentException.class, () -> memberCriteria.withModifiers(Modifier.PUBLIC));
    }

    @Test
    void illegalModifierProtected() {
        assertThrows(IllegalArgumentException.class, () -> memberCriteria.withModifiers(Modifier.PROTECTED));
    }

    @Test
    void iteratorRemove() {
        memberCriteria.membersOfType(Field.class);
        memberCriteria.withAccess(AccessType.PRIVATE);
        memberCriteria.withModifiers(Modifier.STATIC);
        ClassCriteria classCriteria = new ClassCriteria();
        Iterable<Class<?>> classIterable = classCriteria.getIterable(Class.class);
        Iterable<Member> memberIterable = memberCriteria.getIterable(classIterable);
        Iterator<Member> iterator = memberIterable.iterator();
        assertTrue(iterator.hasNext());
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    @SuppressWarnings("all")
    public void withNullAccesses() {
        assertThrows(NullPointerException.class, () -> memberCriteria.withAccess(null));
    }

    @Test
    void withEmptyAccesses() {
        assertThrows(IllegalArgumentException.class, () -> memberCriteria.withAccess(new AccessType[0]));
    }

    @Test
    void annotatedElementsPackage2Class2Member() throws SecurityException, NoSuchMethodException {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setSelection(ClassType.CLASSES);
        memberCriteria.membersOfType(Method.class);
        memberCriteria.named("size");
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<? extends AnnotatedElement> annotatedElementIterable = memberCriteria.getAnnotatedElementIterable(classIterable, IterateStrategy.PACKAGE_CLASS_MEMBERS);
        Iterator<? extends AnnotatedElement> iterator = annotatedElementIterable.iterator();
        assertTrue(iterator.hasNext());
        AnnotatedElement next = iterator.next();
        Assertions.assertEquals(ArrayList.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(ArrayList.class, next);
        next = iterator.next();
        Assertions.assertEquals(ArrayList.class.getDeclaredMethod("size"), next);
        next = iterator.next();
        Assertions.assertEquals(AbstractList.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(AbstractList.class, next);
        next = iterator.next();
        Assertions.assertEquals(AbstractCollection.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(AbstractCollection.class, next);
        next = iterator.next();
        Assertions.assertEquals(AbstractCollection.class.getDeclaredMethod("size"), next);
        next = iterator.next();
        Assertions.assertEquals(Object.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(Object.class, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    void annotatedElementsMembers2Classes() throws SecurityException, NoSuchMethodException {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setSelection(ClassType.CLASSES);

        memberCriteria.membersOfType(Method.class);
        memberCriteria.named("size");
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<? extends AnnotatedElement> annotatedElementIterable = memberCriteria.getAnnotatedElementIterable(classIterable, IterateStrategy.MEMBERS_CLASS);
        annotatedElementIterable.forEach(System.out::println);

        Iterator<? extends AnnotatedElement> iterator = annotatedElementIterable.iterator();
        assertTrue(iterator.hasNext());
        AnnotatedElement next = iterator.next();
        Assertions.assertEquals(ArrayList.class.getDeclaredMethod("size"), next);
        next = iterator.next();
        Assertions.assertEquals(ArrayList.class, next);
        next = iterator.next();
        Assertions.assertEquals(AbstractList.class, next);
        next = iterator.next();
        Assertions.assertEquals(AbstractCollection.class.getDeclaredMethod("size"), next);
        next = iterator.next();
        Assertions.assertEquals(AbstractCollection.class, next);
        next = iterator.next();
        Assertions.assertEquals(Object.class, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    void annotatedElementsPackagesOnly() throws SecurityException, NoSuchMethodException {
        ClassCriteria classCriteria = new ClassCriteria();
        classCriteria.setSelection(ClassType.CLASSES);
        memberCriteria.membersOfType(Method.class);
        memberCriteria.named("size");
        Iterable<Class<?>> classIterable = classCriteria.getIterable(ArrayList.class);
        Iterable<? extends AnnotatedElement> annotatedElementIterable = memberCriteria.getAnnotatedElementIterable(classIterable, IterateStrategy.PACKAGES_ONLY);
        Iterator<? extends AnnotatedElement> iterator = annotatedElementIterable.iterator();
        assertTrue(iterator.hasNext());
        AnnotatedElement next = iterator.next();
        Assertions.assertEquals(ArrayList.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(AbstractList.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(AbstractCollection.class.getPackage(), next);
        next = iterator.next();
        Assertions.assertEquals(Object.class.getPackage(), next);
        assertFalse(iterator.hasNext());
    }

    private static class ToStringPredicate implements Predicate<Member>, Serializable {

        private static final long serialVersionUID = 2067359829290114563L;

        public boolean test(Member member) {
            return "toString".equals(member.getName());
        }
    }
}
