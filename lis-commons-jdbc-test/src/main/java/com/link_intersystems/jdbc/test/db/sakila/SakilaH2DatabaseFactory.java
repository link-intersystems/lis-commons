package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.H2Database;
import com.link_intersystems.jdbc.test.db.FileH2DatabaseFactory;

import java.io.BufferedInputStream;
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
    protected InputStream getDatabaseFileTemplateResource() {
        InputStream resourceAsStream = getResourceAsStream(classifier);
        return new BufferedInputStream(resourceAsStream);
    }

    protected InputStream getResourceAsStream(String classifier) {
        String resourceName = "sakila";
        if (classifier != null) {
            resourceName += "-" + classifier;
        }
        resourceName += ".mv.db";
        return SakilaH2DatabaseFactory.class.getResourceAsStream(resourceName);
    }
}
