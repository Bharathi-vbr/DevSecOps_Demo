package com.example.calc;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Entry point and HTTP server setup for the Calculator API.
 */
public final class CalculatorServer {
    /**
     * Private constructor to prevent instantiation.
     */
    private CalculatorServer() {
        // Utility class: no instances allowed.
    }

    /**
     * Starts the server.
     * @param args command line arguments (not used)
     * @throws IOException if server startup fails
     */
    public static void main(final String[] args) throws IOException {
        final int serverPort = 8080;
        HttpServer server = HttpServer.create(
            new InetSocketAddress(serverPort), 0);
        server.createContext(
            "/", new CalculatorHandler());
        server.setExecutor(null); // default executor
        System.out.println(
            "Calculator server is running on port " + serverPort);
        server.start();
    }
}
