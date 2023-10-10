package nl.rug.aoop.messagequeue.testserverside;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.NetProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import nl.rug.aoop.networking.client.Client;
import org.mockito.Mockito;

public class TestNetProducer {
    private NetProducer netProducer;
    private Client mockClient;

    @BeforeEach
    public void setUp() {
        mockClient = Mockito.mock(Client.class);
        netProducer = new NetProducer(mockClient);
    }

    @Test
    public void testPutMessage() {
        Message message = new Message("header", "body");

        netProducer.putMessage(message);

        String expectedJson = message.toJson();
        Mockito.verify(mockClient).sendMessage(expectedJson);
    }
}
