package com.link_intersystems.io;

import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PathMatch {

    private final Path basepath;
    private final Path path;

    PathMatch(Path basepath, Path path) {
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
}
