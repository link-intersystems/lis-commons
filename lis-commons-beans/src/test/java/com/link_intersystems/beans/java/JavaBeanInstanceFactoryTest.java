package com.link_intersystems.beans.java;

import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeanInstantiationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class JavaBeanInstanceFactoryTest {

    @Test
    void newBeanInstance() {
        JavaBeanInstanceFactory<SomeBean> beanInstanceFactory = new JavaBeanInstanceFactory<>(new JavaBeanClass<>(SomeBean.class));

        Bean<SomeBean> someBeanBean = beanInstanceFactory.newBeanInstance();

        assertNotNull(someBeanBean);
        assertNotNull(someBeanBean.getBeanObject());
    }

    @Test
    void fromExistingBean() {
        JavaBeanInstanceFactory<SomeBean> beanInstanceFactory = new JavaBeanInstanceFactory<>(new JavaBeanClass<>(SomeBean.class));

        SomeBean bean = new SomeBean();
        Bean<SomeBean> someBeanBean = beanInstanceFactory.fromExistingInstance(bean);

        assertNotNull(someBeanBean);
        assertEquals(bean, someBeanBean.getBeanObject());
    }

    @Test
    void newBeanInstanceException() {
        class UninstantiableBean {

        }
        JavaBeanInstanceFactory<UninstantiableBean> beanInstanceFactory = new JavaBeanInstanceFactory<>(new JavaBeanClass<>(UninstantiableBean.class));

        assertThrows(BeanInstantiationException.class, beanInstanceFactory::newBeanInstance);


    }
}