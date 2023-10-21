package nl.rug.aoop.basic;


import nl.rug.aoop.model.StockDataModel;

public class Stock implements StockDataModel {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;
    private double initialPrice;

    public Stock() {

    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public double getPrice() {
        return price;
    }
}
