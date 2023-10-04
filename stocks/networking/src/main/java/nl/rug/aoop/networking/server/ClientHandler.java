package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles the input from the client, serverside.
 */
@Slf4j
public class ClientHandler implements Runnable {
    @Getter
    private Socket socket;
    @Getter
    private int id;
    private final BufferedReader in;
    private final PrintWriter out;
    @Getter
    private boolean running = false;

    /**
     * Constructor for class.
     * @param socket The connection socket.
     * @param id Client ID.
     * @throws IOException Thrown for errors with input/output streams.
     */
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
                if (fromClient == null || "QUIT".equalsIgnoreCase(fromClient)) { //Handler terminates if client terms.
                    terminate();
                    break;
                }
                log.info("Received from client: " + fromClient);
            }
        } catch (IOException e) {
            log.error("Error reading from client", e);
        }
    }

    /**
     * Terminates client handler.
     */
    public void terminate() {
        running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket ", e);
        }
    }

}
