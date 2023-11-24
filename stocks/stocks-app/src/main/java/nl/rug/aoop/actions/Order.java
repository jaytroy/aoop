package nl.rug.aoop.actions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.Gson;
import nl.rug.aoop.model.typeadapters.LocalDateTimeAdapter;

import java.time.LocalDateTime;

/**
 * Defines an order which can be sent over the network.
 */
public class Order implements Comparable<Order> {
    /**
     * Defines the type of order: BUY or SELL.
     */
    public enum Action {
        BUY, SELL
    }

    public enum Type {
        MARKET, LIMIT
    }

    @Getter
    private Action action;
    @Getter
    private String clientId;
    @Getter
    private String symbol;
    @Setter
    @Getter
    private long quantity;
    @Getter
    private double price;
    @Getter
    private LocalDateTime timestamp;
    @Getter
    private Type type;

    /**
     * Creates a new limit order with the specified parameters. (Price specified).
     *
     * @param clientId  The identifier of the client or trader placing the order.
     * @param symbol    The symbol of the stock associated with the order.
     * @param quantity  The quantity of shares in the order.
     * @param price     The price of the order.
     * @param timestamp The timestamp of the order.
     */
    public Order(Action action, String clientId, String symbol, long quantity, double price, LocalDateTime timestamp) {
        this.type = Type.LIMIT;

        this.action = action;
        this.clientId = clientId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    /**
     * Creates a new market order with the specified parameters. (No price specified. Buys at market price).
     *
     * @param clientId  The identifier of the client or trader placing the order.
     * @param symbol    The symbol of the stock associated with the order.
     * @param quantity  The quantity of shares in the order.
     * @param timestamp The timestamp of the order.
     */
    public Order(Action action, String clientId, String symbol, long quantity, LocalDateTime timestamp) {
        this.type = Type.MARKET;

        this.action = action;
        this.clientId = clientId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }


    @Override
    public int compareTo(Order other) {
        if (this.price != other.price) {
            return this.action == Action.BUY ? Double.compare(other.price, this.price) : Double.compare(this.price,
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
