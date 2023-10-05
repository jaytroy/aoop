package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implements the TCP server. AKA NetworkServer.
 */
@Slf4j
public class Server implements Runnable {
    @Getter
    private final int port;
    private ServerSocket serverSocket;
    @Getter
    private volatile boolean running = false;
    @Getter
    private ExecutorService service;
    private int id = 0;
    private MessageHandler msgHandler;

    /**
     * Server constructor.
     * @param port The port to which the server connects to.
     */
    public Server(MessageHandler handler, int port) {
        this.port = port;
        this.msgHandler = handler;
    }

    /**
     * Starts the server.
     * @throws IOException When an I/O error occurs opening the socket.
     */
    public void start() throws IOException { //Create a thread here which starts the server?
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();
        running = true;
    }

    @Override
    public void run() {
        if (!running) {
            log.error("Server is not started. Call start() before running.");
            return;
        }
        while (running) {
            try {
                Socket acceptedSocket = serverSocket.accept();
                log.info("New connection from client");

                Thread clientThread = new Thread(new ClientHandler(acceptedSocket, id));
                clientThread.start();
                //service.submit(clientHandler);
                id++;
            } catch (IOException e) {
                log.error("Socket error: " + e.getMessage());
            }
        }
    }

    /**
     * Should receive a message. Is this necessary?
     */
    public void receiveMessage() {

    }

    /**
     * Terminates the server.
     */
    public void terminate() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error("Error closing server socket: " + e.getMessage());
        }
        service.shutdown();
    }

}
