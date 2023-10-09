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
    private StringWriter output;
    private InputStream testInput;

    @BeforeEach
    public void setup() throws IOException {
        // Create a ServerSocket for testing
        serverSocket = new ServerSocket(0); // 0 means the system will choose a random available port
        int port = serverSocket.getLocalPort();

        // Create a ClientSocket to connect to the server
        clientSocket = new Socket("localhost", port);

        output = new StringWriter();
        PrintWriter out = new PrintWriter(output, true);

        // Create a BufferedReader for reading from the client socket
        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        clientHandler = new ClientHandler(new DummyMessageHandler(), clientSocket, 1);

        // Start the ClientHandler in a separate thread
        Thread clientHandlerThread = new Thread(clientHandler);
        clientHandlerThread.start();

        // Create a separate input stream for testing with sample data
        testInput = new ByteArrayInputStream("Test message\nQUIT\n".getBytes());
    }

    @Test
    public void testClientHandler() throws IOException {
        // Send a message to the ClientHandler
        PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
        clientOut.println("Test message");

        // Read the message from the ClientHandler
        String receivedMessage = output.toString();

        // Verify that the received message matches the sent message
        assertEquals("Hello, enter 'quit' or 'QUIT' to exit. Your id : 1\nReceived: Test message\n", receivedMessage);

        // Wait for the ClientHandler to terminate
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the ClientHandler has terminated
        assertFalse(clientHandler.isRunning());
    }

    @Test
    public void testClientHandlerTermination() throws IOException {
        // Terminate the ClientHandler
        clientHandler.terminate();

        // Wait for the ClientHandler to terminate
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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