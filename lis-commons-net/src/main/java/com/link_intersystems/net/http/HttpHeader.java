package com.link_intersystems.net.http;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpHeader {

    private final String name;
    private final List<String> values;

    public HttpHeader(String name, String... values) {
        this(name, Arrays.asList(values));
    }

    public HttpHeader(String name, List<String> values) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null.");
        }

        this.name = normalizedName(name);

        if (values == null) {
            throw new IllegalArgumentException("header values must not be null");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("header values must not be empty");
        }

        this.values = normalizeValues(values);
    }


    private List<String> normalizeValues(List<String> values) {
        return values.stream()
                .map(String::trim)
                .collect(toList());
    }

    static String normalizedName(String headerName) {
        return headerName == null ? null : headerName.trim();
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    public boolean equals(Object o) {
        return o instanceof HttpHeader && equals((HttpHeader) o);
    }

    private boolean equals(HttpHeader other) {
        return getName().equalsIgnoreCase(other.getName()) && Objects.equals(getValues(), other.getValues());
    }

    public int hashCode() {
        return getName().toLowerCase().hashCode() ^ getValues().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getName());
        sb.append(": ");
        sb.append(String.join(", ", getValues()));

        return sb.toString();
    }
}
