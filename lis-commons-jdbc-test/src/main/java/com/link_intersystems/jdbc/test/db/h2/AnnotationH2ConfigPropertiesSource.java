package com.link_intersystems.jdbc.test.db.h2;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AnnotationH2ConfigPropertiesSource implements H2ConfigPropertiesSource {

    private Class<?> testClass;

    private Set<Class<?>> visitedClasses = new HashSet<>();
    private H2ConfigProperties configProperties;

    public AnnotationH2ConfigPropertiesSource(Class<?> testClass) {
        this.testClass = requireNonNull(testClass);
    }

    @Override
    public H2ConfigProperties getConfigProperties() {
        if (configProperties == null) {
            configProperties = resolveConfigProperties();
        }
        return configProperties;
    }

    private H2ConfigProperties resolveConfigProperties() {
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


    protected H2Config findH2Config(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        if (!visitedClasses.add(clazz)) {
            return null;
        }

        H2Config config = clazz.getAnnotation(H2Config.class);
        if (config != null) {
            return config;
        }

        H2Config h2Config = findOnAnnotations(clazz);
        if (h2Config != null) {
            return h2Config;
        }

        Class<?> superclass = clazz.getSuperclass();
        return findH2Config(superclass);
    }

    private H2Config findOnAnnotations(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();

            H2Config h2Config = findH2Config(annotationClass);
            if (h2Config != null) {
                return h2Config;
            }
        }
        return null;
    }


}
