package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        new FilePathAssertions(paths).assertContains("pom.xml");
    }

    @Test
    void dirs() {
        fileScanner.addDirectoryPatterns("**");

        List<FilePath> paths = fileScanner.scan();
        FilePathAssertions filePaths = new FilePathAssertions(paths);
        filePaths.assertContains("src/main/java");
        filePaths.assertContains("src/main");
        filePaths.assertContains("src/");

        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
    }


    @Test
    void subDirFiles() {
        fileScanner.addFilePattern("**/main/java/**/*List.java");

        List<FilePath> paths = fileScanner.scan();
        FilePathAssertions filePaths = new FilePathAssertions(paths);
        filePaths.assertContains("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        filePaths.assertContains("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java");
        filePaths.assertContains("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java");

        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
    }


    @Test
    void excludeFiles() {
        subDirFiles();
        fileScanner.addExcludeFilePatterns("**/ForeignKeyList.java");

        List<FilePath> paths = fileScanner.scan();
        FilePathAssertions filePaths = new FilePathAssertions(paths);
        filePaths.assertContains("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        filePaths.assertContains("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java");
        filePaths.assertContains("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java");

        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
    }

    @Test
    void excludeDirs() {
        subDirFiles();
        fileScanner.addExcludeDirectoryPatterns("**/jdbc");

        List<FilePath> paths = fileScanner.scan();
        FilePathAssertions filePaths = new FilePathAssertions(paths);
        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java");
        filePaths.assertNotContains("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java");
    }
}
