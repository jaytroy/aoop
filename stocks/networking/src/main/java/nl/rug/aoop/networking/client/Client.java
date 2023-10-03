package nl.rug.aoop.networking.client;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The client class.
 */
@Slf4j
public class Client implements Runnable {
    public static final int TIMEOUT = 1000;
    private InetSocketAddress address;
    private Socket socket;
    private boolean running = false;
    private volatile boolean connected = false;
    private BufferedReader in;
    private PrintWriter out;
    private MessageHandler messageHandler;

    public Client(InetSocketAddress address) {
        this.address = address;
    }
    public void connect() throws IOException {
        this.socket = new Socket();
        this.socket.connect(address, TIMEOUT);
        if (!this.socket.isConnected()) {
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect");
        }
        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream());
    }

    public boolean isConnected() {
        return connected;
    }

    public void sendMessage(String message) throws IllegalArgumentException {
        if (message == null || message.equals("")) {
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }
        out.println(message);
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public void run() {
        if (!connected) {
            log.error("Client is not connected. Call connect() before running.");
            return;
        }
        running = true;
        try {
            while (running) {
                System.out.println("running");
                String fromServer = in.readLine();
                if (fromServer == null) {
                    log.error("Server disconnected.");
                    break;
                }
                log.info("Server sent: " + fromServer);
                if (messageHandler != null) {
                    System.out.println("handling messages");
                    messageHandler.handleMessage(fromServer);
                }
            }
        } catch (IOException e) {
            log.error("Error reading from server: " + e.getMessage());
        } finally {
            terminate();
        }
    }

    public void terminate() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log.error("Error closing socket: " + e.getMessage());
        }
    }
}
