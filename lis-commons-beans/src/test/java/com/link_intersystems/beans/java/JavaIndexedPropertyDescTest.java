package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(JavaBeansExtension.class)
public class JavaIndexedPropertyDescTest {

    private SomeBeanFixture someBeanFixture;

    @BeforeEach
    public void setup(TestBeansFactory beansFactory) throws IntrospectionException, BeanClassException {
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

    @Test
    void getTypeByIndexedWriteMethod() {
        assertEquals(String[].class, someBeanFixture.indexedPropertyWriteOnlyIndexOnlyAccessDescriptor.getType());
    }

    @Test
    void getTypeByIndexedReadMethod() {
        assertEquals(String[].class, someBeanFixture.indexedPropertyReadOnlyIndexOnlyAccessDescriptor.getType());
    }
}
