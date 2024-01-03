package com.link_intersystems.io;

import java.io.IOException;

/**
 * An io-related {@link java.util.function.Consumer} api that supports {@link IOException}s.
 *
 * @param <T>
 */
public interface IOConsumer<T> {

    public void accept(T io) throws IOException;
}
