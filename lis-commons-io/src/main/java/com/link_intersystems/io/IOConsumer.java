package com.link_intersystems.io;

import java.io.IOException;

/**
 * An {@link java.util.function.Consumer} like interface for I/O related stuff.
 *
 * @param <T> an I/O type that might raise an {@link IOException}.
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@FunctionalInterface
public interface IOConsumer<T> {

    /**
     * An {@link IOConsumer} that does nothing (<b>no</b> <b>op</b>eration).
     *
     * @param <T>
     * @return
     */
    public static <T> IOConsumer<T> noop() {
        return t -> {
        };
    }

    public void accept(T t) throws IOException;
}
