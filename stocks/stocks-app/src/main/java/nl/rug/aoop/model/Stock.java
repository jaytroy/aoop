package nl.rug.aoop.model;

import nl.rug.aoop.uimodel.StockDataModel;

import java.util.List;

public class Stock implements StockDataModel {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;
    private double initialPrice;

    public Stock() {
        this.price = this.initialPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public double getFunds() {
        return 0;
    }

    @Override
    public List<String> getOwnedStocks() {
        return null;
    }

    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    public double getMarketCap() {
        return price * sharesOutstanding;
    }
    public double getPrice() {
        return price;
    }
}
