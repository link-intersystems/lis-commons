package com.link_intersystems.beans;

import com.link_intersystems.beans.java.SomeBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class MessagesTest {

    @Test
    void get() {
        assertNotNull(Messages.get());
    }

    @Test
    void formatNoSuchProperty() {
        String formatted = Messages.formatNoSuchProperty(SomeBean.class, "stringProperty");
        assertEquals("Property com.link_intersystems.beans.java.SomeBean.stringProperty does not exist", formatted);
    }
}