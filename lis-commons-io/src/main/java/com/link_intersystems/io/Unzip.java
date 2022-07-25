package com.link_intersystems.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Unzip {

    public static void unzip(Path source, Path target) throws IOException {
        unzip(source.toFile(), target);
    }

    public static void unzip(File source, Path target) throws IOException {
        unzip(new FileInputStream(source), target);
    }


    public static void unzip(InputStream source, Path target) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(source)) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                Path newPath = zipSlipProtect(zipEntry, target);

                if (newPath.getParent() != null) {
                    if (Files.notExists(newPath.getParent())) {
                        Files.createDirectories(newPath.getParent());
                    }
                }

                Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                zipEntry = zis.getNextEntry();

            }

            zis.closeEntry();

        }

    }

    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
}
