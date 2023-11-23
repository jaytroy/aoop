package nl.rug.aoop.networking;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class NetworkingFacade {
    @Getter
    private Server server;

    public NetworkingFacade(MessageHandler messageHandler, int port) {
        this.server = new Server(messageHandler, port);
    }

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

    public void stopServer() {
        server.terminate();
        log.info("Server stopped");
    }

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
