package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(JavaBeansExtension.class)
public class JavaIndexedPropertyDescTest {

    private SomeBeanFixture someBeanFixture;

    @BeforeEach
    public void setup(TestBeansFactory beansFactory) throws IntrospectionException {
        someBeanFixture = new SomeBeanFixture(beansFactory);
    }

    @Test
    void isIndexedReadable() {
        assertTrue(someBeanFixture.stringPropertyDescriptor.isIndexedReadable());
        assertTrue(someBeanFixture.readOnlyPropertyDescriptor.isIndexedReadable());
        assertFalse(someBeanFixture.writeOnlyPropertyDescriptor.isIndexedReadable());

    }

    @Test
    void isIndexedWriteable() {
        assertTrue(someBeanFixture.stringPropertyDescriptor.isIndexedWritable());
        assertTrue(someBeanFixture.writeOnlyPropertyDescriptor.isIndexedWritable());
        assertFalse(someBeanFixture.readOnlyPropertyDescriptor.isIndexedWritable());
    }
}
