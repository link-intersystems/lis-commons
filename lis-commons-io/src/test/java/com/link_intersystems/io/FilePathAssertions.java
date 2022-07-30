package com.link_intersystems.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FilePathAssertions extends AbstractList<FilePath> {

    List<FilePath> filePaths = new ArrayList<>();

    public FilePathAssertions(List<FilePath> filePaths) {
        this.filePaths.addAll(filePaths);
    }

    @Override
    public FilePath get(int index) {
        return filePaths.get(index);
    }

    @Override
    public int size() {
        return filePaths.size();
    }

    public void assertNotContains(String path) {
        assertNotContains(Paths.get(path));
    }

    public void assertNotContains(Path path) {
        assertFalse(isContained(path), containsMessage(path + " is not contained in:"));
    }

    public void assertContains(String path) {
        assertContains(Paths.get(path));
    }

    public void assertContains(Path path) {
        assertTrue(isContained(path), containsMessage(path + " is contained in:"));
    }

    private Supplier<String> containsMessage(String actualPathMessage) {
        return () -> {
            StringBuilder sb = new StringBuilder();

            sb.append(actualPathMessage);
            sb.append("\n");

            Iterator<FilePath> iterator = filePaths.iterator();
            while (iterator.hasNext()) {
                FilePath next = iterator.next();
                sb.append("\to ");
                sb.append(next);
                if (iterator.hasNext()) {
                    sb.append("\n");
                }
            }

            return sb.toString();
        };
    }

    private boolean isContained(Path path) {
        boolean contains = false;
        for (FilePath filePath : filePaths) {
            Path somePaths = filePath.getPath();
            contains = somePaths.equals(path);
            if (contains) {
                break;
            }
        }
        return contains;
    }


}
