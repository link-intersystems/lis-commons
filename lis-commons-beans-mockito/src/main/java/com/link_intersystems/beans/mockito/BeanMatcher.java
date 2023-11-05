package com.link_intersystems.beans.mockito;

import com.link_intersystems.beans.BeansFactory;
import org.mockito.ArgumentMatcher;

import static java.util.Objects.requireNonNull;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class BeanMatcher {

    public static final BeanMatcher DEFAULT = new BeanMatcher();

    private BeansFactory beansFactory;

    public BeanMatcher() {
        this(BeansFactory.getDefault());
    }

    public BeanMatcher(BeansFactory beansFactory) {
        this.beansFactory = requireNonNull(beansFactory);
    }

    public <T> T propertiesEqual(Object aBean, String... excludeProperties) {
        reportMatcher(new PropertiesMatcher<>(aBean, beansFactory, excludeProperties));
        return null;
    }

    private void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
