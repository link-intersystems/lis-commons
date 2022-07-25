package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FileScannerTest {

    private Path tmpDir;
    private FileScanner fileScanner;

    @BeforeEach
    void setUp(@TempDir Path tmpDir) throws IOException {
        this.tmpDir = tmpDir;
        Unzip.unzip(FileScannerTest.class.getResourceAsStream("/lis-commons-jdbc.zip"), tmpDir);

        fileScanner = new FileScanner(tmpDir.toFile());
    }

    @Test
    void baseDirFiles() {
        fileScanner.addFilePattern("pom.xml");

        PathMatches paths = fileScanner.scan();

        assertTrue(paths.containsMatch(Paths.get("pom.xml")));
    }

    @Test
    void subDirFiles() {
        fileScanner.addFilePattern("**/main/java/**/*List.java");

        PathMatches paths = fileScanner.scan();

        assertTrue(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java")));
        assertTrue(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java")));
        assertTrue(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java")));

        assertFalse(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java")));
    }

    @Test
    void dirs() {
        fileScanner.addDirectoryPatterns("**");

        PathMatches paths = fileScanner.scan();

        assertTrue(paths.containsMatch(Paths.get("src/main/java")));
        assertTrue(paths.containsMatch(Paths.get("src/main")));
        assertTrue(paths.containsMatch(Paths.get("src/")));

        assertFalse(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java")));
    }
}
