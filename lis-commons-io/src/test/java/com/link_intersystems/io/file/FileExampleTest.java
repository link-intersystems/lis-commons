package com.link_intersystems.io.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

public class FileExampleTest {

    @Test
    void generateAGradleProject(@TempDir Path tempDir) throws IOException {
        Directory project = new Directory(tempDir.resolve("project"));
        RegularFile settings = project.file("settings.gradle.kts");
        settings.write(content -> {
            content.append("plugins {\n");
            content.append("plugins {\n");
        });
    }
}
