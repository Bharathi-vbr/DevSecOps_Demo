package com.example.calc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CalculatorHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/api/calc") && exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            handleApi(exchange);
        } else {
            serveHtml(exchange);
        }
    }

    private void serveHtml(HttpExchange exchange) throws IOException {
        // Load index.html from classpath (src/main/resources)
        InputStream htmlStream = getClass().getClassLoader().getResourceAsStream("index.html");
        if (htmlStream == null) {
            String errorMsg = "<h1>index.html not found</h1>";
            exchange.sendResponseHeaders(404, errorMsg.length());
            exchange.getResponseBody().write(errorMsg.getBytes());
            exchange.close();
            return;
        }
        byte[] html = htmlStream.readAllBytes();
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, html.length);
        exchange.getResponseBody().write(html);
        exchange.close();
    }

    private void handleApi(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Map<String, String> params = parseFormData(body);

        double a = Double.parseDouble(params.getOrDefault("a", "0"));
        double b = Double.parseDouble(params.getOrDefault("b", "0"));
        String op = params.getOrDefault("op", "add");

        double result = switch (op) {
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide"   -> (b != 0 ? a / b : Double.NaN);
            default         -> a + b;
        };

        String response = "{\"result\": " + result + "}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    private Map<String, String> parseFormData(String data) {
        Map<String, String> map = new HashMap<>();
        if (data == null || data.isEmpty()) return map;
        for (String pair : data.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }
}
