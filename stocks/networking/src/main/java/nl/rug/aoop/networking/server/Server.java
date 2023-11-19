package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
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
    private MessageHandler handler;
    @Getter
    private ExecutorService threadPool;
    @Getter
    private final ConcurrentHashMap<String, ClientHandler> clientHandlers;

    /**
     * Server constructor.
     *
     * @param handler the message handler.
     * @param port The port to which the server connects to.
     */
    public Server(MessageHandler handler, int port) {
        this.port = port;
        this.handler = handler;
        this.clientHandlers = new ConcurrentHashMap<>();
    }

    /**
     * Starts the server.
     *
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
                ClientHandler handler = new ClientHandler(this.handler, acceptedSocket);
                String clientId = handler.getId();
                if (clientHandlers.containsKey(clientId)) {
                    log.error("Client ID {} is already connected. Rejecting the new connection.", clientId);
                    handler.terminate();
                } else {
                    clientHandlers.put(clientId, handler);
                    log.info("New connection from client: " + clientId + " ip: " + acceptedSocket.getRemoteSocketAddress());
                    threadPool.submit(() -> {
                        try {
                            handler.run();
                        } finally {
                            log.info("Removing client: " + clientId);
                            clientHandlers.remove(clientId);
                        }
                    });
                }
                log.info("Number of connected clients: " + clientHandlers.size());
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
        clientHandlers.values().forEach(ClientHandler::terminate);  // Terminate all connected clients
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
