package com.link_intersystems.events;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.EventObject;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

class FunctionalListenerInvocationHandler<L, E, P> implements InvocationHandler {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private EventMethod<L, E> eventMethod;
    private BiConsumer<E, P> eventConsumer;
    private Supplier<P> paramSupplier;
    private Predicate<E> eventFilter;

    FunctionalListenerInvocationHandler(EventMethod<L, E> eventMethod, Predicate<E> eventFilter,
                                        BiConsumer<E, P> eventConsumer, Supplier<P> paramSupplier) {
        this.eventMethod = eventMethod;
        this.eventFilter = eventFilter;
        this.eventConsumer = eventConsumer;
        this.paramSupplier = paramSupplier;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        args = nullSafeArgs(args);

        if (isEqualsMethod(method, args)) {
            Object object = args[0];
            if (object == null) {
                return false;
            }

            boolean proxyClass = Proxy.isProxyClass(object.getClass());
            if (proxyClass) {
                InvocationHandler otherInvocationHandler = Proxy.getInvocationHandler(object);
                return Objects.equals(this, otherInvocationHandler);
            } else {
                return false;
            }

        } else if (isHashCodeMethod(method, args)) {
            return Objects.hashCode(this);
        }

        if (methodAccepted(method)) {
            E eventObject = eventMethod.getEventObject(args);
            if (eventAccepted(eventObject)) {
                P param = paramSupplier.get();
                eventConsumer.accept(eventObject, param);
            }
        }

        Class<?> returnType = method.getReturnType();
        if (returnType.equals(Void.TYPE) || returnType.equals(Void.class)) {
            return null;
        } else {
            String msg = "Listener method returns " + returnType + ", but listener methods" +
                    " should usually declare void as their return type.";
            throw new UnsupportedOperationException(msg);
        }
    }

    private boolean eventAccepted(E eventObject) {
        return eventFilter == null || eventFilter.test(eventObject);
    }

    private boolean methodAccepted(Method method) {
        return eventMethod.test(method);
    }

    private boolean isEqualsMethod(Method method, Object[] args) {
        return method.getName().equals("equals") && args.length == 1 && method.getReturnType().equals(Boolean.TYPE);
    }

    private boolean isHashCodeMethod(Method method, Object[] args) {
        return method.getName().equals("hashCode") && args.length == 0
                && method.getReturnType().equals(Integer.TYPE);
    }

    private Object[] nullSafeArgs(Object[] args) {
        if (args == null) {
            args = EMPTY_ARGS;
        }
        return args;
    }

}
