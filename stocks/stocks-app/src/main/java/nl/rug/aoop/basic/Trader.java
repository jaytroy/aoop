package nl.rug.aoop.basic;

import java.util.Map;

import java.util.List;

public class Trader {
    private String id;
    private String name;
    private double funds;
    private List<Stock> ownedStocks;

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

    public List<Stock> getOwnedStocks() {
        return ownedStocks;
    }

    public void setFunds(double newFunds) {
        funds = newFunds;
    }

    public void addOwnedStock(Stock stock) {
        ownedStocks.add(stock);
    }

    public void removeOwnedStock(Stock stock) {
        ownedStocks.remove(stock);
    }

    public boolean hasEnoughStock(Stock stock, int quantity) {
        int count = 0;
        for (Stock ownedStock : ownedStocks) {
            if (ownedStock.equals(stock)) {
                count++;
                if (count >= quantity) {
                    return true;
                }
            }
        }
        return false;
    }
}
