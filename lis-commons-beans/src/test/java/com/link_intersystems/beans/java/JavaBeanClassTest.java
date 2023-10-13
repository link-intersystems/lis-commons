package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanInstantiationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class JavaBeanClassTest {

    public static class UninstantiableBean {

        private UninstantiableBean() {
        }
    }

    @Test
    void newBeanInstance() {
        BeanClass<UninstantiableBean> javaBeanClass = new JavaBeanClass<>(UninstantiableBean.class);
        assertThrows(BeanInstantiationException.class, javaBeanClass::newBeanInstance);
    }

    @Test
    void getName() {
        BeanClass<SomeBean> javaBeanClass = new JavaBeanClass<>(SomeBean.class);
        assertEquals("SomeBean", javaBeanClass.getName());
    }
}