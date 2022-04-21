package com.link_intersystems.events;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @param <L>
 * @param <E>
 */
public class EventMethod<L, E extends EventObject> implements Predicate<Method> {

    private static interface ClassResolveExceptionHandler {

        String notGenericSuperclass();
    }

    private class EventMethodResolver implements Supplier<List<Method>> {

        private String[] methodNames;
        private Supplier<Class<?>> listenerClassSupplier;
        private Supplier<Class<?>> eventClassSupplier;

        public EventMethodResolver(String[] methodNames, Supplier<Class<?>> listenerClassSupplier, Supplier<Class<?>> eventClassSupplier) {
            this.methodNames = methodNames;
            this.listenerClassSupplier = listenerClassSupplier;
            this.eventClassSupplier = eventClassSupplier;
        }

        @Override
        public List<Method> get() {
            if (methodNames.length == 0) {
                return resolveListenerMethods(getListenerClass(), getEventObjectClass());
            } else {
                return resolveListenerMethodsByName(methodNames, getListenerClass(), getEventObjectClass());
            }
        }
    }

    private List<Method> eventMethods;
    private Supplier<List<Method>> eventMethodsSupplier;

    private Class<E> eventObjectClass;
    private Class<L> listenerClass;

    public EventMethod(String... methodNames) {
        this(null, null, methodNames);
    }

    public EventMethod(Class<L> listenerClass, Class<E> eventObjectClass, String... methodNames) {
        this.listenerClass = listenerClass;
        this.eventObjectClass = eventObjectClass;

        this.eventMethodsSupplier = new EventMethodResolver(methodNames, this::getListenerClass, this::getEventObjectClass);
    }

    private EventMethod(EventMethod cloneBase, List<Method> methods) {
        this.eventObjectClass = cloneBase.getEventObjectClass();
        this.listenerClass = cloneBase.getListenerClass();
        this.eventMethodsSupplier = () -> methods;
    }

    private List<Method> resolveListenerMethodsByName(String[] methodNames, Class<L> listenerClass, Class<E> eventObjectClass) {
        List<Method> eventMethods = new ArrayList<>();
        for (String methodName : methodNames) {
            try {
                Method eventMethod = listenerClass.getDeclaredMethod(methodName, eventObjectClass);
                eventMethods.add(eventMethod);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return eventMethods;
    }

    private List<Method> resolveListenerMethods(Class<?> clazz, Class<E> eventObjectClass) {
        Class<?>[] eventMethodParams = new Class<?>[]{eventObjectClass};
        List<Method> listenerMethodNames = stream(clazz.getDeclaredMethods())
                .filter(m -> Arrays.equals(m.getParameterTypes(), eventMethodParams))
                .collect(toList());

        Class<?>[] superinterfaces = clazz.getInterfaces();

        List<Method> superMethodNames = stream(superinterfaces)
                .map(si -> resolveListenerMethods(si, eventObjectClass))
                .flatMap(List::stream)
                .collect(toList());

        listenerMethodNames.addAll(superMethodNames);
        return listenerMethodNames;
    }

    public boolean isCompatible(EventMethod<?, ?> otherEventMethod) {
        Class<?> otherListenerClass = otherEventMethod.getListenerClass();
        Class<?> eventObjectClass = getEventObjectClass();
        Class<?> thisListenerClass = getListenerClass();
        Class<?> otherEventObjectClass = otherEventMethod.getEventObjectClass();

        return thisListenerClass.equals(otherListenerClass) && eventObjectClass.equals(otherEventObjectClass);
    }

    public EventMethod<L, E> join(EventMethod<L, E> otherEventMethod) {
        if (!isCompatible(otherEventMethod)) {
            Class<?> otherListenerClass = otherEventMethod.getListenerClass();
            Class<?> eventObjectClass = this.getEventObjectClass();
            Class<?> thisListenerClass = getListenerClass();
            Class<?> otherEventObjectClass = otherEventMethod.getEventObjectClass();

            String msg = format("EventMethod can not be joined, because it's event listener and event object type is incompatible." +
                            " {0}.{1} != {2}.{3}", thisListenerClass.getSimpleName(),
                    eventObjectClass.getSimpleName(),
                    otherListenerClass.getSimpleName(),
                    otherEventObjectClass.getSimpleName());
            throw new IllegalArgumentException(msg);
        }

        List<Method> thisEventMethods = getEventMethods();
        LinkedHashSet<Method> joinMethodsUnique = new LinkedHashSet<>(thisEventMethods);

        List<Method> otherEventMethods = otherEventMethod.getEventMethods();
        joinMethodsUnique.addAll(otherEventMethods);

        List<Method> joinedMethods = new ArrayList<>(joinMethodsUnique);

        return new EventMethod<>(this, joinedMethods);
    }

    protected List<Method> getEventMethods() {
        if (eventMethods == null) {
            eventMethods = Collections.unmodifiableList(eventMethodsSupplier.get());
        }
        return eventMethods;
    }

    protected Class<L> getListenerClass() {
        if (listenerClass == null) {
            listenerClass = resolveGenericClass(0, () -> "Can not resolve listener class by the generic superclass." +
                    " Either override getListenerClass() or use the constructor that has a listener class parameter.");
        }
        return listenerClass;
    }

    protected <T> Class<T> resolveGenericClass(int genericTypeArgIndex, ClassResolveExceptionHandler exceptionHandler) {
        Class<? extends EventMethod> thisClass = getClass();
        Type genericSuperclass = thisClass.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            String msg = exceptionHandler.notGenericSuperclass();
            throw new IllegalStateException(msg);
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != 2) {
            String msg = "Type arguments length is not the expected length of the EventMethod<L, E>. " +
                    "Is the current class a direct subclass of EventMethod?";
            throw new IllegalStateException(msg);
        }

        Type listenerType = actualTypeArguments[genericTypeArgIndex];
        if (!(listenerType instanceof Class<?>)) {
            String msg = "The type argument at index " + genericTypeArgIndex +
                    " is not a Class<?>. It is " + listenerType;
            throw new IllegalStateException(msg);
        }

        return (Class<T>) listenerType;
    }

    protected Class<E> getEventObjectClass() {
        if (eventObjectClass == null) {
            eventObjectClass = resolveGenericClass(1, new ClassResolveExceptionHandler() {
                @Override
                public String notGenericSuperclass() {
                    return "Can not resolve event object class by the generic superclass." +
                            " Either override getEventObjectClass() or use the constructor that has an" +
                            " event object class listener parameter.";

                }
            });
        }
        return eventObjectClass;
    }


    public boolean test(Method method) {
        return getEventMethods().contains(method);
    }

    E getEventObject(Object[] args) {
        if (args.length > 0) {
            Object arg1 = args[0];
            if (eventObjectClass.isInstance(arg1)) {
                return eventObjectClass.cast(arg1);
            }
        }
        return null;
    }

    public L listener(Runnable runnable) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, runnable);
    }

    public L listener(Runnable runnable, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, eventFilter, runnable);
    }

    public L listener(Consumer<E> consumer) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, consumer);
    }

    public L listener(Consumer<E> consumer, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, eventFilter, consumer);
    }

    public <T> L listener(Consumer<T> consumer, Function<E, T> transformFunciton) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, e -> consumer.accept(transformFunciton.apply(e)));
    }

    public <T> L listener(Consumer<T> consumer, Function<E, T> transformFunciton, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, eventFilter,
                e -> consumer.accept(transformFunciton.apply(e)));
    }

    public <P> L listener(BiConsumer<E, P> consumer, P param) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, consumer, param);
    }

    public <P> L listener(BiConsumer<E, P> consumer, P param, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, eventFilter, consumer, param);
    }

    public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, P param) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, e -> consumer.accept(transformFunciton.apply(e), param));
    }

    public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, P param,
                             Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, eventFilter,
                e -> consumer.accept(transformFunciton.apply(e), param));
    }

    public <P> L listener(BiConsumer<E, P> consumer, Supplier<P> paramSupplier, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, eventFilter, consumer, paramSupplier);
    }

    public <P> L listener(BiConsumer<E, P> consumer, Supplier<P> paramSupplier) {
        Class<L> listenerClass = getListenerClass();
        return FuncListenerFactory.create(listenerClass, this, consumer, paramSupplier);
    }

    public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, Supplier<P> paramSupplier) {
        return FuncListenerFactory.create(listenerClass, this,
                (E e, P p) -> {
                    T apply = transformFunciton.apply(e);
                    consumer.accept(apply, p);
                }, paramSupplier);
    }

    public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, Supplier<P> paramSupplier,
                             Predicate<E> eventFilter) {
        return FuncListenerFactory.create(listenerClass, this, eventFilter,
                (E e, P p) -> consumer.accept(transformFunciton.apply(e), p), paramSupplier);
    }


    @Override
    public String toString() {
        return getEventObjectClass().getName() +
                "[" + String.join(", ", getEventMethods().stream().map(Method::getName).collect(toList())) +
                "]";
    }
}
