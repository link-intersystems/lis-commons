package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FileScannerTest {

    private Path tmpPath;
    private FileScanner fileScanner;

    @BeforeEach
    void setUp(@TempDir Path tmpDir) throws IOException {
        this.tmpPath = tmpDir;
        Unzip.unzip(FileScannerTest.class.getResourceAsStream("/lis-commons-jdbc.zip"), tmpDir);

        fileScanner = new FileScanner();
    }

    @Test
    void scanNoDir() {
        File nonExistentDir = getNonExistentDir();

        assertThrows(IllegalArgumentException.class, () ->  fileScanner.scan(nonExistentDir));
    }

    private File getNonExistentDir() {
        File tmpDir = tmpPath.toFile().getAbsoluteFile();
        File nonExistentDir = new File(tmpDir, UUID.randomUUID().toString());
        while(nonExistentDir.exists()){
            nonExistentDir = new File(tmpDir, UUID.randomUUID().toString());
        }
        return nonExistentDir;
    }

    @Test
    void baseDirFiles() {
        fileScanner.addIncludeFilePattern("pom.xml");

        List<File> files = fileScanner.scan(tmpPath);

        new FileAssertions(files).assertOneEndsWith("pom.xml");
    }

    @Test
    void copyConstructor() {
        fileScanner.addIncludeFilePattern("pom.xml");

        FileScanner copyFileScanner = new FileScanner(this.fileScanner);

        List<File> files = copyFileScanner.scan(tmpPath);

        new FileAssertions(files).assertOneEndsWith("pom.xml");
    }

    @Test
    void dirs() {
        fileScanner.addIncludeDirectoryPatterns("**");

        List<File> paths = fileScanner.scan(tmpPath);
        FileAssertions fileAssertions = new FileAssertions(paths);
        fileAssertions.assertOneEndsWith("src/main/java");
        fileAssertions.assertOneEndsWith("src/main");
        fileAssertions.assertOneEndsWith("src/");

        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
    }


    @Test
    void subDirFiles() {
        fileScanner.addIncludeFilePattern("**/main/java/**/*List.java");

        List<File> paths = fileScanner.scan(tmpPath);
        FileAssertions fileAssertions = new FileAssertions(paths);
        fileAssertions.assertOneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        fileAssertions.assertOneEndsWith("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java");
        fileAssertions.assertOneEndsWith("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java");

        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
    }


    @Test
    void excludeFiles() {
        subDirFiles();
        fileScanner.addExcludeFilePatterns("**/ForeignKeyList.java");

        List<File> paths = fileScanner.scan(tmpPath);
        FileAssertions fileAssertions = new FileAssertions(paths);
        fileAssertions.assertOneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        fileAssertions.assertOneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java");
        fileAssertions.assertOneEndsWith("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java");

        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnDescription.java");
    }

    @Test
    void excludeDirs() {
        subDirFiles();
        fileScanner.addExcludeDirectoryPatterns("**/jdbc");

        List<File> paths = fileScanner.scan(tmpPath);
        FileAssertions fileAssertions = new FileAssertions(paths);
        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/ColumnMetaDataList.java");
        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/ForeignKeyList.java");
        fileAssertions.assertNoneEndsWith("src/main/java/com/link_intersystems/jdbc/TableReferenceList.java");
    }


}
