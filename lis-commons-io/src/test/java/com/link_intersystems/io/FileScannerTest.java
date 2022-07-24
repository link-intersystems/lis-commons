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

    @BeforeEach
    void setUp(@TempDir Path tmpDir) throws IOException {
        this.tmpDir = tmpDir;
        Unzip.unzip(FileScannerTest.class.getResourceAsStream("/lis-commons-jdbc.zip"), tmpDir);
    }

    @Test
    void pathMatcherTest() {
        Path path = Paths.get("src/main/java/test.java");
        Path base = Paths.get("src/main");

        URI relativized = base.toUri().relativize(path.toUri());
        Path relativizedPath = Paths.get(relativized.getPath());

        PathMatcher pomXmlMatcher = FileSystems.getDefault().getPathMatcher("glob:pom.xml");
        assertEquals(Paths.get("java/test.java"), relativizedPath);
    }

    @Test
    void baseDirFiles() {
        FileScanner fileScanner = new FileScanner(tmpDir);
        fileScanner.addFilePattern("pom.xml");

        PathMatches paths = fileScanner.scan();

        assertTrue(paths.containsMatch(Paths.get("pom.xml")));
    }

    @Test
    void subDirFiles() {
        FileScanner fileScanner = new FileScanner(tmpDir);
        fileScanner.addFilePattern("**/main/java/**/*List.java");

        PathMatches paths = fileScanner.scan();

        assertTrue(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java")));
        assertTrue(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java")));
        assertTrue(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java")));

        assertFalse(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java")));
    }

    @Test
    void dirs() {
        FileScanner fileScanner = new FileScanner(tmpDir);
        fileScanner.addDirectoryPatterns("**");

        PathMatches paths = fileScanner.scan();

        assertTrue(paths.containsMatch(Paths.get("src/main/java")));
        assertTrue(paths.containsMatch(Paths.get("src/main")));
        assertTrue(paths.containsMatch(Paths.get("src/")));

        assertFalse(paths.containsMatch(Paths.get("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java")));
    }

    @Test
    void absolutePaths() {
        FileScanner fileScanner = new FileScanner(tmpDir);
        fileScanner.setAbsolutePaths(true);
        fileScanner.addDirectoryPatterns("**/main/java/**/*List.java");

        PathMatches paths = fileScanner.scan();

        Path absolutePath = Paths.get(tmpDir.toAbsolutePath().toString(), "/src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
        assertFalse(paths.containsMatch(absolutePath));
    }
}
