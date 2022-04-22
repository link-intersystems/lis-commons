package com.link_intersystems.events;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * An {@link EventMethod} is an adapter that can adapt method references,
 * such as {@link Runnable}, {@link Consumer} and {@link BiConsumer}, to event listener methods.
 *
 * <pre>
 * public class EventMethodUsageExample {
 *
 *     public static void main(String[] args) {
 *         EventMethodUsageExample eventMethodUsageExample = new EventMethodUsageExample();
 *         eventMethodUsageExample.setSelection(3);
 *         eventMethodUsageExample.setSelection(5);
 *     }
 *
 *     private ListSelectionListener l1 = ListSelectionEventMethod.VALUE_CHANGED.listener(this::printEventFired);
 *
 *     private ListSelectionListener l2 = ListSelectionEventMethod.VALUE_CHANGED.listener(
 *             this::printSelectionChanged,
 *             ListSelectionEvent::getLastIndex,
 *             ">>> ", e -> !e.getValueIsAdjusting());
 *
 *     private ListSelectionModel selectionModel = new DefaultListSelectionModel();
 *
 *     EventMethodUsageExample() {
 *         selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 *         selectionModel.addListSelectionListener(l1);
 *         selectionModel.addListSelectionListener(l2);
 *     }
 *
 *     public void setSelection(int index) {
 *         selectionModel.setValueIsAdjusting(true);
 *         selectionModel.setSelectionInterval(index, index);
 *         selectionModel.setValueIsAdjusting(false);
 *     }
 *
 *     private void printEventFired(ListSelectionEvent e) {
 *         int firstIndex = e.getFirstIndex();
 *         int lastIndex = e.getLastIndex();
 *         boolean isAdjusting = e.getValueIsAdjusting();
 *         System.out.println("Selection model event fired:" +
 *                 " firstIndex= " + firstIndex +
 *                 " lastIndex= " + lastIndex +
 *                 " isAdjusting= " + isAdjusting);
 *     }
 *
 *     private void printSelectionChanged(int selectedIndex, String indent) {
 *         System.out.println(indent + "Index " + selectedIndex + " selected");
 *     }
 * }
 * </pre>
 * <p>
 * Running the example code above will output:
 *
 * <pre>
 * Selection model event fired: firstIndex= 3 lastIndex= 3 isAdjusting= true
 * >>> Index 3 selected
 * Selection model event fired: firstIndex= 3 lastIndex= 3 isAdjusting= false
 * Selection model event fired: firstIndex= 3 lastIndex= 5 isAdjusting= true
 * >>> Index 5 selected
 * Selection model event fired: firstIndex= 3 lastIndex= 5 isAdjusting= false
 * </pre>
 *
 * @param <L> the event listener type.
 * @param <E> the event object type.
 */
public class EventMethod<L, E extends EventObject> implements Predicate<Method> {

    private List<Method> eventMethods;
    private Supplier<List<Method>> eventMethodsSupplier;

    private Supplier<Class<E>> eventObjectClassSupplier;
    private Class<E> eventObjectClass;

    private Supplier<Class<L>> listenerClassSupplier;
    private Class<L> listenerClass;

    /**
     * Creates an {@link EventMethod} which supports the given event methods (by name) and whose event listener class
     * and event object class are resolved by the generic type arguments that a subclass defines . E.g.
     *
     * <pre>
     * public class ActionEventMethod extends EventMethod&lt;ActionListener, ActionEvent> {
     *
     *     public static final ActionEventMethod PERFORMED = new ActionEventMethod("actionPerformed");
     *
     *     public ActionEventMethod(String... methodNames) {
     *         super(methodNames);
     *     }
     * }
     * </pre>
     * <p>
     * The resolution algorithm also searches superclasses. E.g.
     *
     * <pre>
     *     abstract class BaseEventMethod extends EventMethod&lt;SomeListener, SomeEvent> {
     *     }
     *
     *     class ExtendsBase extends BaseEventMethod {
     *     }
     * </pre>
     * <p>
     * But the search algorithm has limitations. E.g. you can not split the type definition in the hierarchy. E.g.
     *
     * <pre>
     *     abstract class BaseEventMethod<L> extends EventMethod<L, SomeEvent> {
     *     }
     *
     *     class ExtendsBase extends BaseEventMethod&lt;SomeListener> {
     *     }
     * </pre>
     * <p>
     * and you may also not swap the type parameter positions.
     *
     * <pre>
     *     abstract class BaseEventMethod&lt;E extends EventObject, L> extends EventMethod<L, E> {
     *     }
     *
     *     class ExtendsBase extends BaseEventMethod&lt;SomeEvent, SomeListener> {
     *     }
     * </pre>
     * <p>
     * However, you can pass the arguments through multiple hierarchy levels.
     *
     * <pre>
     *     abstract class BaseEventMethod&lt;L, E extends EventObject> extends EventMethod&lt;L, E> {
     *     }
     *
     *     class ExtendsBase extends BaseEventMethod&lt;SomeListener, SomeEvent> {
     *     }
     * </pre>
     * <p>
     * or you can simply create an anonymous class if you don't want to create a custom subclass.
     *
     * <pre>
     *     EventMethod eventMethod = new EventMethod&lt;SomeListener, SomeEvent>(){};
     * </pre>
     *
     * @param methodNames the method names that this {@link EventMethod} should support.
     */
    protected EventMethod(String... methodNames) {
        this.listenerClassSupplier = () -> resolveGenericClass(getClass(), 0);
        this.eventObjectClassSupplier = () -> resolveGenericClass(getClass(), 1);
        this.eventMethodsSupplier = new EventMethodResolver(methodNames, this::getListenerClass, this::getEventObjectClass);
    }

    /**
     * Creates an {@link EventMethod} based on the given listener and event object type that supports the defined
     * methodNames.
     */
    public EventMethod(Class<L> listenerClass, Class<E> eventObjectClass, String... methodNames) {
        requireNonNull(listenerClass);
        requireNonNull(eventObjectClass);
        this.listenerClassSupplier = () -> listenerClass;
        this.eventObjectClassSupplier = () -> eventObjectClass;

        this.eventMethodsSupplier = new EventMethodResolver(methodNames, this::getListenerClass, this::getEventObjectClass);
    }

    private EventMethod(EventMethod<L, E> cloneBase, List<Method> methods) {
        this.eventObjectClass = cloneBase.getEventObjectClass();
        this.listenerClass = cloneBase.getListenerClass();
        this.eventMethodsSupplier = () -> methods;
    }

    /**
     * Returns true if this {@link EventMethod} is compatible to the otherEventMethod.
     * This {@link EventMethod} is compatible to the otherEventMethod if the event listener class and
     * the event object class are equal.
     *
     * @param otherEventMethod the {@link EventMethod} to check compatibility with.
     * @return true if this {@link EventMethod}'s event listener class and event object class is compatible
     * to the otherEventMethod.
     */
    public boolean isCompatible(EventMethod<?, ?> otherEventMethod) {
        Class<?> otherListenerClass = otherEventMethod.getListenerClass();
        Class<?> eventObjectClass = getEventObjectClass();
        Class<?> thisListenerClass = getListenerClass();
        Class<?> otherEventObjectClass = otherEventMethod.getEventObjectClass();

        return thisListenerClass.equals(otherListenerClass) && eventObjectClass.equals(otherEventObjectClass);
    }

    /**
     * A joined {@link EventMethod} is an {@link EventMethod} that supports this {@link EventMethod}'s methods and
     * the {@link EventMethod} methods of the other {@link EventMethod}.
     * <p>
     * Assume you have defined an {@link EventMethod} that supports the mouse clicked event.
     * <pre>
     *   EventMethod&lthMouseListener,MouseEvent> clickedEvent = new EventMethod&lt;>(MouseListener.class, MouseEvent.class, "mouseClicked");
     * </pre>
     * <p>
     * and an EventMethod that supports the mouse pressed event.
     *
     * <pre>
     *   EventMethod&lthMouseListener,MouseEvent> pressedEvent = new EventMethod&lt;>(MouseListener.class, MouseEvent.class, "mousePressed");
     * </pre>
     * <p>
     * You can then create a new {@link EventMethod} that supports both event methods by joining them.
     * <pre>
     *   EventMethod&lthMouseListener,MouseEvent> clickedAndPressedEvents = clickedEvent.join(pressedEvent);
     * </pre>
     * </p>
     *
     * @param otherEventMethod the other event method to join with.
     * @return a new {@link EventMethod} instance that supports all event methods of this and the otherEventMethod.
     * @throws IllegalArgumentException if the otherEventMethod is incompatible with this event and can therefore not
     *                                  be joined. The otherEventMethod is incompatible if the event listener class or
     *                                  the event object class are not equal.
     * @see #isCompatible(EventMethod)
     */
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
            listenerClass = listenerClassSupplier.get();

            if (listenerClass == null) {
                String msg = "Can not resolve listener class by the generic superclass of" +
                        " any superclass in the hierarchy. Either override getListenerClass() or" +
                        " use the constructor that has a listener class parameter.";
                throw new IllegalStateException(msg);
            }
        }
        return listenerClass;
    }

    protected Class<E> getEventObjectClass() {
        if (eventObjectClass == null) {
            eventObjectClass = eventObjectClassSupplier.get();

            if (eventObjectClass == null || !EventObject.class.isAssignableFrom(eventObjectClass)) {
                String msg = "Can not resolve event object class by the generic superclass of" +
                        " any superclass in the hierarchy. Either override getListenerClass() or" +
                        " use the constructor that has a listener class parameter.";
                throw new IllegalStateException(msg);
            }
        }
        return eventObjectClass;
    }

    /**
     * Returns true if the given {@link Method} is an event method supported by this {@link EventMethod}.
     */
    public boolean test(Method method) {
        return getEventMethods().contains(method);
    }

    /**
     * Extracts the event Object from the argument array, which is per convention the first argument of a listener
     * method.
     */
    E getEventObject(Object[] args) {
        if (args.length > 0) {
            Object arg1 = args[0];
            if (eventObjectClass.isInstance(arg1)) {
                return eventObjectClass.cast(arg1);
            }
        }
        return null;
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link Runnable}.
     */
    public L listener(Runnable runnable) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, runnable);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link Runnable} if the event is accepted by the eventFilter.
     */
    public L listener(Runnable runnable, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter, runnable);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link Consumer}.
     */
    public L listener(Consumer<E> consumer) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, consumer);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link Consumer} if the event is accepted by the eventFilter.
     */
    public L listener(Consumer<E> consumer, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter, consumer);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link Consumer} by invoking it with the transformed event object.
     */
    public <T> L listener(Consumer<T> consumer, Function<E, T> transformFunciton) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, e -> consumer.accept(transformFunciton.apply(e)));
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link Consumer} by invoking it with the transformed event object if the event is
     * accepted by the eventFilter.
     */
    public <T> L listener(Consumer<T> consumer, Function<E, T> transformFunciton, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter,
                e -> consumer.accept(transformFunciton.apply(e)));
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with the event object and the given argument.
     */
    public <A> L listener(BiConsumer<E, A> consumer, A arg) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, consumer, arg);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with the event object and the given argument
     * if the event is accepted by the eventFilter.
     */
    public <A> L listener(BiConsumer<E, A> consumer, A arg, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter, consumer, arg);
    }


    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with the transformed event object and the given argument.
     */
    public <A, T> L listener(BiConsumer<T, A> consumer, Function<E, T> transformFunciton, A arg) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, e -> consumer.accept(transformFunciton.apply(e), arg));
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with the transformed event object and the given argument
     * if the event is accepted by the eventFilter.
     */
    public <A, T> L listener(BiConsumer<T, A> consumer, Function<E, T> transformFunciton, A arg,
                             Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter,
                e -> consumer.accept(transformFunciton.apply(e), arg));
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with the event object and the argument supplied
     * by the argSupplier if the event is accepted by the eventFilter.
     */
    public <A> L listener(BiConsumer<E, A> consumer, Supplier<A> argSupplier, Predicate<E> eventFilter) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter, consumer, argSupplier);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with the event object and the argument supplied
     * by the argSupplier.
     */
    public <A> L listener(BiConsumer<E, A> consumer, Supplier<A> argSupplier) {
        Class<L> listenerClass = getListenerClass();
        return FunctionalListenerFactory.create(listenerClass, this, consumer, argSupplier);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with transformed event object and the argument supplied
     * by the argSupplier.
     */
    public <A, T> L listener(BiConsumer<T, A> consumer, Function<E, T> transformFunciton, Supplier<A> argSupplier) {
        return FunctionalListenerFactory.create(listenerClass, this,
                (E e, A p) -> {
                    T apply = transformFunciton.apply(e);
                    consumer.accept(apply, p);
                }, argSupplier);
    }

    /**
     * Returns an event listener instance that delegates each call to a supported method to the
     * given {@link BiConsumer} by invoking it with transformed event object and the argument supplied
     * by the argSupplier if the event is accepted by the eventFilter.
     */
    public <A, T> L listener(BiConsumer<T, A> consumer, Function<E, T> transformFunciton, Supplier<A> argSupplier,
                             Predicate<E> eventFilter) {
        return FunctionalListenerFactory.create(listenerClass, this, eventFilter,
                (E e, A p) -> consumer.accept(transformFunciton.apply(e), p), argSupplier);
    }


    @Override
    public String toString() {
        return getEventObjectClass().getName() +
                "[" + getEventMethods().stream().map(Method::getName).collect(Collectors.joining(", ")) +
                "]";
    }


    @SuppressWarnings("unchecked")
    protected <T> Class<T> resolveGenericClass(Class<?> clazz, int genericTypeArgIndex) {
        if (clazz == null) {
            return null;
        }

        Type genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return resolveGenericClass(clazz.getSuperclass(), genericTypeArgIndex);
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != 2) {
            return resolveGenericClass(clazz.getSuperclass(), genericTypeArgIndex);
        }

        Type listenerType = actualTypeArguments[genericTypeArgIndex];
        if (!(listenerType instanceof Class<?>)) {
            return resolveGenericClass(clazz.getSuperclass(), genericTypeArgIndex);
        }

        return (Class<T>) listenerType;
    }

    private static class EventMethodResolver implements Supplier<List<Method>> {

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
            Class<?> listenerClass = listenerClassSupplier.get();
            Class<?> eventClass = this.eventClassSupplier.get();
            if (methodNames.length == 0) {
                return resolveListenerMethods(listenerClass, eventClass);
            } else {
                return resolveListenerMethodsByName(methodNames, listenerClass, eventClass);
            }
        }

        private List<Method> resolveListenerMethods(Class<?> clazz, Class<?> eventObjectClass) {
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

        private List<Method> resolveListenerMethodsByName(String[] methodNames, Class<?> listenerClass, Class<?> eventObjectClass) {
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
    }
}
