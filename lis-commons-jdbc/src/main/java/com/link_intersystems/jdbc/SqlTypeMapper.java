package com.link_intersystems.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface SqlTypeMapper {

    public Object toObject(ResultSet resultSet, int columnIndex) throws SQLException;
}
