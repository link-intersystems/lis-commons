package com.link_intersystems.lang.reflect;

import com.link_intersystems.lang.ref.CopyOnAccessReference;

import java.io.Serializable;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Serialization {
    public static <T extends Serializable> T clone(T object) {
        return new CopyOnAccessReference<>(object).get();
    }
}
