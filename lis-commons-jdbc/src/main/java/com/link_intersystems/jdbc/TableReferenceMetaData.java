package com.link_intersystems.jdbc;

import java.sql.SQLException;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TableReferenceMetaData {
    TableReferenceList getOutgoingReferences(String tableName) throws SQLException;

    TableReferenceList getIncomingReferences(String tableName) throws SQLException;
}
