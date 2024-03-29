package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.db.h2.H2Database;
import com.link_intersystems.jdbc.test.db.h2.FileH2DatabaseFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaH2DatabaseFactory extends FileH2DatabaseFactory {

    private String classifier;

    public SakilaH2DatabaseFactory() {
    }

    public SakilaH2DatabaseFactory(String classifier) {
        this.classifier = classifier;
    }

    @Override
    protected void customizeDatabase(H2Database h2Database) throws SQLException {
        h2Database.setSchema("sakila");
    }

    @Override
    protected InputStream getDatabaseFileTemplateResource() throws IOException {
        InputStream resourceAsStream = getResourceAsStream(classifier);
        return new BufferedInputStream(resourceAsStream);
    }

    protected InputStream getResourceAsStream(String classifier) throws IOException {
        return SakilaResources.getSakilaMvDb(classifier);
    }

}
