package com.link_intersystems.lang.reflect;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.stream;

public class FilteredMethodIterator implements Iterator<Method> {

    private Predicate<Method> methodPredicate;
    private Queue<Method> members = new LinkedList<>();
    private Queue<Class<?>> types = new LinkedList<>();

    /**
     * @param startType
     * @param methodPredicate
     */
    public FilteredMethodIterator(Class<?> startType, Predicate<Method> methodPredicate) {
        this.methodPredicate = methodPredicate;
        types.add(startType);
    }

    @Override
    public boolean hasNext() {
        if (members.isEmpty() && !types.isEmpty()) {
            Class<?> type = types.poll();
            addNextMembers(type);
        }
        return !members.isEmpty();
    }

    private void addNextMembers(Class<?> type) {
        Method[] declaredMethods = type.getDeclaredMethods();
        stream(declaredMethods).filter(methodPredicate).forEach(members::add);
        List<Class<?>> nextTypes = getNextTypes(type);
        types.addAll(nextTypes);
    }

    protected List<Class<?>> getNextTypes(Class<?> type) {
        List<Class<?>> nextTypes = new ArrayList<>();
        Class<?>[] interfaces = type.getInterfaces();
        stream(interfaces).forEach(types::add);

        Class<?> superclass = type.getSuperclass();
        if (superclass != null) {
            types.add(superclass);
        }

        return nextTypes;
    }

    @Override
    public Method next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return members.poll();
    }
}