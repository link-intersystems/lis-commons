package com.link_intersystems.io.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFileTest {

    private static class TestFile extends AbstractFile {

        protected TestFile(Path path) {
            super(path);
        }

        @Override
        public void create() {

        }

        @Override
        public AbstractFile getParent() {
            return null;
        }
    }

    private AbstractFile abstractFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        abstractFile = new TestFile(tempDir);
    }

    @Test
    void exists() {
        assertTrue(abstractFile.exists());
    }

    @Test
    void testEquals(@TempDir Path unequalPath) {
        TestFile equal = new TestFile(abstractFile.getPath());
        TestFile unequal = new TestFile(unequalPath);

        assertEquals(abstractFile, equal);
        assertEquals(equal, abstractFile);
        assertNotEquals(equal, unequal);
        assertNotEquals(equal, null);
        assertNotEquals(equal, "");
    }

    @Test
    void testHashCode() {
        TestFile actual = new TestFile(abstractFile.getPath());

        assertEquals(abstractFile.hashCode(), actual.hashCode());
    }
}