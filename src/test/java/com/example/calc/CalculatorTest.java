package com.example.calc;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculatorTest {

    private static HttpServer server;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", new CalculatorHandler());
        server.start();
        int port = server.getAddress().getPort();
        baseUrl = "http://127.0.0.1:" + port;
        System.out.println("Test server started at: " + baseUrl);
    }

    @AfterAll
    static void stopServer() {
        if (server != null) server.stop(0);
    }

    private static String readAll(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) sb.append(line);
            return sb.toString();
        }
    }

    private static String postForm(String url, String body) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
        String resp = readAll(conn.getInputStream());
        conn.disconnect();
        return resp;
    }

    private static double extractResult(String json) {
        Pattern p = Pattern.compile("\"result\"\\s*:\\s*([-0-9.]+|NaN)");
        Matcher m = p.matcher(json);
        if (!m.find()) throw new AssertionError("result not found in: " + json);
        return "NaN".equals(m.group(1)) ? Double.NaN : Double.parseDouble(m.group(1));
    }

    @Test @Order(1)
    void addOperation() throws Exception {
        String json = postForm(baseUrl + "/api/calc", "a=10&b=5&op=add");
        assertEquals(15.0, extractResult(json), 1e-9);
    }

    @Test @Order(2)
    void subtractOperation() throws Exception {
        String json = postForm(baseUrl + "/api/calc", "a=10&b=5&op=subtract");
        assertEquals(5.0, extractResult(json), 1e-9);
    }

    @Test @Order(3)
    void multiplyOperation() throws Exception {
        String json = postForm(baseUrl + "/api/calc", "a=3&b=7&op=multiply");
        assertEquals(21.0, extractResult(json), 1e-9);
    }

    @Test @Order(4)
    void divideOperation() throws Exception {
        String json = postForm(baseUrl + "/api/calc", "a=12&b=3&op=divide");
        assertEquals(4.0, extractResult(json), 1e-9);
    }

    @Test @Order(5)
    void divideByZeroReturnsNaN() throws Exception {
        String json = postForm(baseUrl + "/api/calc", "a=1&b=0&op=divide");
        assertTrue(Double.isNaN(extractResult(json)));
    }
}
