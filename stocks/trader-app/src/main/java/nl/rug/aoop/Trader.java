package nl.rug.aoop;

import nl.rug.aoop.messagequeue.serverside.NetProducer;
import nl.rug.aoop.network.ExchangeListener;
import nl.rug.aoop.networking.client.Client;

import java.util.HashMap;
import java.util.Map;

public class Trader extends NetProducer implements ExchangeListener { //Should I use inheritance?
    'private String name;
    private int id;
    private double availableFunds;
    private Map<String, Integer> ownedStocks; // Map to track owned stocks (stock symbol -> quantity)

    public Trader(Client client, String name, int id, double availableFunds) {
        super(client);
        this.name = name;
        this.id = id;
        this.availableFunds = availableFunds;
        this.ownedStocks = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
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

    // Method to add owned stocks. Is this buying?
    public void addOwnedStock(String stockSymbol, int quantity) {
        ownedStocks.put(stockSymbol, ownedStocks.getOrDefault(stockSymbol, 0) + quantity);
    }

    // subtract owned stocks. Is this selling?
    public void subtractOwnedStock(String stockSymbol, int quantity) {
        int currentQuantity = ownedStocks.getOrDefault(stockSymbol, 0);
        if (currentQuantity >= quantity) {
            ownedStocks.put(stockSymbol, currentQuantity - quantity);
        }
    }

    @Override
    public void update() { //Get updates from exchange

    }
}
