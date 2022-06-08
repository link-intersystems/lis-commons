package com.link_intersystems.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface RowMapper<T> {

    public T map(ResultSet resultSet) throws SQLException;
}
