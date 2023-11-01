package nl.rug.aoop;

import nl.rug.aoop.messagequeue.serverside.NetProducer;
import nl.rug.aoop.network.ExchangeListener;
import nl.rug.aoop.networking.client.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * The Trader class represents a participant in the stock exchange, including their name, available funds, and
 * owned stocks.
 */
public class Trader extends NetProducer implements ExchangeListener {
    private String name;
    private int id;
    private double availableFunds;
    private Map<String, Integer> ownedStocks; // Map to track owned stocks (stock symbol -> quantity)

    /**
     * Constructs a Trader with the given client, name, ID, and available funds.
     *
     * @param client          The client representing the trader.
     * @param name            The name of the trader.
     * @param id              The ID of the trader.
     * @param availableFunds  The available funds for trading.
     */
    public Trader(Client client, String name, int id, double availableFunds) {
        super(client);
        this.name = name;
        this.id = id;
        this.availableFunds = availableFunds;
        this.ownedStocks = new HashMap<>();
    }

    /**
     * Get the name of the trader.
     *
     * @return The name of the trader.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ID of the trader.
     *
     * @return The ID of the trader.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the available funds for trading.
     *
     * @return The available funds.
     */
    public double getAvailableFunds() {
        return availableFunds;
    }

    /**
     * Get the owned stocks by the trader.
     *
     * @return A map of owned stocks (stock symbol -> quantity).
     */
    public Map<String, Integer> getOwnedStocks() {
        return ownedStocks;
    }

    /**
     * Set the available funds for trading.
     *
     * @param funds The new available funds.
     */
    public void setAvailableFunds(double funds) {
        availableFunds = funds;
    }

    /**
     * Add owned stocks to the trader's portfolio. This method is used for buying stocks.
     *
     * @param stockSymbol The symbol of the stock to be added.
     * @param quantity    The quantity of the stock to be added.
     */
    public void addOwnedStock(String stockSymbol, int quantity) {
        ownedStocks.put(stockSymbol, ownedStocks.getOrDefault(stockSymbol, 0) + quantity);
    }

    /**
     * Subtract owned stocks from the trader's portfolio. This method is used for selling stocks.
     *
     * @param stockSymbol The symbol of the stock to be subtracted.
     * @param quantity    The quantity of the stock to be subtracted.
     */
    public void subtractOwnedStock(String stockSymbol, int quantity) {
        int currentQuantity = ownedStocks.getOrDefault(stockSymbol, 0);
        if (currentQuantity >= quantity) {
            ownedStocks.put(stockSymbol, currentQuantity - quantity);
        }
    }

    @Override
    public void update() {
        // Get updates from the stock exchange
    }
}
