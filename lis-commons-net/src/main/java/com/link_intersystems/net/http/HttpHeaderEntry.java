package com.link_intersystems.net.http;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpHeaderEntry extends AbstractMap.SimpleEntry<String, List<String>> {


    public HttpHeaderEntry(String name, List<String> values) {
        super(name, values);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry))
            return false;
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        Object otherKey = e.getKey();

        if ((otherKey instanceof String)) {
            String otherKeyString = (String) otherKey;
            return eq(getKey(), otherKeyString) && Objects.equals(getValue(), e.getValue());
        }

        return false;
    }


    public int hashCode() {
        return (getKey() == null ? 0 : getKey().toLowerCase().hashCode()) ^
                (getValue() == null ? 0 : getValue().hashCode());
    }

    private boolean eq(String o1, String o2) {
        return o2.equalsIgnoreCase(o1);
    }
}
