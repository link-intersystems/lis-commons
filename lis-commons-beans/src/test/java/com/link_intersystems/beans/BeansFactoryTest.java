package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BeansFactoryTest {

    @Test
    void getDefault() {
        BeansFactory beansFactory = BeansFactory.getDefault();
        assertNotNull(beansFactory);
    }

    @Test
    void getInstance() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {


            URLClassLoader urlClassLoader = new URLClassLoader(new URL[0]) {

                @Override
                public Enumeration<URL> getResources(String name) throws IOException {
                    Enumeration<URL> resources = BeansFactoryTest.class.getClassLoader().getResources(name);
                    URL ambiguousBeansFactory = BeansFactoryTest.class.getResource("ambiguousBeansFactory");
                    return new Enumeration<URL>() {

                        private boolean next = true;

                        @Override
                        public boolean hasMoreElements() {
                            return next || resources.hasMoreElements();
                        }

                        @Override
                        public URL nextElement() {
                            if (next) {

                                next = false;
                                return ambiguousBeansFactory;
                            } else {
                                return resources.nextElement();
                            }
                        }
                    };
                }
            };

            Thread.currentThread().setContextClassLoader(urlClassLoader);

            assertThrows(IllegalStateException.class, () -> BeansFactory.getInstance("java"));

        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

}