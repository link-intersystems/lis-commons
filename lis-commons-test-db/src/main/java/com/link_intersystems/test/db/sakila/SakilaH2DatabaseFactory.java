package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.db.H2DatabaseFactory;
import com.link_intersystems.test.jdbc.H2Database;
import com.link_intersystems.test.jdbc.H2JdbcUrl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaH2DatabaseFactory implements H2DatabaseFactory {

    private String classifier;

    public SakilaH2DatabaseFactory() {
    }

    public SakilaH2DatabaseFactory(String classifier) {
        this.classifier = classifier;
    }

    @Override
    public H2Database create(String databaseName) throws SQLException {
        H2JdbcUrl.Builder builder = new H2JdbcUrl.Builder();
        builder.setDatabaseName(databaseName);
        try {
            File databaseFile = createDatabaseFile(databaseName);
            Path absoluteDbPath = databaseFile.toPath().toAbsolutePath();
            String databaseFilePath = absoluteDbPath.toString();
            builder.setDatabaseFilePath(databaseFilePath);

            builder.setAutoCommit(false);
            H2Database h2Database = new H2Database(builder.build()) {
                @Override
                public void clear() throws SQLException {
                    executeStatement("ROLLBACK");
                }

                @Override
                public void close() throws SQLException {
                    super.close();
                    deleteDirectory(databaseFile);
                }

                private boolean deleteDirectory(File directoryToBeDeleted) {
                    File[] allContents = directoryToBeDeleted.listFiles();
                    if (allContents != null) {
                        for (File file : allContents) {
                            deleteDirectory(file);
                        }
                    }
                    return directoryToBeDeleted.delete();
                }
            };
            h2Database.setSchema("sakila");
            return h2Database;
        } catch (IOException e) {
            throw new SQLException(e);
        }

    }

    private File createDatabaseFile(String databaseName) throws IOException {
        Path tempDirectory = Files.createTempDirectory(databaseName);

        File dbFile = new File(tempDirectory.toFile(), databaseName + ".mv.db");
        String resourceName = "sakila";
        if (classifier != null) {
            resourceName += "-" + classifier;
        }
        resourceName += ".mv.db";
        copyResource(dbFile, resourceName);

        return tempDirectory.toFile();
    }

    private void copyResource(File outFile, String resName) throws IOException {
        try (InputStream inputStream = new BufferedInputStream(SakilaH2DatabaseFactory.class.getResourceAsStream(resName))) {
            byte[] buff = new byte[8192];
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {
                int read;
                while ((read = inputStream.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            }

        }
    }
}
