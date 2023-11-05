package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.Parameter;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class RecordBeanInstanceFactoryTest {

    private ArgumentResolver argumentResolver;
    private RecordBeanClass<PersonRecord> personRecordBeanClass;

    public static record RecordWithoutDefaultConstructor(String value) {
    }

    private BeanInstanceFactory<PersonRecord> beanInstanceFactory;

    @BeforeEach
    void setUp() throws IntrospectionException {
        argumentResolver = spy(ArgumentResolver.class);
        personRecordBeanClass = new RecordBeanClass<>(PersonRecord.class);
        beanInstanceFactory = new RecordBeanInstanceFactory<>(personRecordBeanClass);
    }

    @Test
    void newBeanInstance() {
        Bean<PersonRecord> personBean = beanInstanceFactory.newBeanInstance();
        assertInstanceOf(RecordBean.class, personBean);
    }

    @Test
    void newBeanInstanceWithArgumentResolver() throws IntrospectionException {
        RecordBeanClass<RecordWithoutDefaultConstructor> beanClass = new RecordBeanClass<>(RecordWithoutDefaultConstructor.class);
        ArgumentResolver argumentResolver = new ArgumentResolver() {
            @Override
            public boolean canResolveArgument(Parameter parameter) {
                return true;
            }

            @Override
            public Object resolveArgument(Parameter parameter) throws ArgumentResolveException {
                try {

                    if (parameter.equals(RecordWithoutDefaultConstructor.class.getDeclaredConstructor(String.class).getParameters()[0])) {
                        return "someValue";
                    }
                    return null;
                } catch (Exception e) {
                    throw new ArgumentResolveException(e);
                }
            }
        };

        RecordBeanInstanceFactory<RecordWithoutDefaultConstructor> instanceFactory = new RecordBeanInstanceFactory<>(beanClass);


        Bean<RecordWithoutDefaultConstructor> bean = instanceFactory.newBeanInstance(argumentResolver);
        assertInstanceOf(RecordBean.class, bean);

        RecordWithoutDefaultConstructor beanObject = bean.getBeanObject();
        assertEquals("someValue", beanObject.value());
    }

    @Test
    void unresolveableConstructor() throws IntrospectionException {
        RecordBeanClass<RecordWithoutDefaultConstructor> beanClass = new RecordBeanClass<>(RecordWithoutDefaultConstructor.class);
        ArgumentResolver argumentResolver = new ArgumentResolver() {
            @Override
            public boolean canResolveArgument(Parameter parameter) {
                return false;
            }

            @Override
            public Object resolveArgument(Parameter parameter) {
                return null;
            }
        };
        RecordBeanInstanceFactory<RecordWithoutDefaultConstructor> instanceFactory = new RecordBeanInstanceFactory<>(beanClass);


        assertThrows(BeanInstantiationException.class, () -> instanceFactory.newBeanInstance(argumentResolver));
    }

    @Test
    void argumentResolverException() throws IntrospectionException {
        RecordBeanClass<RecordWithoutDefaultConstructor> beanClass = new RecordBeanClass<>(RecordWithoutDefaultConstructor.class);
        ArgumentResolver argumentResolver = new ArgumentResolver() {
            @Override
            public boolean canResolveArgument(Parameter parameter) {
                return true;
            }

            @Override
            public Object resolveArgument(Parameter parameter) throws ArgumentResolveException {
                throw new ArgumentResolveException("");
            }
        };

        RecordBeanInstanceFactory<RecordWithoutDefaultConstructor> instanceFactory = new RecordBeanInstanceFactory<>(beanClass);


        assertThrows(BeanInstantiationException.class, () -> instanceFactory.newBeanInstance(argumentResolver));
    }


    @Test
    void constructorThrowsException() {
        Exception exception = new InstantiationException();
        beanInstanceFactory = new RecordBeanInstanceFactory<>(personRecordBeanClass) {
            @Override
            protected Callable<PersonRecord> resolveNewInstanceCallable(ArgumentResolver argumentResolver) {
                return () -> {
                    throw exception;
                };
            }
        };


        BeanInstantiationException beanInstantiationException = assertThrows(BeanInstantiationException.class, () -> beanInstanceFactory.newBeanInstance(argumentResolver));
        assertEquals(exception, beanInstantiationException.getCause());
    }

    @Test
    void fromExistingInstance() {
        PersonRecord personRecord = new PersonRecord("René", "Link");
        Bean<PersonRecord> personBean = beanInstanceFactory.fromExistingInstance(personRecord);
        assertInstanceOf(RecordBean.class, personBean);
    }
}