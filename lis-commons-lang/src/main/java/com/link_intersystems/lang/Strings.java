package com.link_intersystems.lang;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Strings {
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
