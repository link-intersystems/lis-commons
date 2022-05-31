package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.UnitTest;
import com.link_intersystems.test.jdbc.RowAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaTestDBExtension.class)
@UnitTest
public class SakilaTestDBExtensionTest extends AbstractDBExtensionTest {


    @Test
    void languageCount() throws SQLException {
        dbAssertions.assertRowCount("language", 6);
    }

    @Test
    void actorTest() throws SQLException {
        RowAssertions rowAssertions = dbAssertions.assertRowExists("actor", 1);

        rowAssertions.assertColumnValue("actor_id", 1);
        rowAssertions.assertColumnValue("first_name", "PENELOPE");
        rowAssertions.assertColumnValue("last_name", "GUINESS");
    }

}

