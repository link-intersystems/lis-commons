package com.link_intersystems.net.http;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpHeaders extends AbstractMap<String, List<String>> {

    private LinkedHashMap<String, List<String>> headers = new LinkedHashMap<>();

    public HttpHeaders() {
    }

    public HttpHeaders(Map<String, List<String>> headerFields) {
        this.putAll(headerFields);
    }

    public void put(String name, String value) {
        List<String> values = parseValue(value);
        putInternal(name, values);
    }

    private List<String> putInternal(String headerName, List<String> values) {
        String effectiveHeaderName = findEqualKey(this, headerName, effetiveKey(headerName));
        List<String> effectiveValues = effectiveValues(values);

        List<String> exsistingHeader = headers.put(effectiveHeaderName, effectiveValues);

        if (exsistingHeader != null) {
            exsistingHeader.addAll(effectiveValues);
            headers.put(effectiveHeaderName, exsistingHeader);
        }

        return exsistingHeader;
    }

    private List<String> parseValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("header value must not be null");
        }

        String[] valueParts = value.split("\\s*,\\s*");
        return stream(valueParts).map(String::trim).collect(toList());
    }

    private List<String> effectiveValues(List<String> values) {
        if (values == null) {
            throw new IllegalArgumentException("header values must not be null");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("header values must not be empty");
        }

        return values.stream()
                .map(String::trim)
                .collect(toList());
    }

    @Override
    public List<String> put(String headerName, List<String> values) {
        return putInternal(headerName, values);
    }

    private String effetiveKey(String headerName) {
        if (headerName == null) {
            throw new IllegalArgumentException("header name must not be null.");
        }

        return headerName.trim();
    }


    @Override
    public List<String> get(Object key) {
        Iterator<Entry<String, List<String>>> i = entrySet().iterator();
        if (key instanceof String) {
            String header = effetiveKey((String) key);
            while (i.hasNext()) {
                Entry<String, List<String>> e = i.next();

                if (header.equalsIgnoreCase(e.getKey())) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        Set<Entry<String, List<String>>> entries = headers.entrySet();

        return new AbstractSet<Entry<String, List<String>>>() {
            @Override
            public Iterator<Entry<String, List<String>>> iterator() {
                Iterator<Entry<String, List<String>>> iterator = entries.iterator();

                return new Iterator<Entry<String, List<String>>>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Entry<String, List<String>> next() {
                        Entry<String, List<String>> entry = iterator.next();
                        String key = entry.getKey();
                        List<String> value = entry.getValue();

                        return new HttpHeaderEntry(key, value);
                    }


                };
            }

            @Override
            public int size() {
                return entries.size();
            }
        };
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map))
            return false;
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size())
            return false;

        return contentEquals(m);
    }

    private boolean contentEquals(Map<?, ?> m) {
        for (Entry<String, List<String>> e : entrySet()) {
            String key = e.getKey();
            String otherKey = findEqualKey(m, key);
            List<String> values = e.getValue();

            if (!values.equals(m.get(otherKey)))
                return false;
        }
        return true;
    }

    private String findEqualKey(Map<?, ?> m, String key) {
        return findEqualKey(m, key, null);
    }

    private String findEqualKey(Map<?, ?> m, String key, String defaultValue) {
        return m.keySet().stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .filter(key::equalsIgnoreCase)
                .findFirst()
                .orElse(defaultValue);
    }


    public int hashCode() {
        int h = 0;
        for (Entry<String, List<String>> stringListEntry : entrySet()) {
            h += stringListEntry.getKey().toLowerCase().hashCode();
            h += stringListEntry.getValue().hashCode();
        }
        return h;
    }
}

