package com.link_intersystems.net.http;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface HttpResponse {

    int getResponseCode() throws IOException;

    InputStream getContent() throws IOException;

    default String getContentAsString(Charset charset) throws IOException {
        StringWriter sw = new StringWriter();

        try (Reader reader = new BufferedReader(new InputStreamReader(getContent(), charset))) {
            int read;
            while ((read = reader.read()) != -1) {
                sw.append((char) read);
            }
        }
        return sw.toString();
    }
}
