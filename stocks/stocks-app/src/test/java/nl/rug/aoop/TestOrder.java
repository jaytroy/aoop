package nl.rug.aoop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import nl.rug.aoop.actions.Order;
public class TestOrder {

    @Test
    void testOrderCreation() {
        LocalDateTime timestamp = LocalDateTime.now();
        Order order = new Order(Order.Type.BUY, "client1", "AAPL", 100, 150.0, timestamp);

        assertEquals(Order.Type.BUY, order.getType());
        assertEquals("client1", order.getClientId());
        assertEquals("AAPL", order.getSymbol());
        assertEquals(100, order.getQuantity());
        assertEquals(150.0, order.getPrice());
        assertEquals(timestamp, order.getTimestamp());
    }

    @Test
    void testOrderComparison() {
        LocalDateTime timestamp1 = LocalDateTime.now();
        LocalDateTime timestamp2 = timestamp1.plusSeconds(1);

        Order buyOrder = new Order(Order.Type.BUY, "client1", "AAPL", 100, 150.0, timestamp1);
        Order sellOrder = new Order(Order.Type.SELL, "client2", "AAPL", 100, 150.0, timestamp2);

        assertTrue(buyOrder.compareTo(sellOrder) < 0);
        assertTrue(sellOrder.compareTo(buyOrder) > 0);
    }

    @Test
    void testOrderToJsonAndFromJson() {
        LocalDateTime timestamp = LocalDateTime.now();
        Order originalOrder = new Order(Order.Type.BUY, "client1", "AAPL", 100, 150.0, timestamp);

        String json = originalOrder.toJson();
        Order restoredOrder = Order.fromJson(json);

        assertNotEquals(originalOrder, restoredOrder);
    }
}
