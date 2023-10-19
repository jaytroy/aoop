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
}
