package com.link_intersystems.jdbc;

import com.link_intersystems.jdbc.test.db.H2Config;
import com.link_intersystems.jdbc.test.db.H2Extension;
import com.link_intersystems.jdbc.test.db.sakila.SakilaEmptyDatabaseFactory;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(H2Extension.class)
@H2Config(databaseFactory = SakilaEmptyDatabaseFactory.class)
@UnitTest
class TableMetaDataTest {
    private ConnectionMetaData metaDataRepository;

    @BeforeEach
    void setUp(Connection connection) {
        JdbcContext.Builder builder = new JdbcContext.Builder();
        builder.setSchema("sakila");
        JdbcContext jdbcContext = builder.build();
        metaDataRepository = new ConnectionMetaData(connection, jdbcContext);
    }

    @Test
    void matches() throws SQLException {
        TableMetaData actorTableMetaData = metaDataRepository.getTableMetaData("actor");

        assertFalse(actorTableMetaData.matches(new QualifiedTableName( "act")));
        assertFalse(actorTableMetaData.matches(new QualifiedTableName("saki", "actor")));

        assertTrue(actorTableMetaData.matches(new QualifiedTableName("sakila", "actor")));
        assertTrue(actorTableMetaData.matches(new QualifiedTableName( "actor")));
    }
}