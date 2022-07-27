package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FilePathTest {

    private Path basepath;
    private Path somePath;
    private FilePath filePath;

    @BeforeEach
    void setUp(){
        basepath = Paths.get("C:/users/rene.link/");
        somePath = Paths.get("documents/clean-code.pdf");

        filePath = new FilePath(basepath, somePath);
    }

    @Test
    void getPath() {
        assertEquals(somePath, filePath.getPath());
    }

    @Test
    void getBasepath() {
        assertEquals(basepath, filePath.getBasepath());
    }

    @Test
    void rebase(){
        FilePath rebasedFilePath = filePath.rebase("D:/books");
        assertEquals(Paths.get("D:/books/documents/clean-code.pdf"), rebasedFilePath.toAbsolutePath());
    }

    @Test
    void getAbsolutePath() {
        assertEquals(Paths.get("C:/users/rene.link/documents/clean-code.pdf"), filePath.toAbsolutePath());
    }
}