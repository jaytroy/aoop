package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implements the TCP client. AKA NetworkClient.
 */
@Slf4j
public class Client implements Runnable {
    /**
     * Sets the timeout length.
     */
    protected static final int TIMEOUT = 1000;
    @Getter
    private InetSocketAddress address;
    private Socket socket;
    @Getter
    private boolean running = false;
    @Getter
    private volatile boolean connected = false;
    private BufferedReader in;
    private PrintWriter out;
    @Setter
    private MessageHandler msgHandler;

    /**
     * Client constructor.
     *
     * @param handler the message handler.
     * @param address The socket host name and port address.
     */
    public Client(MessageHandler handler, InetSocketAddress address) {
        this.address = address;
        this.msgHandler = handler;
    }

    /**
     * Connects the client to a server.
     * @throws IOException Thrown when a connection to the socket cannot be established.
     */
    public void connect() throws IOException {
        this.socket = new Socket();
        this.socket.connect(address, TIMEOUT);

        if (!this.socket.isConnected()) {
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect at Client");
        }

        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream());
    }

    /**
     * Sends a message to the server.
     * @param message The message being sent.
     * @throws IllegalArgumentException Thrown when an invalid messages is passed.
     */
    public void sendMessage(String message) throws IllegalArgumentException {
        if (message == null || message.equals("")) {
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }

        out.println(message);
    }

    /**
     * Runs the TCP client.
      */
    public void run() {
        if (!connected) {
            log.error("Client is not connected. Call connect() before running.");
            return;
        }

        running = true;
        try {
            while (running) {
                System.out.println("Client is running at Client");
                String received = in.readLine();
                if (received == null) {
                    log.error("Server disconnected.");
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error reading from server: " + e.getMessage());
        } finally {
            terminate();
        }
    }

    /**
     * Terminates the TCP client.
     */
    public void terminate() {
        running = false;
        connected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log.error("Error closing socket: " + e.getMessage());
        }
    }
}
