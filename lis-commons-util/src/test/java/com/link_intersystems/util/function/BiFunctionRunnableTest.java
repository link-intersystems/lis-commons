package com.link_intersystems.util.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BiFunctionRunnableTest {

    @Test
    void run() {
        BiFunctionRunnable<String, String, String> runnable = new BiFunctionRunnable<>(this::concat, "Hello ", "World");
        runnable.run();

        assertEquals("Hello World", runnable.getResult());
    }

    private String concat(String s1, String s2) {
        return s1 + s2;
    }
}