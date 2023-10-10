package nl.rug.aoop.networking.testserver;

import nl.rug.aoop.networking.MessageHandler;
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
        serverSocket = new ServerSocket(7600);
        int port = serverSocket.getLocalPort();

        clientSocket = new Socket("localhost", port);

        clientHandler = new ClientHandler(new DummyMessageHandler(), clientSocket, 1);

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
        clientHandler.sendMessage("quit");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientHandler.terminate();

        assertFalse(clientHandler.isRunning());
    }

    @AfterEach
    public void tearDown() throws IOException {
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