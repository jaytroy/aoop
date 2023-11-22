package nl.rug.aoop.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.uimodel.TraderDataModel;
import java.util.Map;
import java.util.HashMap;

/**
 * The Trader class represents a trader in the trading system.
 */
public class Trader implements TraderDataModel {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
    @Getter
    private double funds;
    @Getter
    @Setter
    private Map<String, Integer> ownedStocks;

    /**
     * Default constructor for a Trader. Initializes the ownedStocks map.
     */
    public Trader() {
        ownedStocks = new HashMap<>();
    }

    /**
     * Set the funds available to the trader.
     *
     * @param newFunds The new amount of funds for the trader.
     */
    public void setFunds(double newFunds) {
        funds = newFunds;
    }

    /**
     * Add owned stocks to the trader's portfolio.
     *
     * @param stockSymbol The symbol of the stock to be added.
     * @param quantity    The quantity of stock to add.
     */
    public void addOwnedStock(String stockSymbol, int quantity) {
        if (ownedStocks.containsKey(stockSymbol)) {
            int currentQuantity = ownedStocks.get(stockSymbol);
            int newQuantity = currentQuantity + quantity;
            ownedStocks.put(stockSymbol, newQuantity);
        } else {
            ownedStocks.put(stockSymbol, quantity);
        }
    }

    /**
     * Remove owned stocks from the trader's portfolio.
     *
     * @param stockSymbol The symbol of the stock to be removed.
     * @param quantity    The quantity of stock to remove.
     */
    public void removeOwnedStock(String stockSymbol, int quantity) {
        if (ownedStocks.containsKey(stockSymbol)) {
            int currentQuantity = ownedStocks.get(stockSymbol);
            if (currentQuantity > quantity) {
                int newQuantity = currentQuantity - quantity;
                ownedStocks.put(stockSymbol, newQuantity);
            } else if (currentQuantity == quantity) {
                ownedStocks.remove(stockSymbol);
            }
        }
    }

    /**
     * Check if the trader has enough of a specific stock.
     *
     * @param stockSymbol The symbol of the stock to check.
     * @param quantity    The quantity to check for.
     * @return True if the trader owns enough of the specified stock, otherwise false.
     */
    public boolean hasEnoughStock(String stockSymbol, int quantity) {
        if (ownedStocks.containsKey(stockSymbol)) {
            int currentQuantity = ownedStocks.get(stockSymbol);
            return currentQuantity >= quantity;
        }
        return false;
    }

    /**
     * Convert trader info to JSON.
     *
     * @return the json message.
     */

    public String toJson() {
        Gson gson = new GsonBuilder()
                .create();
        return gson.toJson(this);
    }

    /**
     * Turns a string from JSON to message.
     *
     * @param json The JSON string.
     * @return Returns a message.
     */
    public static Trader fromJson(String json) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.fromJson(json, Trader.class);
    }
}
