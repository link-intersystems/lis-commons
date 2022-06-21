package com.link_intersystems.net.http;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface HttpResponse {

    public int getResponseCode() throws IOException;

    public HttpHeaders getHeaders();

    public InputStream getContent() throws IOException;

    /**
     * @return the content of this {@link HttpResponse} {@link StandardCharsets#UTF_8} decoded.
     */
    default public String getDecodedContent() throws IOException {
        return getDecodedContent(StandardCharsets.UTF_8);
    }

    /**
     * @param charset the charset to use for decoding.
     * @return the content of this {@link HttpResponse} decoded by the given {@link Charset}.
     */
    default public String getDecodedContent(Charset charset) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (Reader reader = new BufferedReader(new InputStreamReader(getContent(), charset))) {
            for (int read = reader.read(); read != -1; read = reader.read()) {
                sb.append((char) read);
            }
        }

        return sb.toString();
    }
}
