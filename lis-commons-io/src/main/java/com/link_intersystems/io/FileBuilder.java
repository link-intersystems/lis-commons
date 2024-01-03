package com.link_intersystems.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * A helper factory for creating directory structures.
 */
public class FileBuilder {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private final Path dirpath;

    /**
     * @param dirpath the directory path that this {@link FileBuilder} operates on.
     */
    public FileBuilder(Path dirpath) {
        if (!Files.isDirectory(dirpath)) {
            throw new IllegalArgumentException(dirpath + " is not a directory.");
        }
        this.dirpath = dirpath;
    }

    public Path getDirpath() {
        return dirpath;
    }

    /**
     * Creates an empty file.
     *
     * @param name
     * @return the path of the written file.
     * @throws IOException if the file already exists.
     */
    public Path createFile(String name) throws IOException {

        return createFile(name, Channels.newChannel(new ByteArrayInputStream(EMPTY_BYTE_ARRAY)));
    }

    /**
     * Writes a file of the given name with the content provided by the {@link ReadableByteChannel}.
     * Use {@link Channels#newChannel(InputStream)} if you want to use an {@link InputStream} instead.
     *
     * @param name
     * @param content
     * @throws IOException if the file exists.
     */
    public Path createFile(String name, ReadableByteChannel content) throws IOException {
        return createFile(name, IOConsumers.readableChannelCopyConsumer(content));
    }

    /**
     * Writes a file of the given name,  the content is provided by the {@link IOConsumer<WritableByteChannel>} callback.
     * Use {@link IOConsumers#adaptOutputStream(IOConsumer)} if you want to use an {@link java.io.OutputStream} instead.
     *
     * @param name
     * @param writableChannelConsumer
     * @throws IOException if the file exists.
     */
    public Path createFile(String name, IOConsumer<WritableByteChannel> writableChannelConsumer) throws IOException {
        Path filepath = getDirpath().resolve(name);

        try (WritableByteChannel writableChannel = newByteChannel(filepath, CREATE_NEW, WRITE)) {
            writableChannelConsumer.accept(writableChannel);
        }

        return filepath;
    }

    public FileBuilder mkdir(String name) throws IOException {
        Path newDirpath = dirpath.resolve(name);
        Files.createDirectories(newDirpath);
        return new FileBuilder(newDirpath);
    }
}
