package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    private int id = 0;
    @Getter
    private MessageHandler handler;
    @Getter
    private ExecutorService threadPool;

    /**
     * Server constructor.
     *
     * @param handler the message handler.
     * @param port The port to which the server connects to.
     */
    public Server(MessageHandler handler, int port) {
        this.port = port;
        this.handler = handler;
    }

    /**
     * Starts the server.
     * @throws IOException When an I/O error occurs opening the socket.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(10);
        running = true;
    }

    @Override
    public void run() {
        System.out.println("Server is running");
        if (!running) {
            log.error("Server is not started. Call start() before running.");
            return;
        }
        while (running) {
            try {
                Socket acceptedSocket = serverSocket.accept();
                log.info("New connection from client: " + acceptedSocket.getRemoteSocketAddress()); //Log which client exactly?

                threadPool.submit(new ClientHandler(handler, acceptedSocket, id)); //This will handle incoming messages from Client
                id++;
            } catch (IOException e) {
                log.error("Socket error: " + e.getMessage());
            }
        }

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
        threadPool.shutdown();
    }
}
