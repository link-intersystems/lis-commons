package com.link_intersystems.io;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Factory for creating {@link IOConsumer} adapters.
 */
public class IOConsumers {

    /**
     * Creates an {@link IOConsumer<WritableByteChannel>} adapter for an {@link IOConsumer<OutputStream>}.
     *
     * @param outputStreamIOConsumer
     * @return
     */
    public static IOConsumer<WritableByteChannel> adaptOutputStream(IOConsumer<OutputStream> outputStreamIOConsumer) {
        return writer -> {
            try (OutputStream outputStream = Channels.newOutputStream(writer)) {
                outputStreamIOConsumer.accept(outputStream);
            }
        };
    }

    /**
     * Creates an {@link IOConsumer<WritableByteChannel>} that will copy the given {@link ReadableByteChannel} to the {@link WritableByteChannel} when
     * {@link IOConsumer#accept(Object)} is invoked. A direct {@link ByteBuffer} of size 8192 is used.
     *
     * @param readableByteChannel
     */
    public static IOConsumer<WritableByteChannel> readableChannelCopyConsumer(ReadableByteChannel readableByteChannel) {
        return readableChannelCopyConsumer(readableByteChannel, ByteBuffer.allocateDirect(8192));
    }

    /**
     * Creates an {@link IOConsumer<WritableByteChannel>} that will copy the given {@link ReadableByteChannel} to the {@link WritableByteChannel} when
     * {@link IOConsumer#accept(Object)} is invoked. The given {@link ByteBuffer} will be used for the copy process.
     *
     * @param readableByteChannel
     * @param byteBuffer          the {@link ByteBuffer} to use for the copy process.
     */
    public static IOConsumer<WritableByteChannel> readableChannelCopyConsumer(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer) {
        return contentWriter -> {
            while (readableByteChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                contentWriter.write(byteBuffer);
                byteBuffer.flip();
            }
        };
    }

}
