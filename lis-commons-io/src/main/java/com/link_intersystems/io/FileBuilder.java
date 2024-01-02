package com.link_intersystems.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

public class FileBuilder {

    private static final int EOF = -1;
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private int writeBufferSize = 8192;

    public static interface ContentWriter<T> {

        public void write(T writer) throws IOException;
    }

    private final Path path;

    public FileBuilder(Path path) {
        this.path = Objects.requireNonNull(path);
    }

    public Path getPath() {
        return path;
    }

    public Path writeFile(String name) throws IOException {

        return writeFileStream(name, new ByteArrayInputStream(EMPTY_BYTE_ARRAY));
    }

    public Path writeFile(String name, String content) throws IOException {

        return writeFile(name, content, UTF_8);
    }

    public Path writeFile(String name, String content, Charset charset) throws IOException {

        return writeFileStream(name, new ByteArrayInputStream(content.getBytes(charset)));
    }

    public Path writeFileStream(String name, InputStream content) throws IOException {
        try(ReadableByteChannel readableByteChannel = Channels.newChannel(content)){
            return writeFile(name, readableByteChannel);
        }
    }

    public Path writeFile(String name, ReadableByteChannel content) throws IOException {
        return writeFile(name, contentWriter -> {
            ByteBuffer byteBuffer = createByteBuffer(writeBufferSize);
            while (content.read(byteBuffer) != EOF) {
                byteBuffer.flip();
                contentWriter.write(byteBuffer);
                byteBuffer.flip();
            }
        });
    }

    public Path writeFileStream(String name, ContentWriter<OutputStream> contentWriter) throws IOException {
        return writeFile(name, writer -> {
            try (OutputStream outputStream = Channels.newOutputStream(writer)) {
                contentWriter.write(outputStream);
            }
        });
    }

    public Path writeFile(String name, ContentWriter<WritableByteChannel> contentWriter) throws IOException {
        Path filepath = path.resolve(name);

        try (WritableByteChannel writableChannel = newByteChannel(filepath, CREATE_NEW, WRITE)) {
            contentWriter.write(writableChannel);
        }

        return filepath;
    }

    private ByteBuffer createByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size);
    }

}
