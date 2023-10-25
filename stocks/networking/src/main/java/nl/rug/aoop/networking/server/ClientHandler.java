package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles the input from the client, serverside. AKA MessageHandler.
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
    private MessageHandler msgHandler;

    /**
     * Constructor for class.
     *
     * @param socket The connection socket.
     * @param id     Client ID.
     * @param handler the message handler.
     * @throws IOException Thrown for errors with input/output streams.
     */
    public ClientHandler(MessageHandler handler, Socket socket, int id) throws IOException {
        this.socket = socket;
        this.id = id;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.msgHandler = handler;
    }

    @Override
    public void run() {
        running = true;
        sendMessage("Hello, enter 'quit' or 'QUIT' to exit. Your id : " + id);
        try {
            while (running) {
                String received = in.readLine();
                if (received == null || "QUIT".equalsIgnoreCase(received)) {
                    terminate();
                    break;
                }
                msgHandler.handleMessage(received);
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
            socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket ", e);
        }
    }

    /**
     * Send a message to the client.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        out.println(message);
    }
}