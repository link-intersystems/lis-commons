package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.db.H2DatabaseConfig;
import com.link_intersystems.jdbc.test.db.H2TestDBExtension;
import com.link_intersystems.test.UnitTest;
import com.link_intersystems.jdbc.test.RowAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
@H2DatabaseConfig(databaseFactory = SakilaSlimDatabaseFactory.class)
public class SakilaTestDBExtensionTest extends AbstractDBExtensionTest {

    @RegisterExtension
    static H2TestDBExtension extension = new H2TestDBExtension();

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

