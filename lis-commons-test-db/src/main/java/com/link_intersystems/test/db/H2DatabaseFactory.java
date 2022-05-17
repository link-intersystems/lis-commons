package com.link_intersystems.test.db;

import com.link_intersystems.test.jdbc.H2Database;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface H2DatabaseFactory {
    H2Database create() throws SQLException, IOException;
}
