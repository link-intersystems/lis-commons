package com.link_intersystems.util.composite;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.*;

public class CompositeProxy<T> implements InvocationHandler {

    public static <T> T create(Class<T> type, T... components) {
        return create(type, Arrays.asList(components));
    }

    public static <T> T create(Class<? super T> type, Collection<T> components) {
        return create(type, () -> components);
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<? super T> type, Supplier<Collection<T>> componentsSupplier) {
        CompositeProxy<T> compositeProxy = new CompositeProxy<>(type, componentsSupplier);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{type}, compositeProxy);
    }

    private final Class<? super T> type;
    private final Supplier<Collection<T>> componentsSupplier;

    public CompositeProxy(Class<? super T> type, Supplier<Collection<T>> componentsSupplier) {
        this.type = checkType(type);
        this.componentsSupplier = requireNonNull(componentsSupplier);
    }

    private static <T> Class<T> checkType(Class<T> type) {
        List<Method> nonVoidPublicMethods = findNonVoidInstanceMethods(requireNonNull(type));

        if (!nonVoidPublicMethods.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(CompositeProxy.class.getSimpleName());
            sb.append(" can only handle methods that return void, but ");
            sb.append(type.getName());
            sb.append(" declares methods that do not return void:\n");

            for (Method nonVoidPublicMethod : nonVoidPublicMethods) {
                sb.append("\t o ");
                sb.append(nonVoidPublicMethod.getReturnType().getName());
                sb.append(" ");
                sb.append(nonVoidPublicMethod.getName());
                sb.append("(");
                Iterator<Class<?>> parameterTypes = Arrays.asList(nonVoidPublicMethod.getParameterTypes()).iterator();
                while (parameterTypes.hasNext()) {
                    Class<?> parameterType = parameterTypes.next();
                    sb.append(parameterType.getName());
                    if (parameterTypes.hasNext()) {
                        sb.append(", ");
                    }

                }
                sb.append(")");
                sb.append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }

        return type;
    }

    private static <T> List<Method> findNonVoidInstanceMethods(Class<T> type) {
        Method[] declaredMethods = type.getDeclaredMethods();
        List<Method> nonVoidPublicMethods = Arrays.stream(declaredMethods).filter(CompositeProxy::isInstanteMethod).filter(m -> !isVoidReturn(m)).collect(Collectors.toList());

        List<Class<?>> interfaces = Arrays.asList(type.getInterfaces());
        if (!interfaces.isEmpty()) {
            for (Class<?> anInterface : interfaces) {
                List<Method> superTypeMethods = findNonVoidInstanceMethods(anInterface);
                nonVoidPublicMethods.addAll(superTypeMethods);
            }
        }

        return nonVoidPublicMethods;
    }

    private static boolean isInstanteMethod(Method m) {
        int modifiers = m.getModifiers();
        return Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers);
    }

    private static boolean isVoidReturn(Method m) {
        Class<?> returnType = m.getReturnType();
        return void.class == returnType || Void.class == returnType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isEquals(method)) {
            return equals(args[0]);
        }

        if (isHashCode(method)) {
            return hashCode();
        }

        Collection<T> components = componentsSupplier.get();

        for (T component : components) {
            try {
                method.invoke(component, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        return null;
    }


    private boolean isEquals(Method method) {
        return "equals".equals(method.getName()) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Object.class;
    }

    private boolean isHashCode(Method method) {
        return "hashCode".equals(method.getName()) && method.getParameterTypes().length == 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oClass = o.getClass();
        if (!Proxy.isProxyClass(oClass)) return false;
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(o);
        if (invocationHandler.getClass() != CompositeProxy.class) return false;
        CompositeProxy<?> that = (CompositeProxy<?>) invocationHandler;
        return Objects.equals(type, that.type) && componentsEqual(that);
    }

    private boolean componentsEqual(CompositeProxy<?> that) {
        Collection<?> thatComponents = that.componentsSupplier.get();
        Collection<T> thisComponents = componentsSupplier.get();
        return Objects.equals(thisComponents, thatComponents);
    }

    @Override
    public int hashCode() {
        Collection<T> collection = componentsSupplier.get();
        return hash(type, collection);
    }
}
