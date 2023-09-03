package com.link_intersystems.jdbc.test.db.sakila;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class SakilaResources {
    private static final String PACKAGE_PATH = SakilaResources.class.getPackageName().replaceAll("\\.", "/");

    public static Reader getSakilaDdlSql() throws IOException {
        return new InputStreamReader(new GZIPInputStream(getResourceAsStream("sakila-ddl.sql")), StandardCharsets.UTF_8);
    }

    public static Reader getSlimDataSql() throws IOException {
        return new InputStreamReader(new GZIPInputStream(getResourceAsStream("sakila-slim-db.sql")), StandardCharsets.UTF_8);
    }

    public static Reader getSakilaDataSql() throws IOException {
        return new InputStreamReader(new GZIPInputStream(getResourceAsStream("sakila-db.sql")), StandardCharsets.UTF_8);
    }

    public static Reader getSakilyTinyDataSql() throws IOException {
        return new InputStreamReader(new GZIPInputStream(getResourceAsStream("sakila-tiny-db.sql")), StandardCharsets.UTF_8);
    }

    public static InputStream getSakilaMvDb(String classifier) throws IOException {
        String resourceName = "sakila";
        if (classifier != null) {
            resourceName += "-" + classifier;
        }
        resourceName += ".mv.db";
        return new GZIPInputStream(getResourceAsStream(resourceName));
    }

    private static InputStream getResourceAsStream(String resourceName) throws IOException {
        ClassLoader classLoader = SakilaResources.class.getClassLoader();
        String resourceBaseName = PACKAGE_PATH + "/" + resourceName;
        URL resource = classLoader.getResource(resourceBaseName + ".gz");
        if (resource == null) {
            resource = classLoader.getResource(resourceBaseName);
        }
        return resource.openStream();
    }

}
