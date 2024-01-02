package com.link_intersystems.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class FileBuilderTest {

    private static final String TEST_CONTENT = "abcdefghijklmnopqrstuvwxyzéßöäü";

    private FileBuilder fileBuilder;

    @BeforeEach
    void setUp(@TempDir Path basepath) {

        fileBuilder = new FileBuilder(basepath);

        Assertions.assertEquals(basepath, fileBuilder.getPath());

    }

    @Test
    void writeFileByInputStream() throws IOException {
        Path file = fileBuilder.writeFileStream("test", new ByteArrayInputStream(TEST_CONTENT.getBytes(UTF_8)));

        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_8).isEqualTo(TEST_CONTENT);
    }

    @Test
    void writeFile() throws IOException {
        Path file = fileBuilder.writeFileStream("test", out -> {
            out.write(TEST_CONTENT.getBytes(UTF_8));
        });

        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_8).isEqualTo(TEST_CONTENT);
    }

    @Test
    void writeFileByStringAndCharset() throws IOException {
        Path file = fileBuilder.writeFile("test", TEST_CONTENT, UTF_16);

        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_16).isEqualTo(TEST_CONTENT);
    }

    @Test
    void writeFileByStringWithDefaultCharset() throws IOException {
        Path file = fileBuilder.writeFile("test", TEST_CONTENT);

        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_8).isEqualTo(TEST_CONTENT);
    }

    @Test
    void writeEmptyFile() throws IOException {
        Path file = fileBuilder.writeFile("test");

        assertThat(file).hasFileName("test");
        assertThat(file).content(UTF_8).isEmpty();
    }

}