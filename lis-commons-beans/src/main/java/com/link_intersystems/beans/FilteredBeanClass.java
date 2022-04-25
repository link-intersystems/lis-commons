package com.link_intersystems.beans;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FilteredBeanClass<T> extends BeanClass<T> {

    private BeanClass<T> beanClass;
    private PropertyDescFilter propertyFilter;

    FilteredBeanClass(BeanClass<T> beanClass, PropertyDescFilter propertyFilter) {
        this.beanClass = requireNonNull(beanClass);
        this.propertyFilter = requireNonNull(propertyFilter);
    }

    @Override
    public String getName() {
        return beanClass.getName();
    }

    @Override
    public Class<T> getType() {
        return beanClass.getType();
    }


    @Override
    public BeanInstanceFactory<T> getBeanInstanceFactory(ArgumentResolver argumentResolver) {
        BeanInstanceFactory<T> beanInstanceFactory = beanClass.getBeanInstanceFactory(argumentResolver);
        return new BeanInstanceFactory<T>() {
            @Override
            public Bean<T> newBeanInstance() {
                Bean<T> bean = beanInstanceFactory.newBeanInstance();
                return new FilteredBean<>(FilteredBeanClass.this, bean, propertyFilter);
            }

            @Override
            public Bean<T> fromExistingInstance(T beanObject) {
                Bean<T> bean = beanInstanceFactory.fromExistingInstance(beanObject);
                return new FilteredBean<>(FilteredBeanClass.this, bean, propertyFilter);
            }
        };
    }

    @Override
    public BeanEventTypeList getBeanEventTypes() {
        return beanClass.getBeanEventTypes();
    }

    @Override
    public PropertyDescList getProperties() {
        PropertyDescList properties = beanClass.getProperties();
        return getFilteredProperties(properties);
    }

    @Override
    public PropertyDescList getIndexedProperties() {
        PropertyDescList properties = beanClass.getIndexedProperties();
        return getFilteredProperties(properties);
    }

    @Override
    public PropertyDescList getAllProperties() {
        PropertyDescList properties = beanClass.getAllProperties();
        return getFilteredProperties(properties);
    }

    private PropertyDescList getFilteredProperties(PropertyDescList properties) {
        List<? extends PropertyDesc> filteredPropertyDescs = properties.stream()
                .filter(propertyFilter::accept)
                .collect(toList());
        return new PropertyDescList(filteredPropertyDescs);
    }

    @Override
    public String toString() {
        return "FilteredBeanClass{" +
                "beanClass=" + beanClass +
                ", propertyFilter=" + propertyFilter +
                '}';
    }

    private static class FilteredBean<T> extends Bean<T> {

        private final BeanClass<T> beanClass;
        private final Bean<T> bean;
        private final PropertyDescFilter propertyFilter;

        public FilteredBean(BeanClass<T> beanClass, Bean<T> bean, PropertyDescFilter propertyFilter) {
            super(beanClass, bean.getBeanObject());
            this.beanClass = beanClass;
            this.bean = bean;
            this.propertyFilter = propertyFilter;
        }

        @Override
        public PropertyList getAllProperties() {
            List<Property> properties = bean.getProperties().stream()
                    .filter(this::accept).collect(toList());
            return new PropertyList(properties);
        }

        private boolean accept(Property property) {
            return propertyFilter.accept(property.getPropertyDesc());
        }

        @Override
        public T getBeanObject() {
            return bean.getBeanObject();
        }

        @Override
        public BeanClass<T> getBeanClass() {
            return beanClass;
        }

        @Override
        public void removeListener(Object listener) {
            bean.removeListener(listener);
        }

        @Override
        public void addListener(Object listener) {
            bean.addListener(listener);
        }

        @Override
        public String toString() {
            return "FilteredBean{" +
                    "bean=" + bean +
                    ", propertyFilter=" + propertyFilter +
                    '}';
        }
    }
}
