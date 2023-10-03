package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


@Slf4j
public class ClientHandler implements Runnable {
    private Socket socket;
    private int id;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running = true; // Initialize as true

    public ClientHandler(Socket socket, int id) throws IOException {
        this.socket = socket;
        this.id = id;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        running = true;
        out.println("Hello, enter 'quit' or 'QUIT' to exit. Your id : " + id);
        try {
            while (running) {
                String fromClient = in.readLine();
                if (fromClient == null || "QUIT".equalsIgnoreCase(fromClient)) {
                    terminate();
                    break;
                }
                log.info("Received from client: " + fromClient);

            }
        } catch (IOException e) {
            log.error("Error reading from client", e);
        }
    }


    public void terminate() {
        running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket ", e);
        }
    }

}
