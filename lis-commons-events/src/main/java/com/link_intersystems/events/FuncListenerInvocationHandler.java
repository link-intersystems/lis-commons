package com.link_intersystems.events;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.EventObject;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

class FuncListenerInvocationHandler<L, E extends EventObject, P> implements InvocationHandler {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private EventMethod<L, E> eventMethod;
    private BiConsumer<E, P> eventConsumer;
    private Supplier<P> paramSupplier;
    private Predicate<E> eventFilter;

    FuncListenerInvocationHandler(EventMethod<L, E> eventMethod, Predicate<E> eventFilter,
                                  BiConsumer<E, P> eventConsumer, Supplier<P> paramSupplier) {
        this.eventMethod = eventMethod;
        this.eventFilter = eventFilter;
        this.eventConsumer = eventConsumer;
        this.paramSupplier = paramSupplier;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args = nulsafeArgs(args);

        if (method.getName().equals("equals") && args.length == 1 && method.getReturnType().equals(Boolean.TYPE)) {
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

        } else if (method.getName().equals("hashCode") && args.length == 0
                && method.getReturnType().equals(Integer.TYPE)) {
            return Objects.hashCode(this);
        }

        if (eventMethod.test(method)) {
            E eventObject = eventMethod.getEventObject(args);
            if (eventFilter == null || eventFilter.test(eventObject)) {
                P param = paramSupplier.get();
                eventConsumer.accept(eventObject, param);
            }
        }
        return null;
    }

    private Object[] nulsafeArgs(Object[] args) {
        if (args == null) {
            args = EMPTY_ARGS;
        }
        return args;
    }

}
