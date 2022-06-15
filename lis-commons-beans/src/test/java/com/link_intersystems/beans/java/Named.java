package com.link_intersystems.beans.java;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
interface Named {

    default public String trim(String name) {
        return name.trim();
    }

    public String getName();

}
