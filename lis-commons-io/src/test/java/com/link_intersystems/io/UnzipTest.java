package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class UnzipTest {

    private Path zipFilePath;
    private Path tmpDir;

    @BeforeEach
    void setUp(@TempDir Path tmpDir) throws IOException {
        zipFilePath = tmpDir.resolve("test.zip");
        this.tmpDir = tmpDir;
        File  zipFile = zipFilePath.toFile();

        byte[] buff = new byte[8192];

        try (InputStream resourceAsStream = new BufferedInputStream(UnzipTest.class.getResourceAsStream("/lis-commons-jdbc.zip"))) {
            try (OutputStream outputStream = new FileOutputStream(zipFile)) {
                int read;

                while ((read = resourceAsStream.read(buff)) > 0) {
                    outputStream.write(buff, 0, read);
                }
            }
        }
    }

    @Test
    void unzip() throws IOException {
        Unzip.unzip(zipFilePath, tmpDir.resolve("unzipped"));
    }
}