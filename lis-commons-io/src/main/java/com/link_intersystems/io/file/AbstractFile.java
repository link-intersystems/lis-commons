package com.link_intersystems.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class AbstractFile {

    private Path path;

    protected AbstractFile(Path path) {
        this.path = requireNonNull(path);
    }

    /**
     * @return the {@link Path} of this {@link AbstractFile}.
     */
    public Path getPath() {
        return path;
    }

    /**
     * Creates this file. This method does nothing, if the file already exists.
     *
     * @throws IOException
     */
    public abstract void create() throws IOException;

    /**
     * @return the parent file of this file if any or <code>null</code>.
     * @throws IOException
     */
    public abstract AbstractFile getParent();

    public boolean exists() {
        return Files.exists(getPath());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractFile that = (AbstractFile) o;
        return Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }
}
