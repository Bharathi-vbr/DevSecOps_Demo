import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class CalculatorHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/api/calc") && exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            handleApi(exchange);
        } else {
            File file = new File("index.html");
            byte[] html = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, html.length);
            exchange.getResponseBody().write(html);
            exchange.close();
        }
    }

    private void handleApi(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Map<String, String> params = parseFormData(body);

        double a = Double.parseDouble(params.getOrDefault("a", "0"));
        double b = Double.parseDouble(params.getOrDefault("b", "0"));
        String op = params.getOrDefault("op", "add");

        double result;
        switch (op) {
            case "subtract" -> result = a - b;
            case "multiply" -> result = a * b;
            case "divide" -> result = (b != 0) ? a / b : Double.NaN;
            default -> result = a + b;
        }

        String json = "{\"result\": " + result + "}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.length());
        exchange.getResponseBody().write(json.getBytes());
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
