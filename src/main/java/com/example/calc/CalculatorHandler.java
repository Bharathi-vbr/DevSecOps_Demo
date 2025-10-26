package com.example.calc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles HTTP requests for the calculator.
 * <p>This class is not designed for extension.</p>
 */
public final class CalculatorHandler implements HttpHandler {
    /**
     * HTTP status code for Not Found.
     */
    private static final int HTTP_NOT_FOUND = 404;
    /**
     * HTTP status code for OK.
     */
    private static final int HTTP_OK = 200;

    /**
     * Handles an HTTP exchange and routes to HTML or API handler.
     * @param exchange the HTTP exchange to process
     * @throws IOException if IO errors occur
     */
    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if ("/api/calc".equals(path)
                && "POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            handleApi(exchange);
        } else {
            serveHtml(exchange);
        }
    }

    /**
     * Serves the calculator HTML page.
     * @param exchange the HTTP exchange
     * @throws IOException if IO errors occur
     */
    private void serveHtml(final HttpExchange exchange) throws IOException {
        InputStream htmlStream =
                getClass().getClassLoader().getResourceAsStream("index.html");
        if (htmlStream == null) {
            String errorMsg = "<h1>index.html not found</h1>";
            exchange.sendResponseHeaders(HTTP_NOT_FOUND, errorMsg.length());
            exchange.getResponseBody().write(errorMsg.getBytes());
            exchange.close();
            return;
        }
        byte[] html = htmlStream.readAllBytes();
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(HTTP_OK, html.length);
        exchange.getResponseBody().write(html);
        exchange.close();
    }

    /**
     * Handles API calc requests and returns a JSON response.
     * @param exchange the HTTP exchange
     * @throws IOException if IO errors occur
     */
    private void handleApi(final HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Map<String, String> params = parseFormData(body);

        double a = Double.parseDouble(params.getOrDefault("a", "0"));
        double b = Double.parseDouble(params.getOrDefault("b", "0"));
        String op = params.getOrDefault("op", "add");

        double result = switch (op) {
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide" -> b != 0 ? a / b : Double.NaN;
            default -> a + b;
        };

        String response = "{\"result\": " + result + "}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(HTTP_OK, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    /**
     * Parses URL-encoded form data.
     * @param data the form data string
     * @return a map of field names to values
     */
    private Map<String, String> parseFormData(final String data) {
        Map<String, String> map = new HashMap<>();
        if (data == null || data.isEmpty()) {
            return map;
        }
        for (String pair : data.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }
}
