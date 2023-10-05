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
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClientHandler {
    private static Server server;
    private static Client client
    private int id;
    private ClientHandler handler;
    private Socket socket;

    @BeforeAll
    public static void openSocket() {
        server = new Server(8000);
        try {
            server.start();
        } catch (IOException e) {
            fail("Server start failed in TestClientHandler");
        }

        client = new Client(new InetSocketAddress("localhost", 8000));

        socket =
    }

    @BeforeEach
    public void setup() {
        id = 10;
        try {
            handler = new ClientHandler(socket,id);
        } catch(IOException e) {
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

    @AfterAll
    public static void closeSocket() {
        try {
            socket.close();
        } catch(IOException e) {
           log.error("Socket closure failed in TestClientHandler");
           fail("Failed in closing socket");
        }
    }
}
