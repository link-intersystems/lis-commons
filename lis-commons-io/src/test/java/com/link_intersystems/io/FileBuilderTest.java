package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileBuilderTest {

    private static final String TEST_CONTENT = "abcdefghijklmnopqrstuvwxyzéßöäü";

    private FileBuilder fileBuilder;
    private Path tempDirPath;

    @BeforeEach
    void setUp(@TempDir Path tempDirPath) {
        this.tempDirPath = tempDirPath;

        fileBuilder = new FileBuilder(tempDirPath);

        assertEquals(tempDirPath, fileBuilder.getDirpath());

    }

    @Test
    void dirpathIsNotADirectory() throws IOException {
        Path test = tempDirPath.resolve("test");
        Path filepath = Files.createFile(test);

        assertThrows(IllegalArgumentException.class, () -> new FileBuilder(filepath));
    }

    @Test
    void createEmptyFile() throws IOException {
        Path file = fileBuilder.createFile("test");

        assertEquals(tempDirPath.resolve("test"), file);
        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_8).isEmpty();
    }

    @Test
    void createFile() throws IOException {
        Path file = fileBuilder.createFile("test", writer -> {
            writer.write(ByteBuffer.wrap("Hello World".getBytes(UTF_8)));
        });

        assertEquals(tempDirPath.resolve("test"), file);
        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_8).isEqualTo("Hello World");
    }

    @Test
    void createDirectory() throws IOException {
        FileBuilder dir1 = fileBuilder.mkdir("dir1");

        assertNotNull(dir1);
        Path expectedDirpath = tempDirPath.resolve("dir1");

        assertEquals(expectedDirpath, dir1.getDirpath());
    }
}