package com.link_intersystems.lang;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class ClasspathTestUtil {

    public static URLClassLoader getCurrentClasspathClassLoader() {
        return getCurrentClasspathClassLoader(null);
    }

    public static URLClassLoader getCurrentClasspathClassLoader(ClassLoader parentClassLoader) {
        URL[] urls = getCurrentClasspathURLs();
        URLClassLoader otherURLClassLoader = new URLClassLoader(urls, parentClassLoader);
        return otherURLClassLoader;
    }

    public static URL[] getCurrentClasspathURLs() {
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        URL[] urls = Arrays.stream(classpathEntries).map(cpe -> {
            try {
                return new File(cpe).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(URL[]::new);
        return urls;
    }
}
