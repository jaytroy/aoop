package nl.rug.aoop.networking;

import nl.rug.aoop.networking.server.ClientHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientHandler {
    private ClientHandler clientHandler;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    @BeforeEach
    public void setup() throws IOException {
        // Create a ServerSocket for testing
        serverSocket = new ServerSocket(7600);
        int port = serverSocket.getLocalPort();

        // Create a ClientSocket to connect to the server
        clientSocket = new Socket("localhost", port);

        clientHandler = new ClientHandler(new DummyMessageHandler(), clientSocket, 1);

        // Start the ClientHandler in a separate thread
        Thread clientHandlerThread = new Thread(clientHandler);
        clientHandlerThread.start();
    }

    @Test
    public void testClientHandlerRunning() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(clientHandler.isRunning());
    }


    @Test
    public void testClientHandlerTermination() {
        // Send a message to the ClientHandler
        clientHandler.sendMessage("quit");

        // Add a small delay to allow the ClientHandler to process the termination message
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientHandler.terminate();

        // Verify that the ClientHandler has terminated
        assertFalse(clientHandler.isRunning());
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Close the client socket and server socket
        clientSocket.close();
        serverSocket.close();
    }

    static class DummyMessageHandler implements MessageHandler {
        @Override
        public void handleMessage(String message) {
            // Dummy implementation
        }
    }
}