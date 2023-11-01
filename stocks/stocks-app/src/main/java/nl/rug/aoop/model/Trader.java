package nl.rug.aoop.model;

import nl.rug.aoop.uimodel.TraderDataModel;

import java.util.Map;
import java.util.HashMap;

/**
 * The Trader class represents a trader in the trading system.
 */
public class Trader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private Map<String, Integer> ownedStocks;

    /**
     * Default constructor for a Trader. Initializes the ownedStocks map.
     */
    public Trader() {
        ownedStocks = new HashMap<>();
    }

    /**
     * Get the ID of the trader.
     *
     * @return The trader's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the name of the trader.
     *
     * @return The trader's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the funds available to the trader.
     *
     * @return The trader's available funds.
     */
    public double getFunds() {
        return funds;
    }

    /**
     * Get the map of owned stocks and their quantities.
     *
     * @return A map of stock symbols to quantities owned by the trader.
     */
    public Map<String, Integer> getOwnedStocks() {
        return ownedStocks;
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
}
