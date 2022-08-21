package com.link_intersystems.jdbc.test.db.h2;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AnnotationH2ConfigPropertiesSource implements H2ConfigPropertiesSource {

    private Class<?> testClass;

    public AnnotationH2ConfigPropertiesSource(Class<?> testClass) {
        this.testClass = requireNonNull(testClass);
    }

    @Override
    public H2ConfigProperties getConfigProperties() {
        H2Config h2Config = findH2Config(testClass);

        if (h2Config == null) {
            return new DefaultH2ConfigProperties();
        }

        try {
            return new AnnotationH2ConfigProperties(h2Config);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get config properties", e);
        }
    }


    private H2Config findH2Config(Class<?> declaringClass) {
        if (declaringClass == null) {
            return null;
        }

        H2Config config = declaringClass.getAnnotation(H2Config.class);
        if (config != null) {
            return config;
        }

        Class<?> superclass = declaringClass.getSuperclass();

        return findH2Config(superclass);
    }
}
