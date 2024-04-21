package com.link_intersystems.io.file;


import com.link_intersystems.io.IOConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest {

    @Test
    void createDir(@TempDir File tempDir) throws IOException {
        Directory newDir = new Directory(new File(tempDir, "newDir"));

        newDir.create();
        org.assertj.core.api.Assertions.assertThat(newDir.getPath()).exists();
    }

    @Test
    void createDirIfExist(@TempDir File tempDir) throws IOException {
        File dir = new File(tempDir, "newDir");
        Files.createDirectories(dir.toPath());
        org.assertj.core.api.Assertions.assertThat(dir.toPath()).exists();

        Directory newDir = new Directory(dir);
        newDir.create();

        org.assertj.core.api.Assertions.assertThat(newDir.getPath()).exists();
    }

    @Test
    void getParent(@TempDir File tempDir) throws IOException {
        createDir(tempDir);

        Directory newDir = new Directory(new File(tempDir, "newDir"));

        Directory parentDirectory = newDir.getParent();
        assertNotNull(parentDirectory);

        Directory directory = new Directory(tempDir);
        assertEquals(directory, parentDirectory);
    }

    @Test
    void createFile(@TempDir File tempDir) throws IOException {

        Directory directory = new Directory(tempDir);
        RegularFile readme = directory.file("README.md");
        assertNotNull(readme);
        readme.write(IOConsumer.noop());
        assertTrue(new File(tempDir, "README.md").isFile());
        assertEquals(tempDir.toPath(), readme.getPath().getParent());
    }

    @Test
    void listFiles(@TempDir File tempDir) throws IOException {
        File src = new File(tempDir, "src");
        assertTrue(src.mkdirs());
        File main = new File(src, "main");
        assertTrue(main.mkdirs());
        File java = new File(main, "java");
        assertTrue(java.mkdirs());
        File mainJava = new File(java, "Main.java");
        assertTrue(mainJava.createNewFile());

        File readme = new File(tempDir, "README.md");
        assertTrue(readme.createNewFile());

        File license = new File(tempDir, "LICENSE.md");
        assertTrue(license.createNewFile());


        Directory directory = new Directory(tempDir);

        List<RegularFile> files = directory.listFiles();

        assertEquals(2, files.size());

    }
}