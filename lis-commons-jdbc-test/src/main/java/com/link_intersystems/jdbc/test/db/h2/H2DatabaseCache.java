package com.link_intersystems.jdbc.test.db.h2;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;

import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2DatabaseCache {

    private H2ConfigPropertiesSource configPropertiesSource;

    public H2DatabaseCache(H2ConfigPropertiesSource configPropertiesSource) {
        this.configPropertiesSource = requireNonNull(configPropertiesSource);
    }

    public H2Database getDatabase(H2DatabaseStore h2DatabaseStore) throws Exception {
        H2Database h2Database = h2DatabaseStore.get();

        if (h2Database == null) {
            H2ConfigProperties configProperties = configPropertiesSource.getConfigProperties();
            h2Database = createDatabase(configProperties);
            h2DatabaseStore.put(h2Database);
        }

        return h2Database;
    }

    protected H2Database createDatabase(H2ConfigProperties configProperties) throws SQLException {
        H2Factory h2DatabaseFactory = configProperties.getH2Factory();
        String databaseName = configProperties.getDatabaseName();
        H2Database h2Database = h2DatabaseFactory.create(databaseName);

        DBSetup dbSetup = configProperties.getDBSetup();
        try (Connection connection = h2Database.getConnection()) {
            dbSetup.setupSchema(connection);
            dbSetup.setupDdl(connection);
            dbSetup.setupData(connection);
        }

        return h2Database;
    }


    public void removeDatabase(H2DatabaseStore h2DatabaseStore) throws Exception {
        clearDatabase(h2DatabaseStore);

        H2Database h2Database = h2DatabaseStore.get();
        if (h2Database != null) {
            h2Database.close();

        }

        h2DatabaseStore.remove();
    }

    public void clearDatabase(H2DatabaseStore h2DatabaseStore) throws Exception {
        H2Database h2Database = h2DatabaseStore.get();
        if (h2Database != null) {
            h2Database.clear();
        }
    }

}

