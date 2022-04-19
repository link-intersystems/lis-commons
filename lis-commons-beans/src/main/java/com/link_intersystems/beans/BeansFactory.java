package com.link_intersystems.beans;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static com.link_intersystems.util.Iterators.toStream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class BeansFactory {

    private static BeansFactory DEFAULT_BEANS_FACTORY;

    public static BeansFactory getDefault() {
        if (DEFAULT_BEANS_FACTORY == null) {
            DEFAULT_BEANS_FACTORY = getInstance("java");
        }
        return DEFAULT_BEANS_FACTORY;
    }

    public static BeansFactory getInstance(String type) {
        ServiceLoader<BeansFactory> loader = ServiceLoader.load(BeansFactory.class);
        List<BeansFactory> beansFactories = toStream(loader)
                .filter(bf -> bf.getTypeName().equals(type))
                .collect(Collectors.toList());

        if (beansFactories.isEmpty()) {
            throw new IllegalArgumentException("No BeansFactory of type " + type + " found.");
        }

        if (beansFactories.size() > 1) {
            StringBuilder msg = new StringBuilder();

            msg.append("BeansFactory type ''");
            msg.append(type);
            msg.append("'' is ambiguous. ");
            msg.append(beansFactories.size());
            msg.append(" BeansFactories found:\n");

            for (BeansFactory beansFactory : beansFactories) {
                msg.append("\to ");
                msg.append(beansFactory.getClass().getName());
            }

            throw new IllegalStateException(msg.toString());
        }

        return beansFactories.get(0);
    }

    public abstract String getTypeName();

    public <T> BeanClass<T> createBeanClass(Class<T> beanClass) throws BeanClassException {
        return createBeanClass(beanClass, null);
    }

    public abstract <T> BeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException;

    public <T> Bean<T> createBean(T bean) throws BeanClassException {
        return createBean(bean, null);
    }

    @SuppressWarnings("unchecked")
    public <T> Bean<T> createBean(T bean, Class<?> stopClass) throws BeanClassException {
        BeanClass<T> beanClass = (BeanClass<T>) createBeanClass(bean.getClass(), stopClass);
        return beanClass.getBeanFromInstance(bean);
    }
}
