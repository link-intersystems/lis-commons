package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * ConnectionDelegate is an abstract class that implements the Connection interface.
 * It acts as a delegate for another Connection object, forwarding all method calls to the underlying connection.get().
 */
public class SuppliedConnectionDelegate extends AbstractConnectionDelegate {
    private ConnectionSupplier connectionSupplier;

    /**
     * Uses the given connection as it's delegate connection.
     *
     * @param connection
     */
    public SuppliedConnectionDelegate(Connection connection) {
        this(() -> connection);
    }

    /**
     * Uses the {@link Connection} supplied by the {@link ConnectionSupplier} as it's delegate connection.
     *
     * @param connectionSupplier
     */
    public SuppliedConnectionDelegate(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = requireNonNull(connectionSupplier);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return connectionSupplier.get();
    }

}
