package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;

import java.io.*;
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
    protected static final int TIMEOUT = 1000; //Is there any reason to keep this not private?
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
        this.socket = new Socket(); //Creates a new socket. A socket allows for communication via a port
        this.socket.connect(address, TIMEOUT); //Connects the socket to the address set in the constructor

        if (!this.socket.isConnected()) {
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect");
        }

        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); //Allows us to read from socket
        out = new PrintWriter(this.socket.getOutputStream()); //Allows us to write to the socket
    }

    /**
     * Sends a message to the server.
     * @param message The message being sent.
     * @throws IllegalArgumentException Thrown when an invalid messages is passed.
     */
    public void sendMessage(String message) throws IllegalArgumentException {  //Actually send messages. Limited?
        if (message == null || message.equals("")) { //These checks could be done within message no?
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }

        out.println(message); //Sends a message to the server via socket
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
                System.out.println("Running");
                String received = in.readLine(); //Reads the line from server
                if (received == null) {
                    log.error("Server disconnected.");
                    break;
                }

                /*
                if (messageHandler != null) { //This should not be here
                    System.out.println("handling messages");
                    messageHandler.handleMessage(msg);
                }
                 */
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
