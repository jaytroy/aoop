package nl.rug.aoop.model;

import nl.rug.aoop.uimodel.TraderDataModel;

import java.util.List;
import java.util.Map;

import nl.rug.aoop.uimodel.TraderDataModel;

import java.util.HashMap;
import java.util.Map;

public class Trader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private Map<String, Integer> ownedStocks;

    public Trader() {
        ownedStocks = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getFunds() {
        return funds;
    }

    public Map<String, Integer> getOwnedStocks() {
        return ownedStocks;
    }

    public void setFunds(double newFunds) {
        funds = newFunds;
    }

    public void addOwnedStock(String stockSymbol, int quantity) {
        if (ownedStocks.containsKey(stockSymbol)) {
            int currentQuantity = ownedStocks.get(stockSymbol);
            int newQuantity = currentQuantity + quantity;
            ownedStocks.put(stockSymbol, newQuantity);
        } else {
            ownedStocks.put(stockSymbol, quantity);
        }
    }

    // Method to remove owned stock
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

    public boolean hasEnoughStock(String stockSymbol, int quantity) {
        if (ownedStocks.containsKey(stockSymbol)) {
            int currentQuantity = ownedStocks.get(stockSymbol);
            return currentQuantity >= quantity;
        }
        return false;
    }
}
