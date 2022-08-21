package com.link_intersystems.jdbc.test.db.sakila;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@SakilaEmptyExtension
public class SakilaEmptyDBExtensionTest extends AbstractDBExtensionTest {

    public static Stream<String> tableNames() {
        return new SakilaEmptyDB().getTableNames().stream();
    }

    @ParameterizedTest
    @MethodSource("tableNames")
    void languageCount(String tableName) throws SQLException {
        dbAssertions.assertRowCount(tableName, 0);
    }

}

