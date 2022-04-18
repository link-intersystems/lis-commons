package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.IntrospectionException;

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
        assertTrue(someBeanFixture.stringArrayPropertyDescriptor.isIndexedReadable());
        assertTrue(someBeanFixture.readOnlyIndexedPropertyDescriptor.isIndexedReadable());
        assertFalse(someBeanFixture.writeOnlyIndexedPropertyDescriptor.isIndexedReadable());

    }

    @Test
    void isIndexedWriteable() {
        assertTrue(someBeanFixture.stringArrayPropertyDescriptor.isIndexedWritable());
        assertTrue(someBeanFixture.writeOnlyIndexedPropertyDescriptor.isIndexedWritable());
        assertFalse(someBeanFixture.readOnlyIndexedPropertyDescriptor.isIndexedWritable());
    }
}
