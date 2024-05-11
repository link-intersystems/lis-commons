package com.link_intersystems.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class AbstractConnectionDelegateTest {

    private AbstractConnectionDelegate delegate;
    private Connection connectionMock;

    static Stream<Method> delegationMethods() {
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(Connection.class.getDeclaredMethods()));
        methods.addAll(Arrays.asList(Wrapper.class.getDeclaredMethods()));
        return methods.stream().filter(ModifierSupport::isNotStatic).filter(ModifierSupport::isPublic);
    }

    @BeforeEach
    void setUp() {
        connectionMock = mock(Connection.class);

        delegate = createConnectionDelegate(connectionMock);
    }

    protected AbstractConnectionDelegate createConnectionDelegate(Connection targetConnection) {
        return new AbstractConnectionDelegate() {
            @Override
            protected Connection getConnection() throws SQLException {
                return targetConnection;
            }
        };
    }

    @ParameterizedTest
    @MethodSource("delegationMethods")
    void methodDelegation(Method method) throws InvocationTargetException, IllegalAccessException {
        Object[] args = getTestArgs(method);
        method.invoke(delegate, args);

        method.invoke(verify(connectionMock, times(1)), args);
    }

    private Object[] getTestArgs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = testObject(parameterTypes[i]);
        }

        return args;
    }

    private Object testObject(Class<?> type) {
        if (boolean.class.equals(type)) {
            return true;
        } else if (int.class.equals(type)) {
            return 1;
        }
        return null;
    }

}