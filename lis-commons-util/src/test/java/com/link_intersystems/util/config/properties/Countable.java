package com.link_intersystems.util.config.properties;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface Countable {

    public static final ConfigProperty<Integer> COUNT = ConfigProperty.named("count").withDefaultValue(1);

}
