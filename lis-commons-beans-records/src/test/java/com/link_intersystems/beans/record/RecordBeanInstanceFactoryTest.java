package com.link_intersystems.beans.record;

import com.link_intersystems.beans.ArgumentResolver;
import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeanInstanceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecordBeanInstanceFactoryTest {

    private ArgumentResolver argumentResolver;

    public static record RecordWithoutDefaultConstructor(String value) {
    }

    private BeanInstanceFactory<PersonRecord> beanInstanceFactory;

    @BeforeEach
    void setUp() throws IntrospectionException {
        argumentResolver = spy(ArgumentResolver.class);
        RecordBeanClass<PersonRecord> personRecordBeanClass = new RecordBeanClass<>(PersonRecord.class);
        beanInstanceFactory = new RecordBeanInstanceFactory<>(personRecordBeanClass, argumentResolver);
    }

    @Test
    void newBeanInstance() {
        Bean<PersonRecord> personBean = beanInstanceFactory.newBeanInstance();
        assertInstanceOf(RecordBean.class, personBean);
    }

    @Test
    void newBeanInstanceWithArgumentResolver() throws IntrospectionException {
        RecordBeanClass<RecordWithoutDefaultConstructor> beanClass = new RecordBeanClass<>(RecordWithoutDefaultConstructor.class);
        ArgumentResolver argumentResolver = parameter -> {
            try {

                if (parameter.equals(RecordWithoutDefaultConstructor.class.getDeclaredConstructor(String.class).getParameters()[0])) {
                    return "someValue";
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        };
        RecordBeanInstanceFactory<RecordWithoutDefaultConstructor> instanceFactory = new RecordBeanInstanceFactory<>(beanClass, argumentResolver);


        Bean<RecordWithoutDefaultConstructor> bean = instanceFactory.newBeanInstance();
        assertInstanceOf(RecordBean.class, bean);

        RecordWithoutDefaultConstructor beanObject = bean.getBeanObject();
        assertEquals("someValue", beanObject.value());
    }

    @Test
    void fromExistingInstance() {
        PersonRecord personRecord = new PersonRecord("René", "Link");
        Bean<PersonRecord> personBean = beanInstanceFactory.fromExistingInstance(personRecord);
        assertInstanceOf(RecordBean.class, personBean);
    }
}