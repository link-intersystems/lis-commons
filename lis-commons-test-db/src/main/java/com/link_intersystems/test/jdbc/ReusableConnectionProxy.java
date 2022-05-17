package com.link_intersystems.test.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ReusableConnectionProxy implements InvocationHandler {

    public static Connection createProxy(Connection connection) {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class<?>[]{Connection.class}, new ReusableConnectionProxy(connection));
    }

    private Connection connection;

    private ReusableConnectionProxy(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            return null;
        }
        return method.invoke(connection, args);
    }
}
