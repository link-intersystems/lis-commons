package com.link_intersystems.io;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FileAssertions {

    List<File> filePaths = new ArrayList<>();

    public FileAssertions(List<File> filePaths) {
        this.filePaths.addAll(filePaths);
    }

    public void assertNoneEndsWith(String path) {
        assertNoneEndsWith(Paths.get(path));
    }

    public void assertNoneEndsWith(Path path) {
        assertFalse(oneEndsWith(path), containsMessage(path + " is not contained in:"));
    }

    public void assertOneEndsWith(String path) {
        assertOneEndsWith(Paths.get(path));
    }

    public void assertOneEndsWith(Path path) {
        assertTrue(oneEndsWith(path), containsMessage(path + " is contained in:"));
    }

    private Supplier<String> containsMessage(String actualPathMessage) {
        return () -> {
            StringBuilder sb = new StringBuilder();

            sb.append(actualPathMessage);
            sb.append("\n");

            Iterator<File> iterator = filePaths.iterator();
            while (iterator.hasNext()) {
                File next = iterator.next();
                sb.append("\to ");
                sb.append(next);
                if (iterator.hasNext()) {
                    sb.append("\n");
                }
            }

            return sb.toString();
        };
    }

    private boolean oneEndsWith(Path path) {
        boolean contains = false;
        for (File file : filePaths) {
            contains = file.toPath().endsWith(path);
            if (contains) {
                break;
            }
        }
        return contains;
    }


}
