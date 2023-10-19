package nl.rug.aoop.basic;

import nl.rug.aoop.model.*;

import java.util.List;

public class Trader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private List<String> ownedStocks;

    public Trader(String id, String name, double funds, List<String> ownedStocks) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.ownedStocks = ownedStocks;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getFunds() {
        return funds;
    }

    @Override
    public List<String> getOwnedStocks() {
        return ownedStocks;
    }
    public void setFunds(double newFunds) {
        funds = newFunds;
    }

    public void addOwnedStock(String stockSymbol, int quantity) {
        for (int i = 0; i < quantity; i++) {
            ownedStocks.add(stockSymbol);
        }
    }

    public void removeOwnedStock(String stockSymbol, int quantity) {
        for (int i = 0; i < quantity; i++) {
            ownedStocks.remove(stockSymbol);
        }
    }

    public boolean hasEnoughStock(String stockSymbol, int quantity) {
        int count = 0;
        for (String ownedStock : ownedStocks) {
            if (ownedStock.equals(stockSymbol)) {
                count++;
                if (count >= quantity) {
                    return true;
                }
            }
        }
        return false;
    }
}
