package com.link_intersystems.jdbc.test.db.h2;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AnnotationH2ConfigProperties implements H2ConfigProperties {

    private H2Config h2Config;

    private H2Factory h2Factory;
    private DBSetup dbSetup;

    public AnnotationH2ConfigProperties(H2Config h2Config) throws Exception {
        this.h2Config = requireNonNull(h2Config);

        h2Factory = h2Config.databaseFactory().newInstance();
        dbSetup = h2Config.databaseSetup().newInstance();
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
        return h2Config.databaseName();
    }
}
