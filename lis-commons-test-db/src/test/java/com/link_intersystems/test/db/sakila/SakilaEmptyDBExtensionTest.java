package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaEmptyTestDBExtension.class)
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

