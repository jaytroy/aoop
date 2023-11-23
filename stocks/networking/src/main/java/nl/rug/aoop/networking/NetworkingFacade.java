package nl.rug.aoop.networking;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A facade for networking to be used on other packages.
 *
 */
@Slf4j
public class NetworkingFacade {
    @Getter
    private Server server;

    /**
     * Constructs a new netfacade with the specified messagehandler and port number.
     *
     * @param messageHandler The message handler for processing incoming messages.
     * @param port           The port number on which the server will listen.
     */
    public NetworkingFacade(MessageHandler messageHandler, int port) {
        this.server = new Server(messageHandler, port);
    }

    /**
     * Starts the server on a new thread.
     * Logs information about the server start status.
     */
    public void startServer() {
        try {
            server.start();
            Thread serverThread = new Thread(server);
            serverThread.start();
            log.info("Server started on port " + server.getPort());
        } catch (IOException e) {
            log.error("Failed to start the server: " + e.getMessage());
        }
    }

    /**
     * Stops the server and logs information about the server stop status.
     */
    public void stopServer() {
        server.terminate();
        log.info("Server stopped");
    }

    /**
     * Connects to a remote server at the specified address with the given client ID.
     * Logs information about the connection status.
     *
     * @param messageHandler The message handler for processing incoming messages.
     * @param address        The InetSocketAddress of the remote server.
     * @param clientId       The unique identifier for the client.
     */
    public void connectToServer(MessageHandler messageHandler, InetSocketAddress address, String clientId) {
        try {
            Client client = new Client(messageHandler, address, clientId);
            client.connect();
            Thread clientThread = new Thread(client);
            clientThread.start();
            log.info("Client connected to server at " + address);
        } catch (IOException e) {
            log.error("Failed to connect to the server: " + e.getMessage());
        }
    }
}
