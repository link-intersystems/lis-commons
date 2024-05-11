package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link java.util.concurrent.Callable} for {@link Connection} related actions. In contrast to the
 * {@link java.util.concurrent.Callable} interface this {@link #call(Connection)} method throws a {@link SQLException}
 * instead of a generic {@link Exception}.
 *
 * @param <T> The return type of the function call.
 */
@FunctionalInterface
public interface ConnectionCallable<T> {
    T call(Connection connection) throws SQLException;
}
