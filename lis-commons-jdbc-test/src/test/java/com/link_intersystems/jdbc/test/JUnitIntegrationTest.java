package com.link_intersystems.jdbc.test;

import com.link_intersystems.jdbc.test.db.h2.H2Database;
import com.link_intersystems.jdbc.test.db.sakila.SakilaEmptyExtension;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SakilaEmptyExtension
class JUnitIntegrationTest {

    @Test
    void injectParameters(Connection connection, H2Database database, DataSource dataSource) {
        assertNotNull(connection);
        assertNotNull(database);
        assertNotNull(dataSource);
    }
}
