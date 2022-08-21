package com.link_intersystems.jdbc.test.db.h2;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface H2DatabaseStore {

    public void put(H2Database h2Database);

    public H2Database get();

    public void remove();
}
