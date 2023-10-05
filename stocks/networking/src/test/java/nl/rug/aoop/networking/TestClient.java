package nl.rug.aoop.networking;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;

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
        server = new Server(8000);
        try{
            server.start();
        } catch (IOException e) {
            log.error("Server start failed in TestClient");
        }
    }

    @BeforeEach
    public void setup() {
        address = new InetSocketAddress("localhost", 8000);
        client = new Client(address);
        badAddress = new InetSocketAddress("localhost", 80);
        badClient = new Client(badAddress);
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
        } catch(IOException e) {
            fail("Should not have thrown an exception");
        }
        client.terminate();
    }

    @Test
    public void testClientConnectFail() {
        assertThrows(IOException.class, () -> {badClient.connect();});
    }

    //Not yet implemented
    @Test
    public void testClientSendMessage() {

    }

    //Not yet implemented
    @Test
    public void testClientSendIllegalMessage() {

    }

    @Test
    public void testClientTerminate() {
        try {
            client.connect();
        } catch(IOException e) {
            fail("Should not have thrown an exception");
        }
        client.terminate();
        assertFalse(client.isRunning());
    }

    @AfterAll
    public static void endServer() {
        if(server.getService() != null) {
            server.terminate();
        }
    }
}
