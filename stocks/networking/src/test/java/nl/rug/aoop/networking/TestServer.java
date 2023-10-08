package nl.rug.aoop.networking;

import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class TestServer {
    private int port;
    private static Server server;

    @BeforeEach
    public void setup() {
        port = 8000;
        server = new Server(new DummyMessageHandler(), port);
    }

    @Test
    public void testServerConstructor() {
        assertEquals(port, server.getPort());
    }

    @Test
    public void testServerStartAndRun() {
        Thread serverThread = new Thread(server::run);
        serverThread.start();

        try {
            Thread.sleep(1000);

            assertTrue(server.isRunning());

            try (Socket clientSocket = new Socket("localhost", port)) {
                assertTrue(clientSocket.isConnected());

                OutputStream outputStream = clientSocket.getOutputStream();
                String messageToSend = "Hello, Server!";
                outputStream.write(messageToSend.getBytes());

                Thread.sleep(1000);

                assertEquals("Received: Hello, Server!", DummyMessageHandler.lastReceivedMessage);
            } catch (IOException e) {
                fail("Should not have thrown an exception when connecting the client.");
            }
        } catch (InterruptedException e) {
            fail("Interrupted while waiting for the server to start.");
        } finally {
            // Terminate the server
            server.terminate();
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
        if (server != null) {
            server.terminate();
        }
    }
}
