package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ConnectionSupplier is an interface that provides a way to obtain a {@link Connection} object.
 * Implementations of this interface should define the logic to establish a database connection and return a Connection object.
 * If an error occurs during the process of obtaining a Connection, a SQLException should be thrown.
 * <p>
 * This interface is often used in conjunction with a DataSource implementation, where the DataSource calls the ConnectionSupplier's get() method to obtain a Connection.
 * <p>
 * Example Usage:
 * <pre>
 * // Custom implementation of ConnectionSupplier.
 * public class MyConnectionSupplier implements ConnectionSupplier {
 *    public Connection get() throws SQLException {
 *    // Custom logic to obtain a Connection object.
 *    ...
 *    }
 * }
 * </pre>
 *
 * @see ConnectionSupplierDataSource
 */
@FunctionalInterface
public interface ConnectionSupplier {

    public Connection get() throws SQLException;
}
