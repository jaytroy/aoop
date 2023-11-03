package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Implements the TCP client. AKA NetworkClient.
 */
@Slf4j
public class Client implements Runnable {
    /**
     * Sets the timeout length.
     */
    private String id;
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
    public Client(MessageHandler handler, InetSocketAddress address,String id) {
        this.address = address;
        this.msgHandler = handler;
        this.id = id;
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

        sendMessage(id); //Send the clients id for serverside identification
    }

    /**
     * Sends a message to the server.
     * @param message The message being sent.
     * @throws IllegalArgumentException Thrown when an invalid message is passed.
     */
    public void sendMessage(String message) throws IllegalArgumentException {
        if (message == null || message.equals("")) {
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }

        out.println(message);
        out.flush();
        log.info("Client sent message: " + message);
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
                //System.out.println("Client is running at Client");
                String received = in.readLine();
                log.info(id + " received: " + received);
                if (received == null) {
                    log.error("Server disconnected.");
                    break;
                }
                msgHandler.handleMessage(received);
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
