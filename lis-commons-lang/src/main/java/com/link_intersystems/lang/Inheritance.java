package com.link_intersystems.lang;

import java.util.function.*;

public class Inheritance {

    public static final Inheritance INSTANCE = new Inheritance();

    public <E, T> void ifInstance(E element, Class<T> type, Consumer<T> consumer) {
        if (isInstance(element, type)) {
            consumer.accept(cast(element, type));
        }
    }

    public <E, T, U> void ifInstance(E element, Class<T> type, BiConsumer<T, U> consumer,
                                     Supplier<U> paramSource) {
        if (isInstance(element, type)) {
            U u = paramSource.get();
            T casted = cast(element, type);
            consumer.accept(casted, u);
        }
    }

    public <E, T, R> R ifInstanceCall(E element, Class<T> type, Function<T, R> function) {
        return ifInstanceCall(element, type, function, null);
    }

    public <E, T, R> R ifInstanceCall(E element, Class<T> type, Function<T, R> function,
                                      R ifNotAdabtable) {
        if (isInstance(element, type)) {
            T casted = cast(element, type);
            return function.apply(casted);
        }
        return ifNotAdabtable;
    }

    public <E, T, U, R> R ifInstanceCall(E element, Class<T> type, BiFunction<T, U, R> function,
                                         Supplier<U> paramSource) {
        if (isInstance(element, type)) {
            U param = paramSource.get();
            T casted = cast(element, type);
            return function.apply(casted, param);
        }
        return null;
    }

    private boolean isInstance(Object element, Class<?> type) {
        return type.isInstance(element);
    }

    protected <T> T cast(Object element, Class<T> type) {
        if (element == null) {
            return null;
        }

        return type.cast(element);
    }

}
