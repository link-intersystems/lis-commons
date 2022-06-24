package com.link_intersystems.net.http.test;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpMockServer implements Closeable, Runnable {

    private ReceivedRequest latestRequest;

    private Map<String, RequestMocking> requestMockings = new LinkedHashMap<>();

    public OngoingRequestMocking whenRequestPath(String requestPath) {
        return new OngoingRequestMocking(requestPath, requestMockings);
    }

    public static class ReceivedRequest {
        private String method;
        private String path;
        private String version;

        private Map<String, String> headers = new LinkedHashMap<>();
        private byte[] body;

        public ReceivedRequest(String method, String path, String version) {
            this.method = method;
            this.path = path;
            this.version = version;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        public String getVersion() {
            return version;
        }

        public void setBody(byte[] body) {
            this.body = body;
        }

        void addHeader(String name, String value) {
            headers.put(name, value);
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public byte[] getBody() {
            return body;
        }
    }

    private ServerSocket serverSocket;
    private Thread socketHandlerThread;
    private CountDownLatch countDownLatch = new CountDownLatch(1);


    public void start() throws IOException {
        serverSocket = new ServerSocket(findAvailablePort());

        socketHandlerThread = new Thread(this);
        socketHandlerThread.start();
    }

    public void stop() {
        if (socketHandlerThread != null) {
            socketHandlerThread.interrupt();
            Thread.yield();
            try {
                countDownLatch.await(250, MILLISECONDS);
            } catch (InterruptedException e) {
                socketHandlerThread.stop();
            }
        }
    }

    @Override
    public void run() {
        try {
            Socket accept = serverSocket.accept();
            OutputStream out = accept.getOutputStream();
            InputStream in = accept.getInputStream();

            latestRequest = receiveRequest(in);
            writeResponse(out, latestRequest);
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            countDownLatch.countDown();
        }
    }

    private void writeResponse(OutputStream out, ReceivedRequest receivedRequest) throws IOException {
        RequestMocking requestMocking = requestMockings.get(receivedRequest.getPath());
        if (requestMocking == null) {
            writeResponse(out, 400);
        } else {
            writeResponse(out, requestMocking);
        }
    }

    private void writeResponse(OutputStream out, int responseCode) throws IOException {
        out.write("HTTP/1.1".getBytes(StandardCharsets.UTF_8));
        out.write(" ".getBytes(StandardCharsets.UTF_8));
        out.write(Integer.toString(responseCode).getBytes(StandardCharsets.UTF_8));
        out.write(" OK\n".getBytes(StandardCharsets.UTF_8));
    }

    private void writeResponse(OutputStream out, RequestMocking requestMocking) throws IOException {
        int responseCode = requestMocking.getResponseCode();
        writeResponse(out, responseCode);

        byte[] body = requestMocking.getBody();
        if (body != null) {
            out.write("\n".getBytes(StandardCharsets.UTF_8));
            out.write(body);
        }
    }

    private ReceivedRequest receiveRequest(InputStream in) throws IOException {
        String request = readLine(in);
        String[] parts = request.split("\\s");
        ReceivedRequest receivedRequest = new ReceivedRequest(parts[0], parts[1], parts[2]);
        readHeaders(in, receivedRequest);
        readBody(in, receivedRequest);
        return receivedRequest;
    }

    private void readBody(InputStream in, ReceivedRequest receivedRequest) throws IOException {
        int contentLength = Integer.MAX_VALUE;

        Map<String, String> headers = receivedRequest.getHeaders();
        String contentLengthValue = headers.get("Content-Length");
        if (contentLengthValue != null) {
            contentLength = Integer.valueOf(contentLengthValue);
        }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int readCount = 0;

        int read;
        while (readCount < contentLength) {
            int available = in.available();
            if (available < 1) {
                break;
            }
            read = in.read();
            if (read == -1) {
                break;
            }
            readCount++;
            bout.write(read);
        }

        receivedRequest.setBody(bout.toByteArray());
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();

        int read;

        while ((read = in.read()) != -1) {
            char c = (char) read;
            if (c == '\r') {
                continue;
            }
            if (c == '\n') {
                break;
            }
            sb.append(c);
        }

        return sb.toString();
    }

    private void readHeaders(InputStream in, ReceivedRequest receivedRequest) throws IOException {
        String line;

        while ((line = readLine(in)) != null) {
            if (line.isEmpty()) {
                break;
            }

            String[] header = line.split(": ");
            receivedRequest.addHeader(header[0], header[1]);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            socketHandlerThread.stop();
        } catch (Exception e) {
        }
        serverSocket.close();
    }

    public static int findAvailablePort() {
        return IntStream.range(50000, 65535).filter(HttpMockServer::available).findFirst().orElseThrow(() -> new IllegalStateException("no port available"));
    }

    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }


    public URL getBaseUrl() {
        try {
            return new URL("http://localhost:" + serverSocket.getLocalPort());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    public ReceivedRequest getLatestRequest() {
        return latestRequest;
    }


}
