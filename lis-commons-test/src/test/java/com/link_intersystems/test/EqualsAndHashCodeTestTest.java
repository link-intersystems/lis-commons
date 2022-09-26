package com.link_intersystems.test;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class EqualsAndHashCodeTestTest extends EqualsAndHashCodeTest {
    @Override
    protected Object createInstance() throws Exception {
        return new String("A");
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new String("B");
    }
}
