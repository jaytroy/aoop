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
import static org.mockito.Mockito.mock;

public class TestClientHandler {
    private ClientHandler clientHandler;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private MessageHandler handler;

    @BeforeEach
    public void setup() throws IOException {
        /*
        serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();

        clientSocket = new Socket("localhost", port);
        handler = mock(MessageHandler.class);
        clientHandler = new ClientHandler(handler, clientSocket);

        Thread clientHandlerThread = new Thread(clientHandler);
        clientHandlerThread.start();
        */
    }

    @Test
    public void testClientHandlerRunning() {
        /*
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(clientHandler.isRunning());
         */
    }


    @Test
    public void testClientHandlerTermination() {
        /*
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientHandler.terminate();

         */


        //assertFalse(clientHandler.isRunning());
    }

    @AfterEach
    public void tearDown() throws IOException {
        //clientHandler.terminate();
        //clientSocket.close();
        //serverSocket.close();
    }
}