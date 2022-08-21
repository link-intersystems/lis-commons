package com.link_intersystems.jdbc.test.db.h2;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface H2ConfigProperties {

    public H2Factory getH2Factory();

    public DBSetup getDBSetup();

    public String getDatabaseName();
}
