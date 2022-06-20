package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpClientTest {

    public static final String TEST_URL = "https://www.link-intersystems.com";
    public static final byte[] TEST_CONTENT = "Hello World".getBytes(StandardCharsets.UTF_8);
    private static final byte[] EMPTY_CONTENT = new byte[0];

    private HttpClient httpClient;
    private HttpRequestFactoryMocking requestMocking;
    private Map<String, String> headers;
    private ContentWriter contentWriter;

    @BeforeEach
    void setUp() throws IOException {
        requestMocking = new HttpRequestFactoryMocking();
        httpClient = new HttpClient(requestMocking);

        headers = new LinkedHashMap<>();

        headers.put("Accept", "application/json");
        headers.put("Content-Length", "1042");

        contentWriter = outputStream -> outputStream.write("Hello World".getBytes(StandardCharsets.UTF_8));
    }

    static Stream<String> allHttpMethods() {
        return Stream.of("POST", "PUT", "DELETE", "GET");
    }

    static Stream<String> contentSupportedHttpMethods() {
        return Stream.of("POST", "PUT", "DELETE");
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestUrlString(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), String.class);

        httpClientMethod.invoke(httpClient, TEST_URL);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), EMPTY_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestUrlStringWithContent(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), String.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, TEST_URL, contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestUrlStringWithHeadersAndContent(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), String.class, Map.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, TEST_URL, headers, contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestUrlStringWithHeaders(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), String.class, Map.class);

        httpClientMethod.invoke(httpClient, TEST_URL, headers);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, EMPTY_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestURL(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), URL.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL));

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), EMPTY_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestURLWithContent(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), URL.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL), contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestURLWithHeadersAndContent(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), URL.class, Map.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL), headers, contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestURLWithHeaders(String httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = HttpClient.class.getDeclaredMethod(httpMethod.toLowerCase(), URL.class, Map.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL), headers);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, EMPTY_CONTENT);
    }

}