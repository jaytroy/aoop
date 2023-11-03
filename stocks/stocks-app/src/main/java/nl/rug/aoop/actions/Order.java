package nl.rug.aoop.actions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.Gson;
import nl.rug.aoop.model.LocalDateTimeAdapter;

import java.time.LocalDateTime;

/**
 * Defines an order which can be sent over the network.
 */
public class Order implements Comparable<Order> {
    /**
     * Defines the type of order: BUY or SELL.
     */
    public enum Type {
        BUY, SELL
    }

    @Getter
    private Type type;
    @Getter
    private String clientId; // Or should it be a trader?
    @Getter
    private String symbol;
    @Setter
    @Getter
    private long quantity;
    @Getter
    private double price;
    @Getter
    private LocalDateTime timestamp;

    /**
     * Creates a new order with the specified parameters.
     *
     * @param type      The type of order (BUY or SELL).
     * @param clientId  The identifier of the client or trader placing the order.
     * @param symbol    The symbol of the stock associated with the order.
     * @param quantity  The quantity of shares in the order.
     * @param price     The price of the order.
     * @param timestamp The timestamp of the order.
     */
    public Order(Type type, String clientId, String symbol, long quantity, double price, LocalDateTime timestamp) {
        this.type = type;
        this.clientId = clientId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Order other) {
        if (this.price != other.price) {
            return this.type == Type.BUY ? Double.compare(other.price, this.price) : Double.compare(this.price,
                    other.price);
        } else {
            return this.timestamp.compareTo(other.timestamp);
        }
    }

    /**
     * Converts the order object to its JSON representation.
     *
     * @return The JSON representation of the order.
     */
    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson.toJson(this);
    }

    /**
     * Creates an Order object from its JSON representation.
     *
     * @param json The JSON representation of the Order.
     * @return The Order object created from JSON.
     */
    public static Order fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson.fromJson(json, Order.class);
    }
}
