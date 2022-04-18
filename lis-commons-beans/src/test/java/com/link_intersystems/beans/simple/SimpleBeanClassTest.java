package com.link_intersystems.beans.simple;

import com.link_intersystems.beans.BeanInstantiationException;
import com.link_intersystems.beans.java.JavaBeanClass;
import com.link_intersystems.test.ComponentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
@ComponentTest
class SimpleBeanClassTest {

    private SimpleBeanClass javaBeanClass;

    @BeforeEach
    void setUp() {
        javaBeanClass = new SimpleBeanClass<>(JavaBeanClass.get(TestBean.class));
    }

    @Test
    void getProperties() {

        List<SimpleProperty> properties = javaBeanClass.getProperties();
        assertEquals(4, properties.size());
    }

    @Test
    void getSimpleName() {
        assertEquals("TestBean", javaBeanClass.getName());
    }

    @Test
    void newInstance() throws BeanInstantiationException {
        assertNotNull(javaBeanClass.newInstance());
    }

    @Test
    void newInstanceThrowsException() throws IntrospectionException {
        class NoBeanClass {
            private NoBeanClass() {
            }
        }
        javaBeanClass = new SimpleBeanClass(JavaBeanClass.get(NoBeanClass.class));

        assertThrows(BeanInstantiationException.class, () -> javaBeanClass.newInstance());
    }
}