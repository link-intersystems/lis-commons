package com.link_intersystems.io.file;

import com.link_intersystems.io.IOConsumer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;

public class RegularFile extends AbstractFile {


    /**
     * Creates a {@link RegularFile} based on the given {@link File}.
     */
    public RegularFile(File file) {
        this(file.toPath());
    }

    /**
     * Creates a {@link RegularFile} based on the given filepath.
     *
     * @param filepath the path of this {@link RegularFile}.
     */
    public RegularFile(Path filepath) {
        super(filepath);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the file is created it will be empty.
     * <p>
     * You do not have to call this method before any
     * {@link #write(IOConsumer, Charset)} or {@link #append(IOConsumer, Charset)}
     * invocation, because these methods will also create this file if it does not exist.
     *
     * @throws IOException
     */
    @Override
    public void create() throws IOException {
        append(IOConsumer.noop());
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
     * Writes the content provided by the {@link Appendable} to this {@link RegularFile} using {@link java.nio.charset.StandardCharsets#UTF_8}.
     *
     * @see #write(IOConsumer, Charset)
     */
    public void write(IOConsumer<Appendable> contentWriter) throws IOException {
        write(contentWriter, UTF_8);
    }

    /**
     * Writes the content provided by the {@link Appendable} to this {@link RegularFile} using the specified {@link Charset}.
     *
     * <ul>
     *     <li>If parent directories do not exist, they will be created.</li>
     *     <li>If the file does not exist, it will be created.</li>
     *     <li>If the file already exists, it will be overwritten.</li>
     *     <li>If the file is an existent directory, an {@link IOException} is raised.</li>
     * </ul>
     * <p>
     *  This method can also be used to create an empty file
     *
     *  <pre>
     *      RegularFile regularFile = ...;
     *      regularFile.write({@link IOConsumer#noop()});
     *  </pre>
     *
     * @param contentWriter an {@link Appendable} {@link IOConsumer} used to write the content of this file.
     * @throws IOException if the file is an existent directory or if the content could not be written.
     */
    public void write(IOConsumer<Appendable> contentWriter, Charset charset) throws IOException {
        ensureParentDirs();

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(getPath(), CREATE, TRUNCATE_EXISTING), charset))) {
            contentWriter.accept(pw);
        }
    }

    private void ensureParentDirs() throws IOException {
        Path filePath = getPath();
        Path parentPath = filePath.getParent();
        if (parentPath != null && !Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
        }
    }

    public void append(IOConsumer<Appendable> contentWriter) throws IOException {
        append(contentWriter, UTF_8);
    }

    /**
     * Appends content provided by the {@link Appendable} to this {@link RegularFile} using the specified {@link Charset}.
     *
     * <ul>
     *     <li>If parent directories do not exist, they will be created.</li>
     *     <li>If the file does not exist, it will be created and the content will be appended.</li>
     *     <li>If the file already exists, the content will be appended.</li>
     *     <li>If the file is an existent directory an {@link IOException} is raised.</li>
     * </ul>
     *
     * @param contentWriter an {@link Appendable} {@link IOConsumer} used to append to the content of this file.
     * @throws IOException if the file is an existent directory or if the content could not be appended.
     */
    public void append(IOConsumer<Appendable> contentWriter, Charset charset) throws IOException {
        ensureParentDirs();

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(getPath(), CREATE, APPEND), charset))) {
            contentWriter.accept(pw);
        }
    }
}