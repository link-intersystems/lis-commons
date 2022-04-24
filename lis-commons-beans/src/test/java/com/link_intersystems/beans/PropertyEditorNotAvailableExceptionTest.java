package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class PropertyEditorNotAvailableExceptionTest {

    @Test
    void message() {
        PropertyEditorNotAvailableException exception = new PropertyEditorNotAvailableException("Message");

        assertEquals("Message", exception.getMessage());
    }

}