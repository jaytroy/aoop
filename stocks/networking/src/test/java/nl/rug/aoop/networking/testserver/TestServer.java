package nl.rug.aoop.networking.testserver;

import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class TestServer {
    private static int port;
    private static MessageHandler handler;
    private static Server server;

    @BeforeAll
    public static void setup() {
        port = 7500;
        handler = new DummyMessageHandler();
        server = new Server(handler, port);
    }

    @Test
    public void testServerConstructor() {
        assertEquals(port, server.getPort());
        assertEquals(handler, server.getMsgHandler());
    }

    @Test
    public void testServerStartAndRun() {
        try {
            server.start();
            assertTrue(server.isRunning());
        } catch (IOException e) {
            fail("Server failed to start.");
        }

        Thread serverThread = new Thread(server::run);
        serverThread.start();

        try {
            Thread.sleep(1000);

            try (Socket clientSocket = new Socket("localhost", port)) {
                assertTrue(clientSocket.isConnected());
            } catch (IOException e) {
                fail("Should not have thrown an exception when connecting the client.");
            }
        } catch (InterruptedException e) {
            fail("Interrupted while waiting for the server to start.");
        } finally {
            // Terminate the server
            if(server.getThreadPool() != null) {
                server.terminate();
            }
            serverThread.interrupt();
        }
    }
    static class DummyMessageHandler implements MessageHandler {
        static String lastReceivedMessage;

        @Override
        public void handleMessage(String message) {
            lastReceivedMessage = "Received: " + message;
        }
    }

    @AfterAll
    public static void endServer() {
        if (server != null && server.getThreadPool() != null) {
            server.terminate();

        }
    }
}
