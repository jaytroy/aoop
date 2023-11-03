package nl.rug.aoop;

import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.TSMessageQueue;
import nl.rug.aoop.model.Exchange;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestExchange {
    MessageQueue queue;
    Server server;
    Exchange exchange;
    @BeforeEach
    public void setup() {
        queue = Mockito.mock(TSMessageQueue.class);
        server = Mockito.mock(Server.class);
        exchange = new Exchange(queue,server);
    }
    @Test
    public void testConstructor() {
        assertEquals();

    }
}
