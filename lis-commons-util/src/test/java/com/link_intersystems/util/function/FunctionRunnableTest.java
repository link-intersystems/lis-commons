package com.link_intersystems.util.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FunctionRunnableTest {

    @Test
    void run() {
        FunctionRunnable<String, String> runnable = new FunctionRunnable<>(this::greeting, "World");

        runnable.run();

        assertEquals("Hello World", runnable.getResult());
    }

    private String greeting(String who) {
        return "Hello " + who;
    }
}