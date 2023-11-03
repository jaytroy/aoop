package nl.rug.aoop.actions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Defines an order which can be sent over the network.
 */
public class Order implements Comparable<Order> {
    /**
     * Defines the type of order this is.
     */
    public enum Type {
        BUY, SELL
    }
    @Getter
    private Type type;
    @Getter
    private String clientId; //Or should it be a trader?
    @Getter
    private String symbol;
    @Setter
    @Getter
    private long quantity;
    @Getter
    private double price;
    @Getter
    private LocalDateTime timestamp;

    public Order(Type type, String clientId, String symbol, long quantity, double price, LocalDateTime tsp) {
        this.type = type;
        this.clientId = clientId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = tsp;
    }

    @Override
    public int compareTo(Order other) {
        if (this.price != other.price) {
            return this.type == Type.BUY ? Double.compare(other.price, this.price) : Double.compare(this.price, other.price);
        } else {
            return this.timestamp.compareTo(other.timestamp);
        }
    }
}
