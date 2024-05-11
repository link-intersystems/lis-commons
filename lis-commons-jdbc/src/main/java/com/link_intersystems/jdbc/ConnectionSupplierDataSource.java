package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * The ConnectionSupplierDataSource class is an implementation of the DataSource interface that uses a
 * {@link ConnectionSupplier} to obtain a Connection object.
 * <p>
 * The ConnectionSupplierDataSource constructor accepts a {@link ConnectionSupplier} object, which is
 * responsible for providing the Connection object. The {@link ConnectionSupplier} interface defines
 * a single get() method that should be implemented by its concrete implementations to establish a
 * database connection and return a {@link Connection} object. If an error occurs during the
 * process of obtaining a {@link Connection}, a SQLException should be thrown.
 * <p>
 * The getConnection() method of ConnectionSupplierDataSource overrides the getConnection() method of the
 * DataSource interface. It calls the get() method of the connectionSupplier
 * object to obtain a Connection object.
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
 *
 * // Create an instance of ConnectionSupplierDataSource with a custom ConnectionSupplier.
 * ConnectionSupplierDataSource dataSource = new ConnectionSupplierDataSource(new MyConnectionSupplier());
 *
 * // Use the dataSource to obtain a Connection object.
 * Connection connection = dataSource.getConnection();
 * </pre>
 *
 * @see DataSource
 * @see ConnectionSupplier
 * @see AbstractDataSource
 */
public class ConnectionSupplierDataSource extends AbstractDataSource {

    private ConnectionSupplier connectionSupplier;

    public ConnectionSupplierDataSource(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = requireNonNull(connectionSupplier);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionSupplier.get();
    }

}
