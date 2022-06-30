package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.H2Database;
import com.link_intersystems.jdbc.test.H2JdbcUrl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class FileH2DatabaseFactory implements H2DatabaseFactory {

    public static final String FILE_EXTENSION = ".mv.db";

    public static H2Database createFileDatabase(File databaseFile, String databaseName) throws SQLException {
        H2JdbcUrl.Builder builder = new H2JdbcUrl.Builder();

        builder.setDatabaseName(databaseName);

        Path absoluteDbPath = databaseFile.toPath().toAbsolutePath();
        String databaseFilePath = absoluteDbPath.toString();

        builder.setDatabaseFilePath(databaseFilePath);
        builder.setAutoCommit(false);
        H2Database h2Database = new H2Database(builder.build()) {
            @Override
            public void clear() throws SQLException {
                executeStatement("COMMIT");
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

        return h2Database;
    }



    @Override
    public H2Database create(String databaseName) throws SQLException {

        try {
            File databaseFile = createDatabaseFile(databaseName);
            H2Database h2Database = createFileDatabase(databaseFile, databaseName);
            customizeDatabase(h2Database);
            return h2Database;
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    protected void customizeDatabase(H2Database h2Database) throws SQLException {
    }

    protected File createDatabaseFile(String databaseName) throws IOException {
        Path tempDirectory = Files.createTempDirectory(databaseName);

        File dbFile = new File(tempDirectory.toFile(), databaseName + FILE_EXTENSION);

        try (InputStream databaseFileTemplateResource = getDatabaseFileTemplateResource()) {
            copyResource(dbFile, databaseFileTemplateResource);
        }

        return tempDirectory.toFile();
    }

    protected abstract InputStream getDatabaseFileTemplateResource() throws IOException;


    private void copyResource(File outFile, InputStream inputStream) throws IOException {
        byte[] buff = new byte[8192];
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {
            int read;
            while ((read = inputStream.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        }
    }
}
