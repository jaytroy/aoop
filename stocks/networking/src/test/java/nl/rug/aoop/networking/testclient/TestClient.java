package nl.rug.aoop.networking.testclient;

import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClient {
    private InetSocketAddress address;
    private Client client;
    private InetSocketAddress badAddress;
    private Client badClient;
    private static Server server;

    @BeforeAll
    public static void startServer() {
        server = new Server(new DummyMessageHandler(), 8000);
        try {
            server.start();
        } catch (IOException e) {
            log.error("Server start failed in TestClient");
        }
    }

    @BeforeEach
    public void setup() {
        address = new InetSocketAddress("localhost", 8000);
        client = new Client(new DummyMessageHandler(), address);
        badAddress = new InetSocketAddress("localhost", 80);
        badClient = new Client(new DummyMessageHandler(), badAddress);
    }

    @Test
    public void testClientConstructor() {
        assertEquals(address, client.getAddress());
    }

    @Test
    public void testClientConnect() {
        try {
            client.connect();
            assertTrue(client.isConnected());
        } catch (IOException e) {
            fail("Should not have thrown an exception");
        }
        client.terminate();
    }

    @Test
    public void testClientConnectFail() {
        assertThrows(IOException.class, () -> {
            badClient.connect();
        });
    }

    static class DummyMessageHandler implements MessageHandler {
        @Override
        public void handleMessage(String message) {
            // Dummy implementation
        }
    }

    // MockServer class to simulate a server that listens for messages
    static class MockServer {
        private final ExecutorService executorService;
        private String receivedMessage;

        public MockServer() {
            executorService = Executors.newSingleThreadExecutor();
        }

        public void start() {
            executorService.submit(() -> {
                try (ServerSocket serverSocket = new ServerSocket(8000)) {
                    Socket socket = serverSocket.accept();
                    receivedMessage = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                } catch (IOException e) {
                    log.error("MockServer error: " + e.getMessage());
                }
            });
        }

        public void terminate() {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("MockServer did not terminate gracefully");
                }
            } catch (InterruptedException e) {
                log.error("MockServer termination interrupted");
            }
        }

        public String getReceivedMessage() {
            return receivedMessage;
        }
    }

    @Test
    public void testClientRun() {
        try {
            client.connect();
            assertTrue(client.isConnected());
        } catch (IOException e) {
            fail("Should not have thrown an exception");
        }

        Thread serverSimulator = new Thread(() -> {
            client.sendMessage("Message 1");
            client.sendMessage("Message 2");
            client.terminate();
        });

        serverSimulator.start();

        Thread clientThread = new Thread(client);
        clientThread.start();

        try {
            clientThread.join();
        } catch (InterruptedException e) {
            fail("Interrupted while waiting for client thread to finish");
        }

        assertFalse(client.isRunning());
        assertFalse(client.isConnected());
    }

    @Test
    public void testClientTerminate() {
        try {
            client.connect();
            assertTrue(client.isConnected());
        } catch (IOException e) {
            fail("Should not have thrown an exception");
        }

        client.terminate();

        assertFalse(client.isRunning());
        assertFalse(client.isConnected());
    }

    @AfterAll
    public static void endServer() {
        if (server != null) {
            server.terminate();
        }
    }

}
