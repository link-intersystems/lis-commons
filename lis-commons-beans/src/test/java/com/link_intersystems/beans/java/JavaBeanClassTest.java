package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanInstantiationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class JavaBeanClassTest {

    public class UninstantiableBean {

        private UninstantiableBean() {
        }
    }

    @Test
    void newBeanInstance() {
        JavaBeanClass<UninstantiableBean> javaBeanClass = new JavaBeanClass<>(UninstantiableBean.class);
        assertThrows(BeanInstantiationException.class, () -> javaBeanClass.newBeanInstance());
    }
}