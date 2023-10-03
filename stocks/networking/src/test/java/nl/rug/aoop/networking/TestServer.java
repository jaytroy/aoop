package nl.rug.aoop.networking;

import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestServer {
    private int port;
    private int badPort;
    private Server server;
    private Server badServer;
    @BeforeEach
    public void setup() {
        port = 8000;
        badPort = 80;
        server = new Server(port);
        badServer = new Server(badPort);
    }
    @Test
    public void testServerConstructor() {
        assertEquals(port, server.getPort());
    }

    @Test
    public void testServerStart() {
        try {
            server.start();
            assertTrue(server.isRunning());
        } catch (IOException e) {
            fail("Should not have thrown an exception.");
        }
        server.terminate();
    }

    @Test
    public void testServerStartFail() {
        assertThrows(IOException.class, () -> badServer.start());
    }

    //Part of runnable interface. Does it need to be tested? Can it be tested? Everything should work fine as long as running = true.
    @Test
    public void testSeverRun() {

    }

    @Test
    public void testServerTerminate() {
        try {
            server.start();
        } catch(IOException e) {
            fail("Should not have thrown an exception");
        }
        server.terminate();
        assertFalse(server.isRunning());
    }
}
