package com.link_intersystems.util.config.properties;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TestProperties extends Countable {

    public static final ConfigProperty<String> FIRST_NAME = ConfigProperty.named("firstName").typed(String.class);
    public static final ConfigProperty<String> NAME = ConfigProperty.named("name").typed(String.class);

    public static final ConfigProperty<Boolean> REGISTERED = ConfigProperty.named("registered").withDefaultValue(true);

    public String getFirstName();

    public String getName();

    public void setName(String name);

    public boolean isRegistered();

    public String getNoProperty();

    public String otherMethod();

    public Integer getCount();
}
