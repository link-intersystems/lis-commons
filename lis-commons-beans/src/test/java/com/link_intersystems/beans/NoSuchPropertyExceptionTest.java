package com.link_intersystems.beans;

import com.link_intersystems.beans.java.SomeBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class NoSuchPropertyExceptionTest {

    @Test
    void getLocalizedMessage() {
        assertNotNull(new NoSuchPropertyException(SomeBean.class, "stringProperty").getLocalizedMessage());
    }
}