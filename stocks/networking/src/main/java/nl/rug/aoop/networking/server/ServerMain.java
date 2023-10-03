package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Runs the server.
 */
@Slf4j
public class ServerMain {
    /**
     * Main function. Runs the server.
     * @param args Standard signature for main.
     */
    public static void main(String[] args) {
        try {
            Server server = new Server(8000);
            server.start();

            Thread stopThread = new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    while (true) {
                        String input = reader.readLine();
                        if ("QUIT".equalsIgnoreCase(input)) {
                            server.terminate();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            stopThread.start();

            Thread serverThread = new Thread(server);
            serverThread.start();
            System.out.println("Server started at port " + server.getPort());

        } catch (IOException e) {
            System.err.println("Could not start the server: " + e.getMessage());
        }
    }
}