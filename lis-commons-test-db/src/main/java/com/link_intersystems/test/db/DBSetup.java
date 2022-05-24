package com.link_intersystems.test.db;

import com.link_intersystems.test.jdbc.SqlScript;

import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface DBSetup {

    public List<String> getTableNames();

    public SqlScript getSchemaScript();

    public SqlScript getDdlScript();

    public SqlScript getDataScript();

    String getDefaultSchema();
}
