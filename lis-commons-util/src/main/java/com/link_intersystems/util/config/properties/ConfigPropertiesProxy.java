package com.link_intersystems.util.config.properties;

import java.lang.reflect.*;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toUpperCase;
import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ConfigPropertiesProxy implements InvocationHandler {

    public static <T> T create(ConfigProperties configProperties, Class<T> configPropertyInterface) {
        Thread thread = Thread.currentThread();
        ClassLoader contextClassLoader = thread.getContextClassLoader();

        return (T) Proxy.newProxyInstance(contextClassLoader, new Class<?>[]{configPropertyInterface}, new ConfigPropertiesProxy(configProperties));
    }

    private static interface PropertyAccess {
        public Object invoke(ConfigProperties configProperties, Object value);
    }

    private ConfigProperties configProperties;

    public ConfigPropertiesProxy(ConfigProperties configProperties) {
        this.configProperties = requireNonNull(configProperties);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.equals(Object.class)) {
            return invokeObjectMethod(method, args);
        }

        PropertyAccess propertyAccess = getPropertyAccess(method);

        if (propertyAccess != null) {
            Object arg = args == null || args.length == 0 ? null : args[0];
            return propertyAccess.invoke(configProperties, arg);
        }

        String msg = format("Can not resolve property for ''{0}''", method);
        throw new IllegalStateException(msg);
    }

    private Object invokeObjectMethod(Method method, Object[] args) {
        String methodName = method.getName();

        switch (methodName) {
            case "equals":
                Object equalArg = args[0];
                if (equalArg != null && Proxy.isProxyClass(equalArg.getClass())) {
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(equalArg);
                    if (invocationHandler == this) {
                        return equals(invocationHandler);
                    }
                }
                return equals(equalArg);
            case "hashCode":
                return hashCode();
            case "toString":
                return toString();
            default:
                return null;
        }
    }

    private PropertyAccess getPropertyAccess(Method method) {
        String propertyName = getPropertyName(method);
        if (propertyName == null) {
            return null;
        }

        ConfigProperty<Object> configProperty = getConfigProperty(method.getDeclaringClass(), toConstantName(propertyName));
        if (configProperty == null) {
            return null;
        }

        if (isGetter(method)) {
            return (configProperties, value) -> configProperties.getProperty(configProperty);
        } else {
            return (configProperties, value) -> {
                configProperties.setProperty(configProperty, value);
                return null;
            };
        }
    }

    private String toConstantName(String propertyName) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < propertyName.length(); i++) {
            char charAt = propertyName.charAt(i);
            if (isUpperCase(charAt)) {
                sb.append("_");
            }
            sb.append(toUpperCase(charAt));
        }

        return sb.toString();
    }

    private static boolean isGetter(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            return false;
        }

        String name = method.getName();
        return name.startsWith("get") || name.startsWith("is");
    }

    private String getPropertyName(Method method) {
        String name = method.getName();

        String propertyName = null;

        if (isGetter(method)) {
            Class<?> returnType = method.getReturnType();
            if (Boolean.TYPE.equals(returnType)) {
                propertyName = name.substring(2);
            } else {
                propertyName = name.substring(3);
            }

        } else if (isSetter(name)) {
            propertyName = name.substring(3);
        }

        if (propertyName != null) {
            propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
        }

        return propertyName;
    }

    private static boolean isSetter(String name) {
        return name.startsWith("set");
    }

    private ConfigProperty<Object> getConfigProperty(Class<?> clazz, String constantName) {
        try {
            Field declaredField = clazz.getDeclaredField(constantName);
            if (Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType().isAssignableFrom(ConfigProperty.class)) {
                return (ConfigProperty<Object>) declaredField.get(clazz);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                ConfigProperty<Object> configProperty = getConfigProperty(anInterface, constantName);
                if (configProperty != null) {
                    return configProperty;
                }
            }
        }
        return null;
    }

}
