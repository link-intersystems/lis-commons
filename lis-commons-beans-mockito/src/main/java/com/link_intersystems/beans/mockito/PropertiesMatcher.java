package com.link_intersystems.beans.mockito;

import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeansFactory;
import com.link_intersystems.beans.Property;
import com.link_intersystems.beans.PropertyList;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class PropertiesMatcher<T> implements ArgumentMatcher<T> {

    private final Bean<T> expectedBean;
    private final List<String> excludeProperties;
    private final BeansFactory beansFactory;

    public PropertiesMatcher(T expectedBean, String... excludeProperties) {
        this(expectedBean, BeansFactory.getDefault(), excludeProperties);
    }

    public PropertiesMatcher(T expectedBean, BeansFactory beansFactory, String... excludeProperties) {
        this.beansFactory = requireNonNull(beansFactory);
        this.expectedBean = beansFactory.createBean(expectedBean, Object.class);
        this.excludeProperties = Arrays.asList(excludeProperties);
    }

    @Override
    public boolean matches(T argument) {
        Bean<T> actualBean = beansFactory.createBean(argument, Object.class);

        Predicate<Property> exclucedPropertiesPredicate = pd -> !excludeProperties.contains(pd.getPropertyDesc().getName());
        PropertyList expectedProperties = expectedBean.getProperties();
        PropertyList expectedFilteredProperties = expectedProperties.filter(exclucedPropertiesPredicate);

        PropertyList actualProperties = actualBean.getProperties();
        PropertyList actualFilteredProperties = actualProperties.filter(exclucedPropertiesPredicate);

        return expectedFilteredProperties.equals(actualFilteredProperties);
    }
}
