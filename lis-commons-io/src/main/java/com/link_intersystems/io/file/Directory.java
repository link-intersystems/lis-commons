package com.link_intersystems.io.file;

import com.link_intersystems.io.IOConsumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Directory extends AbstractFile {

    /**
     * Creates a {@link Directory} based on the given {@link File}.
     */
    public Directory(File dir) {
        this(dir.toPath());
    }

    /**
     * Creates a {@link Directory} based on the given dirpath.
     *
     * @param dirpath the path of this {@link Directory}.
     */
    public Directory(Path dirpath) {
        super(dirpath);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If this directory does not exist, all parent directories that also do not exist
     * will be created as well.
     *
     * @throws IOException
     */
    @Override
    public void create() throws IOException {
        Files.createDirectories(getPath());
    }

    @Override
    public Directory getParent() {
        Path parent = getPath().getParent();

        if (parent != null) {
            return new Directory(parent);
        }

        return null;
    }

    /**
     * @param directoryRelativePath
     * @return the {@link RegularFile} of the given path relative to this directory.
     * @throws IOException
     */
    public RegularFile file(String directoryRelativePath) throws IOException {
        return file(Paths.get(directoryRelativePath));
    }

    /**
     * @param directoryRelativePath
     * @return the {@link RegularFile} of the given path relative to this directory.
     * @throws IOException
     */
    public RegularFile file(Path directoryRelativePath) throws IOException {
        return new RegularFile(getPath().resolve(directoryRelativePath));
    }

    /**
     * @return all regular files in this directory.
     * @throws IOException
     */
    public List<RegularFile> listFiles() throws IOException {
        return listFiles(path -> true);
    }

    /**
     * @return all regular files in this directory that match the given file filter.
     * @throws IOException
     */
    public List<RegularFile> listFiles(DirectoryStream.Filter<Path> fileFilter) throws IOException {
        List<RegularFile> files = new ArrayList<>();
        forEachFiles(files::add, fileFilter);
        return files;
    }

    /**
     * Invokes the given {@link IOConsumer} for each {@link RegularFile} in this directory.
     *
     * @throws IOException
     */
    public void forEachFiles(IOConsumer<RegularFile> fileConsumer) throws IOException {
        forEachFiles(fileConsumer, path -> true);
    }

    /**
     * Invokes the given {@link IOConsumer} for each {@link RegularFile} in this directory
     * that match the given file filter.
     *
     * @throws IOException
     */
    public void forEachFiles(IOConsumer<RegularFile> fileConsumer, DirectoryStream.Filter<Path> fileFilter) throws IOException {
        DirectoryStream.Filter<Path> filter = path -> Files.isRegularFile(path) && fileFilter.accept(path);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(getPath(), filter)) {
            for (Path path : directoryStream) {
                RegularFile regularFile = new RegularFile(path);
                fileConsumer.accept(regularFile);
            }
        }
    }
}
