package com.link_intersystems.mockito.beans;

import org.mockito.ArgumentMatcher;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.*;

public class BeanMatchers {
    public static <T> T propertiesEqual(Object aBean) {
        reportMatcher(new PropertiesMatcher<>(aBean));
        return null;
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
