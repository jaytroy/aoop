package nl.rug.aoop.basic;

import nl.rug.aoop.model.TraderDataModel;

import java.util.Map;

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

}
