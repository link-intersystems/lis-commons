package com.link_intersystems.io.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegularFileTest {

    @Test
    void create(@TempDir File tempDir) throws IOException {
        RegularFile regularFile = new RegularFile(new File(tempDir, "greetings.txt"));
        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).doesNotExist();

        regularFile.create();

        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).exists();
        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).isEmptyFile();
    }

    @Test
    void writeCreateParentDirs(@TempDir File tempDir) throws IOException {
        File noneExistingDirectory = new File(tempDir, "someDir/someOtherDir");
        writeNoneExistingFile(noneExistingDirectory);
    }

    @Test
    void writeNoneExistingFile(@TempDir File tempDir) throws IOException {
        RegularFile regularFile = createRegularFile(tempDir, "greetings.txt");

        regularFile.write(appender -> {
            appender.append("Hello\nRené!");
        });

        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).content(UTF_8).isEqualTo(
                "Hello\nRené!"
        );
    }

    private RegularFile createRegularFile(File tempDir, String filename) {
        File file = new File(tempDir, filename);
        return new RegularFile(file);
    }

    @Test
    void overwriteExistingFile(@TempDir File tempDir) throws IOException {
        writeNoneExistingFile(tempDir);

        RegularFile regularFile = createRegularFile(tempDir, "greetings.txt");

        regularFile.write(appender -> {
            appender.append("Hello\nLink!");
        });

        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).content(UTF_8).isEqualTo(
                "Hello\nLink!"
        );
    }

    @Test
    void tryWriteToADirectory(@TempDir File tempDir) {

        RegularFile regularFile = new RegularFile(tempDir);

        assertThrows(IOException.class, () -> regularFile.write(appender -> {
        }));
    }

    @Test
    void writeWithCharset(@TempDir File tempDir) throws IOException {
        RegularFile regularFile = createRegularFile(tempDir, "greetings.txt");

        regularFile.write(appender -> {
            appender.append("Hello\nRené!");
        }, UTF_16);

        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).content(UTF_16).isEqualTo(
                "Hello\nRené!"
        );
    }

    @Test
    void appendCreateParentDirs(@TempDir File tempDir) throws IOException {
        File noneExistingDirectory = new File(tempDir, "someDir/someOtherDir");
        appendToNoneExistingFile(noneExistingDirectory);
    }


    @Test
    void appendToNoneExistingFile(@TempDir File tempDir) throws IOException {
        RegularFile regularFile = createRegularFile(tempDir, "greetings.txt");

        regularFile.append(appender -> {
            appender.append("Hello\nRené!");
        });

        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).content(UTF_8).isEqualTo(
                "Hello\nRené!"
        );
    }

    @Test
    void appendToFile(@TempDir File tempDir) throws IOException {
        appendToNoneExistingFile(tempDir);

        RegularFile regularFile = createRegularFile(tempDir, "greetings.txt");

        regularFile.append(appender -> {
            appender.append("\nHello\nWorld!");
        });

        org.assertj.core.api.Assertions.assertThat(regularFile.getPath()).content(UTF_8).isEqualTo(
                "Hello\nRené!\nHello\nWorld!"
        );
    }

    @Test
    void appendToADirectory(@TempDir File tempDir) {

        RegularFile regularFile = new RegularFile(tempDir);

        assertThrows(IOException.class, () -> regularFile.write(appender -> {
        }));
    }

    @Test
    void appendContent(@TempDir File tempDir) throws IOException {
        RegularFile fileBuilder = createRegularFile(tempDir, "greetings.txt");

        fileBuilder.write(appender -> {
            appender.append("Hello\nRené!");
        });

        fileBuilder.append(appender -> {
            appender.append("\nHow are you?");
        });

        org.assertj.core.api.Assertions.assertThat(fileBuilder.getPath()).content(UTF_8).isEqualTo(
                "Hello\nRené!\nHow are you?"
        );
    }


    @Test
    void appendContentDifferentCharsets(@TempDir File tempDir) throws IOException {
        RegularFile fileBuilder = createRegularFile(tempDir, "greetings.txt");

        fileBuilder.write(appender -> {
            appender.append("Hello\nRené!");
        }, UTF_8);

        fileBuilder.append(appender -> {
            appender.append("\nHow are you René?");
        }, ISO_8859_1);

        org.assertj.core.api.Assertions.assertThat(fileBuilder.getPath()).content(UTF_8).isEqualTo(
                "Hello\nRené!\nHow are you Ren�?"
        );

        org.assertj.core.api.Assertions.assertThat(fileBuilder.getPath()).content(ISO_8859_1).isEqualTo(
                "Hello\nRenÃ©!\nHow are you René?"
        );
    }


}