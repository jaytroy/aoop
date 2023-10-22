package nl.rug.aoop.ui;

import nl.rug.aoop.model.TraderDataModel;

import java.util.List;

public class Trader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private List<String> ownedStocks;

    public Trader() {
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

    public List<String> getOwnedStocks() {
        return ownedStocks;
    }

    public void setFunds(double newFunds) {
        funds = newFunds;
    }
    public void addOwnedStock(String stockSymbol, int quantity) {
        for (String stockEntry : ownedStocks) {
            if (stockEntry.startsWith(stockSymbol)) {
                int currentQuantity = Integer.parseInt(stockEntry.split(" : ")[1]);
                int newQuantity = currentQuantity + quantity;
                ownedStocks.remove(stockEntry);
                ownedStocks.add(stockSymbol + " : " + newQuantity);
                return;
            }
        }
        ownedStocks.add(stockSymbol + " : " + quantity);
    }


    // Method to remove owned stock
    public void removeOwnedStock(String stockSymbol, int quantity) {
        for (String stockEntry : ownedStocks) {
            if (stockEntry.startsWith(stockSymbol)) {
                int currentQuantity = Integer.parseInt(stockEntry.split(" : ")[1]);
                if (currentQuantity > quantity) {
                    int newQuantity = currentQuantity - quantity;
                    ownedStocks.remove(stockEntry);
                    ownedStocks.add(stockSymbol + " : " + newQuantity);
                } else if (currentQuantity == quantity) {
                    ownedStocks.remove(stockEntry);
                }
                return;
            }
        }
    }

    public boolean hasEnoughStock(String stockSymbol, int quantity) {
        for (String stockEntry : ownedStocks) {
            if (stockEntry.startsWith(stockSymbol)) {
                int currentQuantity = Integer.parseInt(stockEntry.split(" : ")[1]);
                return currentQuantity >= quantity;
            }
        }
        return false;
    }

}
