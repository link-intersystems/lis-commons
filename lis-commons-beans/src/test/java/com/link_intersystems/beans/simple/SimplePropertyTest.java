package com.link_intersystems.beans.simple;

import com.link_intersystems.beans.PropertyReadException;
import com.link_intersystems.beans.PropertyWriteException;
import com.link_intersystems.beans.java.JavaBeanClass;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
class SimplePropertyTest {

    private TestBean testBean;
    private SimpleProperty modifiableProperty;
    private SimpleProperty readOnlyProperty;
    private SimpleProperty writeOnlyProperty;

    @BeforeEach
    void setUp() throws IntrospectionException {
        SimpleBeanClass javaBeanClass = new SimpleBeanClass<>(JavaBeanClass.get(TestBean.class));
        PropertyList properties = javaBeanClass.getProperties();
        modifiableProperty = properties.getByName("modifiableProperty");
        readOnlyProperty = properties.getByName("readOnlyProperty");
        writeOnlyProperty = properties.getByName("writeOnlyProperty");
        testBean = new TestBean();
    }


    @Test
    void getName() {
        assertEquals("modifiableProperty", modifiableProperty.getName());
        assertEquals("readOnlyProperty", readOnlyProperty.getName());
    }

    @Test
    void getProperty() throws PropertyReadException {
        testBean.readOnlyProperty = "test";
        assertEquals("test", readOnlyProperty.get(testBean));

        testBean.modifiableProperty = "test";
        assertEquals("test", modifiableProperty.get(testBean));
    }

    @Test
    void setProperty() throws PropertyWriteException {
        modifiableProperty.set(testBean, "test");
        assertEquals("test", testBean.modifiableProperty);
    }

    @Test
    void setReadonlyProperty() {
        assertThrows(PropertyWriteException.class, () -> readOnlyProperty.set(testBean, "test"));
    }

    @Test
    void getWriteonlyProperty() {
        assertThrows(PropertyReadException.class, () -> writeOnlyProperty.get(testBean));
    }

    @Test
    void getterException() {

        assertThrows(PropertyReadException.class, () -> readOnlyProperty.get(new Object()));
    }

    @Test
    void setterException() {

        assertThrows(PropertyWriteException.class, () -> writeOnlyProperty.set(testBean, new Object()));
    }
}