package nl.rug.aoop.networking;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.server.ClientHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClientHandler {
    private static Socket socket;
    private int id;
    private ClientHandler handler;

    @BeforeAll
    public static void openSocket() {
        try {
            ServerSocket serv = new ServerSocket(8000);
            socket = serv.accept();
        } catch(Exception e) {
            log.error("Socket creation failed in TestClientHandler");
        }
    }

    @BeforeEach
    public void setup() {
        id = 10;
        try {
            handler = new ClientHandler(socket,id);
        } catch(IOException e) {
            log.error("Handler creation failed in TestClientHandler");
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
        }
    }
}
