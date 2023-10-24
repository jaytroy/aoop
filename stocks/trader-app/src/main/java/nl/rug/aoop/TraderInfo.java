package nl.rug.aoop;

import java.util.HashMap;
import java.util.Map;

public class TraderInfo {
    private String name;
    private String id;
    private double availableFunds;
    private Map<String, Integer> ownedStocks; // Map to track owned stocks (stock symbol -> quantity)

    public TraderInfo(String name, String id, double availableFunds) {
        this.name = name;
        this.id = id;
        this.availableFunds = availableFunds;
        this.ownedStocks = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

    public Map<String, Integer> getOwnedStocks() {
        return ownedStocks;
    }

    // update trader's available funds
    public void setAvailableFunds(double funds) {
        availableFunds = funds;
    }

    // add owned stocks
    public void addOwnedStock(String stockSymbol, int quantity) {
        ownedStocks.put(stockSymbol, ownedStocks.getOrDefault(stockSymbol, 0) + quantity);
    }

    // subtract owned stocks
    public void subtractOwnedStock(String stockSymbol, int quantity) {
        int currentQuantity = ownedStocks.getOrDefault(stockSymbol, 0);
        if (currentQuantity >= quantity) {
            ownedStocks.put(stockSymbol, currentQuantity - quantity);
        }
    }
}

