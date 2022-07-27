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
import java.util.List;

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

        List<FilePath> paths = fileScanner.scan();

        assertTrue(new PathMatches(paths).containsMatch(Paths.get("pom.xml")));
    }

    @Test
    void subDirFiles() {
        fileScanner.addFilePattern("**/main/java/**/*List.java");

        List<FilePath> paths = fileScanner.scan();
        PathMatches filePaths = new PathMatches(paths);
        assertTrue(filePaths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java")));
        assertTrue(filePaths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java")));
        assertTrue(filePaths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java")));

        assertFalse(filePaths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java")));
    }

    @Test
    void dirs() {
        fileScanner.addDirectoryPatterns("**");

        List<FilePath> paths = fileScanner.scan();
        PathMatches filePaths = new PathMatches(paths);
        assertTrue(filePaths.containsMatch(Paths.get("src/main/java")));
        assertTrue(filePaths.containsMatch(Paths.get("src/main")));
        assertTrue(filePaths.containsMatch(Paths.get("src/")));

        assertFalse(filePaths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java")));
    }
}
