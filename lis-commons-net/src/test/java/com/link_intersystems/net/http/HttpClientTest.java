package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.link_intersystems.net.http.HttpMethod.*;

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

    static Stream<HttpMethod> allHttpMethods() {
        return Stream.of(GET, POST, PUT, DELETE);
    }

    static Stream<HttpMethod> contentSupportedHttpMethods() {
        return Stream.of(POST, PUT, DELETE);
    }

    private Method getHttpClientMethod(HttpMethod httpMethod, Class<?>... params) throws NoSuchMethodException {
        return HttpClient.class.getDeclaredMethod(httpMethod.name().toLowerCase(), params);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestUrlString(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method httpClientMethod = getHttpClientMethod(httpMethod, String.class);

        httpClientMethod.invoke(httpClient, TEST_URL);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), EMPTY_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestUrlStringWithContent(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, String.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, TEST_URL, contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestUrlStringWithHeadersAndContent(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, String.class, Map.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, TEST_URL, headers, contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestUrlStringWithHeaders(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, String.class, Map.class);

        httpClientMethod.invoke(httpClient, TEST_URL, headers);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, EMPTY_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestURL(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, URL.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL));

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), EMPTY_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestURLWithContent(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, URL.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL), contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, Collections.emptyMap(), TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("contentSupportedHttpMethods")
    void requestURLWithHeadersAndContent(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, URL.class, Map.class, ContentWriter.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL), headers, contentWriter);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, TEST_CONTENT);
    }

    @ParameterizedTest
    @MethodSource("allHttpMethods")
    void requestURLWithHeaders(HttpMethod httpMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method httpClientMethod = getHttpClientMethod(httpMethod, URL.class, Map.class);

        httpClientMethod.invoke(httpClient, new URL(TEST_URL), headers);

        requestMocking.assertRequestWithContent(httpMethod, TEST_URL, headers, EMPTY_CONTENT);
    }

}