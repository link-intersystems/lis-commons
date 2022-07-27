package com.link_intersystems.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FilePath {

    private final Path basepath;
    private final Path path;

    public FilePath(Path basepath, Path path) {
        this.basepath = requireNonNull(basepath);
        this.path = requireNonNull(path);
    }

    public Path getPath() {
        return path;
    }

    public Path getBasepath() {
        return basepath;
    }

    public Path getAbsolutePath() {
        return basepath.resolve(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePath filePath = (FilePath) o;
        return Objects.equals(basepath, filePath.basepath) && Objects.equals(path, filePath.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basepath, path);
    }

    public FilePath rebase(String newBasepath){
        return rebase(Paths.get(newBasepath));
    }

    public FilePath rebase(Path newBasepath) {
        return new FilePath(newBasepath, path);
    }
}
