package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Implements the TCP client.
 */
@Slf4j
public class Client implements Runnable {
    /**
     * Sets the timeout length.
     */
    protected static final int TIMEOUT = 1000; //Is there any reason to keep this not private?
    private InetSocketAddress address;
    private Socket socket;
    private boolean running = false;
    private volatile boolean connected = false;
    private BufferedReader in;
    private PrintWriter out;
    private MessageHandler messageHandler;

    /**
     * Client constructor.
     * @param address The socket host name and port address.
     */
    public Client(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Connects the client to a server.
     * @throws IOException Thrown when a connection to the socket cannot be established.
     */
    public void connect() throws IOException {
        this.socket = new Socket(); //Creates a new socket. A socket allows for communication via a port
        this.socket.connect(address, TIMEOUT); //Connects to socket to the address set in the constructor

        if (!this.socket.isConnected()) {
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect");
        }

        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); //Allows us to read from socket
        out = new PrintWriter(this.socket.getOutputStream()); //Allows us to write to the socket
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * Sends a message to somewhere? It just prints it atm.
     * @param message The message being sent.
     * @throws IllegalArgumentException Thrown when an invalid messages is passed.
     */
    public void sendMessage(String message) throws IllegalArgumentException {  //Actually send messages. Limited?
        if (message == null || message.equals("")) { //These checks could be done within message no?
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }
        out.println(message);
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
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
                System.out.println("running");
                String fromServer = in.readLine(); //Reads the line from console
                if (fromServer == null) {
                    log.error("Server disconnected.");
                    break;
                }
                log.info("Server sent: " + fromServer); //Completing the handshake
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

    /**
     * Terminates the TCP client.
     */
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
