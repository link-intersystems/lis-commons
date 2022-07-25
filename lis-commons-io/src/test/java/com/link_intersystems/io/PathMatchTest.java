package com.link_intersystems.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class PathMatchTest {

    private Path basepath;
    private Path somePath;
    private PathMatch pathMatch;

    @BeforeEach
    void setUp(){
        basepath = Paths.get("C:\\users\\rene.link\\");
        somePath = Paths.get("documents/clean-code.pdf");

        pathMatch = new PathMatch(basepath, somePath);
    }

    @Test
    void getPath() {
        assertEquals(somePath, pathMatch.getPath());
    }

    @Test
    void getBasepath() {
        assertEquals(basepath, pathMatch.getBasepath());
    }

    @Test
    void getAbsolutePath() {
        assertEquals(basepath.resolve(somePath), pathMatch.getAbsolutePath());
    }
}