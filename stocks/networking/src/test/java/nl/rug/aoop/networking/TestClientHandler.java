package nl.rug.aoop.networking;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.ClientHandler;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClientHandler {
    private static Server server;
    private static Client client;
    private int id;
    private ClientHandler handler;
    private Socket socket;

    @BeforeAll
    public static void setupServerAndClient() {
        server = new Server(new DummyMessageHandler(), 8000);
        try {
            server.start();
        } catch (IOException e) {
            fail("Server start failed in TestClientHandler");
        }

        client = new Client(new DummyMessageHandler(), new InetSocketAddress("localhost", 8000));
    }

    @BeforeEach
    public void setup() {
        id = 10;
        try {
            // Create a new socket for each test
            socket = new Socket("localhost", 8000);
            handler = new ClientHandler(new DummyMessageHandler(), socket, id);
        } catch (IOException e) {
            log.error("Handler creation failed in TestClientHandler");
            fail("Failed setting up handler");
        }
    }

    @Test
    public void testClientHandlerConstructor() {
        assertEquals(id, handler.getId());
        assertEquals(socket, handler.getSocket());
    }

    @Test
    public void testClientHandlerRun() {
        handler.run();
        assertTrue(handler.isRunning());
        handler.terminate();
    }

    @Test
    public void testClientHandlerTerminate() {
        handler.run();
        handler.terminate();
        assertFalse(handler.isRunning());
    }

    static class DummyMessageHandler implements MessageHandler {
        static String lastReceivedMessage;

        @Override
        public void handleMessage(String message) {
            lastReceivedMessage = "Received: " + message;
        }
    }


    @AfterAll
    public static void closeServer() {
        if (server != null) {
            server.terminate();
        }
    }
}
