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
 * Handles the input from the client, serverside. Can send output.
 */
@Slf4j
public class ClientHandler implements Runnable {
    @Getter
    private Socket socket;
    @Getter
    private String id;
    private final BufferedReader in;
    private final PrintWriter out;
    @Getter
    private boolean running = false;
    private MessageHandler handler;

    /**
     * Constructor for class.
     *
     * @param socket  The connection socket.
     * @param handler the message handler.
     * @throws IOException Thrown for errors with input/output streams.
     */
    public ClientHandler(MessageHandler handler, Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.handler = handler;

        // Read the client ID as the first line of the connection
        this.id = in.readLine();
        if (this.id == null || this.id.trim().isEmpty()) {
            throw new IOException("Client did not send an ID");
        }

        log.info("New handler created for " + this.id + " ip: "+ socket.getRemoteSocketAddress());
    }

    @Override
    public void run() {
        log.info("ClientHandler is running");
        running = true;
        try {
            while (running) {
                String received = in.readLine();
                log.info("Received message from " + id + " ip:" + socket.getRemoteSocketAddress() + ": " + received);

                if(received != null) {
                    handler.handleMessage(received);
                } else {
                    return;
                }

                //TCP handshake
                sendMessage("Server received message");
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
        //server.terminateClientHandler(this.id)
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
    public void sendMessage(String message) { //Sends a message back to the client
        out.println(message);
        out.flush();
    }
}
