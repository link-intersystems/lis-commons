package com.link_intersystems.net.http;

import java.text.MessageFormat;
import java.util.*;

import static java.util.Collections.singletonList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpHeaders extends AbstractList<HttpHeader> {

    private List<HttpHeader> headers = new ArrayList<>();

    public HttpHeaders() {
    }

    public HttpHeaders(Collection<HttpHeader> headerEntries) {
        this.addAll(headerEntries);
    }

    @Override
    public Iterator<HttpHeader> iterator() {
        return Collections.unmodifiableCollection(headers).iterator();
    }

    @Override
    public HttpHeader get(int index) {
        return headers.get(index);
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public boolean add(HttpHeader httpHeaderEntry) {
        if (httpHeaderEntry == null) {
            throw new IllegalArgumentException("httpHeaderEntry must not be null");
        }
        return headers.add(httpHeaderEntry);
    }

    public boolean add(String name, String value) {
        if (value == null) {
            throw new IllegalArgumentException("header value must not be null");
        }
        return addInternal(name, singletonList(value));
    }

    public boolean add(String headerName, List<String> values) {
        return addInternal(headerName, values);
    }

    private boolean addInternal(String headerName, List<String> values) {
        int existingHeaderIndex = indexOfHeader(headerName);

        if (existingHeaderIndex != -1) {
            HttpHeader existingHeader = get(existingHeaderIndex);
            List<String> existingValues = new ArrayList<>(existingHeader.getValues());
            existingValues.addAll(values);
            HttpHeader newHttpHeader = new HttpHeader(existingHeader.getName(), existingValues);
            headers.set(existingHeaderIndex, newHttpHeader);
            return true;
        } else {
            return headers.add(new HttpHeader(headerName, values));
        }
    }

    @Override
    public HttpHeader set(int index, HttpHeader element) {
        int indexOfHeader = indexOfHeader(element.getName());
        if (indexOfHeader > -1) {
            String msg = MessageFormat.format("Can not set header ''{0}'' at index {1}," +
                    " because a header with the same name already exists at {2}", element, index, indexOfHeader);
            throw new IllegalStateException(msg);
        }
        return headers.set(index, element);
    }

    @Override
    public HttpHeader remove(int index) {
        return headers.remove(index);
    }

    public HttpHeader get(String headerName) {
        int index = indexOfHeader(headerName);
        if (index == -1) {
            return null;
        }
        return get(index);
    }


    private int indexOfHeader(String headerName) {
        String normalizedName = HttpHeader.normalizedName(headerName);
        for (int i = 0; i < size(); i++) {
            HttpHeader httpHeader = get(i);
            if (httpHeader.getName().equalsIgnoreCase(normalizedName)) {
                return i;
            }
        }
        return -1;
    }
}

