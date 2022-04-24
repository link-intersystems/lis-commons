package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(JavaBeansExtension.class)
class PropertyDescriptor2AccessorsTransformerTest {

    private SomeBeanFixture someBeanFixture;
    private PropertyDescriptor2AccessorsTransformer transformer;

    @BeforeEach
    void setUp(TestBeansFactory beansFactory) throws IntrospectionException {
        someBeanFixture = new SomeBeanFixture(beansFactory);
        transformer = new PropertyDescriptor2AccessorsTransformer();
    }

    @Test
    void applyOnlyWriteMethod()  {
        List<Method> methods = transformer.apply(someBeanFixture.writeOnlyPropertyDescriptor.getJavaPropertyDescriptor());

        assertEquals(1, methods.size());
    }

    @Test
    void applyOnlyReadMethod()  {
        List<Method> methods = transformer.apply(someBeanFixture.readOnlyPropertyDescriptor.getJavaPropertyDescriptor());

        assertEquals(1, methods.size());
    }
}