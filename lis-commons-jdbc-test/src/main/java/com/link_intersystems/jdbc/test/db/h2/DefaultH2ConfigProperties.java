package com.link_intersystems.jdbc.test.db.h2;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;
import com.link_intersystems.jdbc.test.db.setup.NoDBSetup;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultH2ConfigProperties implements H2ConfigProperties {

    private H2Factory h2Factory = new DefaultH2Factory();
    private DBSetup dbSetup = new NoDBSetup();

    private String databaseName = "test";

    public void setH2Factory(H2Factory h2Factory) {
        this.h2Factory = requireNonNull(h2Factory);
    }

    public void setDBSetup(DBSetup dbSetup) {
        this.dbSetup = requireNonNull(dbSetup);
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = requireNonNull(databaseName);
    }

    @Override
    public H2Factory getH2Factory() {
        return h2Factory;
    }

    @Override
    public DBSetup getDBSetup() {
        return dbSetup;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
