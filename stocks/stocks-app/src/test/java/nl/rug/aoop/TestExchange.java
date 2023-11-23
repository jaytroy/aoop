package nl.rug.aoop;

import nl.rug.aoop.model.Exchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.server.ClientHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.model.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class TestExchange {

    private Exchange exchange;
    private Server server;
    private MessageQueue messageQueue;

    @BeforeEach
    void setUp() {
        messageQueue = mock(MessageQueue.class);
        server = mock(Server.class);
        exchange = new Exchange(messageQueue, server);
    }

    @Test
    void initializeStocks() {
        List<Stock> stocks = exchange.initializeStocks();
        assertNotNull(stocks);
    }

    @Test
    void initializeTraders() {
        List<Trader> traders = exchange.initializeTraders();
        assertNotNull(traders);
    }
}
